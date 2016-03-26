package main;

import database.DbConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.String;
import java.sql.Connection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService implements IAccountService {

    private final ConcurrentMap<String, Long> usersIDs = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, UserProfile> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UserProfile> sessions = new ConcurrentHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger(AccountService.class);

    private String hostname;
    private String port;
    private String dbName;
    private String login;
    private String password;
    private String driver;

    public AccountService() {
        this.hostname = Main.STANDART_MYSQL_HOST;
        this.port = Main.STANDART_MYSQL_PORT;
        this.dbName = Main.STANDART_MYSQL_DB_NAME;
        this.login = Main.STANDART_MYSQL_LOGIN;
        this.password = Main.STANDART_MYSQL_PASSWORD;
        this.driver = Main.STANDART_MYSQL_DRIVER;
    }

    public AccountService(String hostname, String port, String dbName, String driver, String login, String password) {
        this.hostname = hostname;
        this.port = port;
        this.dbName = dbName;
        this.login = login;
        this.password = password;
        this.driver = driver;
    }

    //private Connection getConnection() {
        //return DbConnector.getConnectionFromPool(hostname, port, dbName, driver, login, password);
    //}

    @Override
    public boolean checkUserExistsByLogin(String userLogin){
        LOGGER.debug("Checked existance of user with login {}", userLogin);
        return usersIDs.containsKey(userLogin);
    }

    @Override
    public boolean checkSessionExists(String sessionId) {
        LOGGER.debug("Checked existance of session with {}", sessionId);
        return sessions.containsKey(sessionId);
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        LOGGER.debug("Checked existance of user with id {}", userId);
        return users.containsKey(userId);
    }

    @Override
    public long addUser(UserProfile userProfile) {
        usersIDs.put(userProfile.getLogin(), userProfile.getUserId());
        users.put(userProfile.getUserId(), userProfile);
        LOGGER.debug("User was added: {}", userProfile.toJSON());
        return -1;
    }

    @Override
    public void deleteUser(String sessionId, UserProfile userProfile) {
        sessions.remove(sessionId);
        usersIDs.remove(userProfile.getLogin());
        users.remove(userProfile.getUserId());
        LOGGER.debug("User with session {} was deleted: {}", sessionId, userProfile.toJSON());
    }

    @Override
    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
        LOGGER.debug("Session deleted, id {}", sessionId);
    }

    @Override
    public void addSession(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
        LOGGER.debug("User with session {} was added: {}", sessionId, userProfile.toJSON());
    }

    @Override
    public UserProfile getUserByLogin(String userLogin) {
        LOGGER.debug("User got by login: {}", userLogin);
        return users.get(usersIDs.get(userLogin));
    }

    @Override
    public UserProfile getUserById(Long userId) {
        LOGGER.debug("User got by id: {}", userId);
        return users.get(userId);
    }

    @Override
    public UserProfile getSession(String sessionId) {
        LOGGER.debug("Session got by id: {}", sessionId);
        return sessions.get(sessionId);
    }

    public void changeUser(UserProfile userProfile) {return;}


}
