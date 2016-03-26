package database.dao;

import database.datasets.UserDataSet;
import database.datasets.UserStatusDataSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Installed on 26.03.2016.
 */
public class StatusDataSetDAO {

    private Session session;

    public StatusDataSetDAO(Session session) {this.session = session;}

    public UserStatusDataSet getUserStatus(UserDataSet userDataSet) {
        Criteria criteria = session.createCriteria(UserStatusDataSet.class);
        criteria.add(Restrictions.eq("user", userDataSet));
        return (UserStatusDataSet) criteria.uniqueResult();
    }

    public UserStatusDataSet getUserStatusBySessionId(String sessionId) {
        Criteria criteria = session.createCriteria(UserStatusDataSet.class);
        criteria.add(Restrictions.eq("sessionId", sessionId));
        return (UserStatusDataSet) criteria.uniqueResult();
    }

    public UserDataSet getUserDataSetBySessionId(String sessionId) {
        UserStatusDataSet userStatusDataSet = getUserStatusBySessionId(sessionId);
        return userStatusDataSet.getUser();
    }

    public void saveStatus(UserStatusDataSet userStatusDataSet) {
        session.save(userStatusDataSet);
    }

    public void updateStatus(UserStatusDataSet userStatusDataSet) {
        session.update(userStatusDataSet);
    }

    public boolean checkIfUserIsActive(UserDataSet userDataSet) {
        UserStatusDataSet userStatusDataSet = getUserStatus(userDataSet);
        return userStatusDataSet.isActive();
    }

    public boolean checkIfUserIsActiveBySessionId(String sessionId) {
        UserStatusDataSet userStatusDataSet = getUserStatusBySessionId(sessionId);
        if(userStatusDataSet == null)
            return false;
        return userStatusDataSet.isActive();
    }

    public void setUserStatusAsActive(long userId, String sessionId) {
        UserDataSetDAO userDataSetDAO = new UserDataSetDAO(session);
        UserDataSet userDataSet = userDataSetDAO.getUserById(userId);
        UserStatusDataSet userStatusDataSet = getUserStatus(userDataSet);
        if(userStatusDataSet.isActive())
            return;
        userStatusDataSet.setActive(true);
        userStatusDataSet.setSessionId(sessionId);
        updateStatus(userStatusDataSet);
    }

    public void setUserStatusAsNotActive(UserStatusDataSet userStatusDataSet) {
        if(!userStatusDataSet.isActive())
            return;
        userStatusDataSet.setActive(false);
        userStatusDataSet.setSessionId(null);
        updateStatus(userStatusDataSet);
    }

    public void setUserStatusAsNotActiveBySessionId(String sessionId) {
        UserStatusDataSet userStatusDataSet = getUserStatusBySessionId(sessionId);
        setUserStatusAsNotActive(userStatusDataSet);
    }
}
