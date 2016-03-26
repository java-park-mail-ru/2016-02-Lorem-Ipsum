package database.dao;

import database.datasets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Created by Installed on 26.03.2016.
 */
public class UserDataSetDAO {

    private Session session;

    public UserDataSetDAO(Session session) {this.session = session;}

    public void saveUser(UserDataSet userDataSet) {
        session.save(userDataSet);
    }

    public void updateUser(UserDataSet userDataSet) {
        session.update(userDataSet);
    }

    public void deleteUser(UserDataSet userDataSet) {
        session.delete(userDataSet);
    }

    public void deleteUserById(long userId) {
        UserDataSet userDataSet = getUserById(userId);
        deleteUser(userDataSet);
    }

    public UserDataSet getUserById(long id) {
        return (UserDataSet) session.get(UserDataSet.class, id);
    }

    public UserDataSet getUserByLogin(String login) {
        Criteria criteria = session.createCriteria(UserDataSet.class);
        criteria.add(Restrictions.eq("login", login));
        UserDataSet userDataSet = (UserDataSet) criteria.uniqueResult();
        return userDataSet;
    }

    public boolean checkUserExistsById(long id) {
        UserDataSet userDataSet = getUserById(id);
        return userDataSet != null;
    }

    public boolean checkUserExistsByLogin(String login) {
        UserDataSet userDataSet = getUserByLogin(login);
        return userDataSet != null;
    }

}
