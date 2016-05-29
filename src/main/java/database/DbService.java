package database;

import database.dao.GameResultDataSetDAO;
import database.dao.StatusDataSetDAO;
import database.dao.UserDataSetDAO;
import database.datasets.GameResultDataSet;
import database.datasets.UserDataSet;
import database.datasets.UserStatusDataSet;
import database.exceptions.gmrsexceptions.GetBestResultsException;
import database.exceptions.gmrsexceptions.SaveGameException;
import database.exceptions.sesexceptions.AddSessionException;
import database.exceptions.sesexceptions.GetSessionException;
import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.*;
import main.IGame;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Date;
import java.util.List;

/**
 * Created by Installed on 26.03.2016.
 */
public class DbService implements IDbService, IGame {

    private SessionFactory sessionFactory;

    public static final Logger LOGGER = LogManager.getLogger("DbLogger");

    public DbService(String hostname, String port, String dbName, String driverName,
                     String login, String password) {

        try {
            Configuration configuration = new Configuration();

            //noinspection StringBufferReplaceableByString
            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").
                    append(hostname).append(':').
                    append(port).append('/').
                    append(dbName);

            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", driverName);
            configuration.setProperty("hibernate.connection.url", url.toString());
            configuration.setProperty("hibernate.connection.username", login);
            configuration.setProperty("hibernate.connection.password", password);
            configuration.setProperty("hibernate.hbm2ddl.auto", "create");

            configuration.addAnnotatedClass(UserDataSet.class);
            configuration.addAnnotatedClass(UserStatusDataSet.class);
            configuration.addAnnotatedClass(GameResultDataSet.class);

            DbConnector dbConnector = new DbConnector(hostname, port, dbName, driverName, login, password);

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
            builder.applySettings(configuration.getProperties());
            builder.applySetting(Environment.DATASOURCE, dbConnector.getDataSource());
            ServiceRegistry serviceRegistry = builder.build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed to init dbService. Reason: {}" , e.getMessage());
            throw e;
        }

    }

    @Override
    public void close() {
        sessionFactory.close();
    }

    @Override
    public boolean checkUserExistsByLogin(String userLogin) throws UserExistsException {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            return userDataSetDAO.checkUserExistsByLogin(userLogin);
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new UserExistsException(e);
        }
    }

    @Override
    public boolean checkSessionExists(String sessionId) throws SessionExistsException {
        try(Session session = sessionFactory.openSession()) {
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            return statusDataSetDAO.checkIfUserIsActiveBySessionId(sessionId);
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new SessionExistsException(e);
        }
    }

    @Override
    public boolean checkUserExistsById(Long userId) throws UserExistsException {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            return userDataSetDAO.checkUserExistsById(userId);
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new UserExistsException(e);
        }
    }

    @Override
    public long addUser(UserProfile userProfile) throws AddUserException {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserStatusDataSet userStatusDataSet = new UserStatusDataSet();
            UserDataSet userDataSet = new UserDataSet(
                    userProfile.getLogin(),
                    userProfile.getPassword(),
                    userProfile.getEmail()
            );
            userStatusDataSet.setUser(userDataSet);
            userDataSet.setCurrentSatus(userStatusDataSet);
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            userDataSetDAO.saveUser(userDataSet);
            statusDataSetDAO.saveStatus(userStatusDataSet);
            transaction.commit();
            return userDataSet.getId();
            //session.close();
        }
        catch (HibernateException e) {
            try {
                if(transaction != null)
                    transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
            throw new AddUserException(e);
        }
    }

    @Override
    public void deleteUser(String sessionId, UserProfile userProfile) throws DeleteUserException {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            userDataSetDAO.deleteUserById(userProfile.getUserId());
            transaction.commit();
        }
        catch (HibernateException e) {
            try {
                if(transaction != null)
                    transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
            throw new DeleteUserException(e);
        }
    }

    @Override
    public void changeUser(UserProfile userProfile) throws ChangeUserException {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            UserDataSet userDataSet = userDataSetDAO.getUserById(userProfile.getUserId());
            userDataSet.setLogin(userProfile.getLogin());
            userDataSet.setPassword(userProfile.getPassword());
            userDataSet.setEmail(userProfile.getEmail());
            userDataSet.setModificationTime(new Date(System.currentTimeMillis()));
            userDataSetDAO.updateUser(userDataSet);

            transaction.commit();
            //session.close();
        }
        catch (HibernateException e) {
            try {
                if(transaction != null)
                    transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
            throw new ChangeUserException(e);
        }
    }

    @Override
    public void deleteSession(String sessionId) throws DeleteUserException {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            statusDataSetDAO.setUserStatusAsNotActiveBySessionId(sessionId);
            transaction.commit();
            //session.close();
        }
        catch (HibernateException e) {
            try {
                if(transaction != null)
                    transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
            throw new DeleteUserException(e);
        }
    }

    @Override
    public void addSession(String sessionId, UserProfile userProfile) throws AddSessionException {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            //userDataSet.setId(userProfile.getId());
            statusDataSetDAO.setUserStatusAsActive(userProfile.getUserId(), sessionId);
            transaction.commit();
            //session.close();
        }
        catch (HibernateException e) {
            try {
                if(transaction != null)
                    transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
            throw new AddSessionException(e);
        }
    }

    @Override
    public UserProfile getUserByLogin(String userLogin) throws GetUserException {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            UserDataSet userDataSet = userDataSetDAO.getUserByLogin(userLogin);
            return userDataSet.toUserProfile();
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new GetUserException(e);
        }
    }

    @Override
    public UserProfile getUserById(Long userId) throws GetUserException {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            UserDataSet userDataSet = userDataSetDAO.getUserById(userId);
            return userDataSet.toUserProfile();
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new GetUserException(e);
        }
    }

    @Override
    public UserProfile getSession(String sessionId) throws GetSessionException {
        try(Session session = sessionFactory.openSession()) {
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            UserDataSet userDataSet = statusDataSetDAO.getUserDataSetBySessionId(sessionId);
            return userDataSet.toUserProfile();
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new GetSessionException(e);
        }
    }

    @Override
    public void saveGameResultByUserId(long userIdFirst, long scoreFirst,
                                       long userIdSecond, long scoreSecond)
    throws SaveGameException
    {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            GameResultDataSetDAO gameResultDataSetDAO = new GameResultDataSetDAO(session);
            UserDataSet userDataSetFirst = userDataSetDAO.getUserById(userIdFirst);
            UserDataSet userDataSetSecond = userDataSetDAO.getUserById(userIdSecond);
            gameResultDataSetDAO.saveGameResult(userDataSetFirst, userDataSetSecond, scoreFirst, scoreSecond);
            transaction.commit();
        }
        catch (HibernateException e) {
            if(transaction != null)
                transaction.rollback();
            LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            throw new SaveGameException(e);
        }
    }

    public void saveGameResultByUserLogin(String userLoginFirst, long scoreFirst,
                                          String userLoginSecond, long scoreSecond)
    throws SaveGameException
    {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            UserDataSet userDataSetFirst = userDataSetDAO.getUserByLogin(userLoginFirst);
            UserDataSet userDataSetSecond = userDataSetDAO.getUserByLogin(userLoginSecond);
            GameResultDataSetDAO gameResultDataSetDAO = new GameResultDataSetDAO(session);
            gameResultDataSetDAO.saveGameResult(userDataSetFirst, userDataSetSecond, scoreFirst, scoreSecond);
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new SaveGameException(e);
        }
    }

    @Override
    public List<GameResultDataSet> getBestResults(int limit) throws GetBestResultsException {
        try(Session session = sessionFactory.openSession()) {
            GameResultDataSetDAO gameResultDataSetDAO = new GameResultDataSetDAO(session);
            return gameResultDataSetDAO.getBestResults(limit);
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            throw new GetBestResultsException(e);
        }
    }



}
