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

    public void saveGameResult(UserDataSet userDataSetFirst, UserDataSet userDataSetSecond, long scoreFirst, long scoreSecond) {
        GameResultDataSet gameResultDataSet = new GameResultDataSet();
        gameResultDataSet.setScoreFirst(scoreFirst);
        gameResultDataSet.setScoreSecond(scoreSecond);
        gameResultDataSet.setUserFirst(userDataSetFirst);
        gameResultDataSet.setUserSecond(userDataSetSecond);
        if(scoreFirst < scoreSecond) {
            gameResultDataSet.setScoreWinner(scoreSecond);
        }
        else {
            gameResultDataSet.setScoreWinner(scoreFirst);
        }
        saveResult(gameResultDataSet);
    }

    public List<GameResultDataSet> getBestResults(int limit) {
        Criteria criteria = session.createCriteria(GameResultDataSet.class);
        criteria.addOrder(Order.desc("scoreWinner"));
        criteria.setMaxResults(limit);
        return (List<GameResultDataSet>) criteria.list();
    }
}
