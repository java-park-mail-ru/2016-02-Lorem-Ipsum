package main;

import database.datasets.GameResultDataSet;
import database.exceptions.gmrsexceptions.GetBestResultsException;
import database.exceptions.gmrsexceptions.SaveGameException;

import java.util.List;

/**
 * Created by Installed on 03.04.2016.
 */
public interface IGame extends IAccountService {
    List<GameResultDataSet> getBestResults(int limit) throws GetBestResultsException;

    void saveGameResultByUserId(long userIdFirst, long scoreFirst,
                                long userIdSecond, long scoreSecond)
            throws SaveGameException;
}
