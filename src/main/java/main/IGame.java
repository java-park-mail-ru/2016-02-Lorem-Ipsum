package main;

import database.datasets.GameResultDataSet;

import java.util.List;

/**
 * Created by Installed on 03.04.2016.
 */
public interface IGame extends IAccountService {
    @SuppressWarnings("SameParameterValue")
    List<GameResultDataSet> getBestResults(int limit);

    void saveGameResultByUserId(long userIdFirst, long scoreFirst, long userIdSecond, long scoreSecond);
}
