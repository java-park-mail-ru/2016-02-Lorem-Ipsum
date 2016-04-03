package database.dao;

import database.datasets.GameResultDataSet;
import database.datasets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.List;

/**
 * Created by Installed on 26.03.2016.
 */
public class GameResultDataSetDAO {

    private final Session session;

    public GameResultDataSetDAO(Session session) {this.session = session;}

    public void saveResult(GameResultDataSet resultDataSet) {session.save(resultDataSet);}

    public void saveGameResult(UserDataSet userDataSet, long score) {
        GameResultDataSet gameResultDataSet = new GameResultDataSet();
        gameResultDataSet.setUser(userDataSet);
        gameResultDataSet.setScore(score);
        saveResult(gameResultDataSet);
    }

    public List<GameResultDataSet> getBestResults(int limit) {
        Criteria criteria = session.createCriteria(GameResultDataSet.class);
        criteria.addOrder(Order.desc("score"));
        criteria.setMaxResults(limit);
        return (List<GameResultDataSet>) criteria.list();
    }
}
