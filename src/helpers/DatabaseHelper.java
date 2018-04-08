package helpers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Data;

import java.sql.*;

public class DatabaseHelper {

    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");

            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Result (player VARCHAR(50), score INT)");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }

    public ObservableList<Data> getResults() {
        ObservableList<Data> data = FXCollections.observableArrayList();
        try {
            Statement statement = getConnection().createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM Result ORDER BY score DESC LIMIT 5");

            while (rs.next()) {
                data.add(new Data(rs.getString("player"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (getConnection() != null)
                    getConnection().close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        System.out.println(data.toString());
        return data;
    }

    public static void writeResult(String player, int score) {
        System.out.println(player + " " + score);
        try {
            Statement statement = getConnection().createStatement();

            statement.executeUpdate("DELETE FROM Result WHERE EXISTS (SELECT * FROM Result WHERE player = '" + player + "')");
            statement.executeUpdate("INSERT INTO Result VALUES ('" + player + "', " + score + ")");

            ResultSet rs = statement.executeQuery("SELECT * FROM Result");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (getConnection() != null)
                    getConnection().close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
}
