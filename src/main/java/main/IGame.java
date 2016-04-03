package main;

import database.datasets.GameResultDataSet;

import java.util.List;

/**
 * Created by Installed on 03.04.2016.
 */
public interface IGame extends IAccountService {

    public void saveGameResultByUserId(long userId, long score);
    public List<GameResultDataSet> getBestResults(int limit);
}
