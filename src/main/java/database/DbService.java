package database;

import database.dao.StatusDataSetDAO;
import database.dao.UserDataSetDAO;
import database.datasets.GameResultDataSet;
import database.datasets.UserDataSet;
import database.datasets.UserStatusDataSet;
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
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * Created by Installed on 26.03.2016.
 */
public class DbService implements IDbService {

    private SessionFactory sessionFactory;
    private DbConnector dbConnector;

    public static final Logger LOGGER = LogManager.getLogger("DbLogger");

    public DbService(String hostname, String port, String dbName, String driverName,
                     String login, String password) {

        try {
            Configuration configuration = new Configuration();

            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").
                    append(hostname).append(":").
                    append(port).append("/").
                    append(dbName);

            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", driverName);
            configuration.setProperty("hibernate.connection.url", url.toString());
            configuration.setProperty("hibernate.connection.username", login);
            configuration.setProperty("hibernate.connection.password", password);
            //configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "create");

            configuration.addAnnotatedClass(UserDataSet.class);
            configuration.addAnnotatedClass(UserStatusDataSet.class);
            configuration.addAnnotatedClass(GameResultDataSet.class);

            dbConnector = new DbConnector(hostname, port, dbName, driverName, login, password);

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

    public void close() {
        sessionFactory.close();
    }

    public boolean checkUserExistsByLogin(String userLogin) {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            boolean exists = userDataSetDAO.checkUserExistsByLogin(userLogin);
            //session.close();
            return exists;
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            return false;
        }
    }

    public boolean checkSessionExists(String sessionId) {
        try(Session session = sessionFactory.openSession()) {
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            boolean exists = statusDataSetDAO.checkIfUserIsActiveBySessionId(sessionId);
            //session.close();
            return exists;
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            return false;
        }
    }

    public boolean checkUserExistsById(Long userId) {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            boolean exists = userDataSetDAO.checkUserExistsById(userId);
            //session.close();
            return exists;
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            return false;
        }
    }

    public long addUser(UserProfile userProfile) {
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
                transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
                return -1;
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
                return  -1;
            }
        }
    }

    public void deleteUser(String sessionId, UserProfile userProfile) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            userDataSetDAO.deleteUserById(userProfile.getUserId());
            transaction.commit();
        }
        catch (HibernateException e) {
            try {
                transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
        }
    }

    public void changeUser(UserProfile userProfile) {
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
                transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
        }
    }

    public void deleteSession(String sessionId) {
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
                transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
        }
    }

    public void addSession(String sessionId, UserProfile userProfile) {
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
                transaction.rollback();
                LOGGER.debug("Failed db operation. Rolled back. Reason: {}" , e.getMessage());
            }
            catch (NullPointerException e2) {
                LOGGER.debug("Failed db operation. Failed to roll back. Reason: {}" , e2.getMessage());
            }
        }
    }

    public UserProfile getUserByLogin(String userLogin) {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            UserDataSet userDataSet = userDataSetDAO.getUserByLogin(userLogin);
            return userDataSet.toUserProfile();
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            return null;
        }
    }

    public UserProfile getUserById(Long userId) {
        try(Session session = sessionFactory.openSession()) {
            UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
            UserDataSet userDataSet = userDataSetDAO.getUserById(userId);
            return userDataSet.toUserProfile();
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            return null;
        }
    }

    public UserProfile getSession(String sessionId) {
        try(Session session = sessionFactory.openSession()) {
            StatusDataSetDAO statusDataSetDAO = new StatusDataSetDAO(session);
            UserDataSet userDataSet = statusDataSetDAO.getUserDataSetBySessionId(sessionId);
            return userDataSet.toUserProfile();
        }
        catch (HibernateException e) {
            LOGGER.debug("Failed db operation. Reason: {}" , e.getMessage());
            return null;
        }
    }

}
