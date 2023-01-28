package ru.thedreamingsaviour.game.resourceloader.database;

import ru.thedreamingsaviour.game.resourceloader.database.entity.Save;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final DatabaseHandler databaseHandler = new DatabaseHandler();

    public static DatabaseHandler getInstance() {
        return databaseHandler;
    }

    private final Connection CONNECTION;

    private DatabaseHandler() {
        String connectionString = "jdbc:postgresql://localhost:5432/tds";
        try {
            Class.forName("org.postgresql.Driver");
            CONNECTION = DriverManager.getConnection(connectionString, "postgres", "890123890123");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertSave(long playerId, long score, String level) {
        try {
            String request = "insert into save (save_id, player_id, score, level) values(?,?,?,?)";
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(request);
            preparedStatement.setLong(1, generateNewId("save", "save_id"));
            preparedStatement.setLong(2, playerId);
            preparedStatement.setLong(3, score);
            preparedStatement.setString(4, level);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public List<Save> getSaveList(long playerId) {
        List<Save> saves = new ArrayList<>();
        try {
            String request = "select * from cave where player_id = ?";
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(request);
            preparedStatement.setLong(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Save save = new Save();
                save.setSaveId(resultSet.getLong("save_id"));
                save.setPlayerId(resultSet.getLong("player_id"));
                save.setScore(resultSet.getLong("score"));
                save.setLevel(resultSet.getString("level"));

                saves.add(save);
            }
            return saves;
        } catch (Exception exception) {
            return saves;
        }
    }

    public boolean updateShipmentStatus(Long saveId, Long score, String level) {
        try {
            String request = "update save set score = ?, level = ? where save_id = ?";
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(request);
            preparedStatement.setLong(1, score);
            preparedStatement.setString(2, level);
            preparedStatement.setLong(3, saveId);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean deleteSave(Long saveId) {
        try {
            String request = "delete from save where save_id = ?";
            PreparedStatement preparedStatement = CONNECTION.prepareStatement(request);
            preparedStatement.setLong(1, saveId);
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private long generateNewId(String table, String id) throws SQLException {
        String request = "select " + id + " from " + table;
        PreparedStatement preparedStatement = CONNECTION.prepareStatement(request);
        ResultSet resultSet = preparedStatement.executeQuery();
        long newId = 0;
        try {
            while (resultSet.next()) {
                long thisId = Long.parseLong(resultSet.getString(id));
                if (newId < thisId) {
                    newId = thisId;
                }
            }
        } catch (Exception ignored) {
        }
        return ++newId;
    }
}
