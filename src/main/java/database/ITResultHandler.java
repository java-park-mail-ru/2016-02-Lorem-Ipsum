package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ITResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
