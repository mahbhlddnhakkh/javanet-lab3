package labs.lab3_game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

  private Connection connection;

  public void connect() {
    try {
    Class.forName("org.sqlite.JDBC");
    connection = DriverManager.getConnection("jdbc:sqlite:data.db");
    Statement st = connection.createStatement();
    st.executeUpdate("CREATE TABLE IF NOT EXISTS Users ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, wins INTEGER DEFAULT 0 );");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void registerPlayer(String name) {
    try {
      PreparedStatement st = connection.prepareStatement("INSERT OR IGNORE INTO Users (name) VALUES (?)");
      st.setString(1, name);
      st.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int getPlayerWins(String name) {
    try {
      PreparedStatement st = connection.prepareStatement("SELECT wins FROM Users WHERE name == ?");
      st.setString(1, name);
      ResultSet r = st.executeQuery();
      while(r.next()) {
        return r.getInt("wins");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -999;
  }

  public MyUtils.PlayerWinsArray getLeaderBoard() {
    try {
      MyUtils.PlayerWinsArray arr = null;
      Statement st = connection.createStatement();
      ResultSet r = st.executeQuery("SELECT COUNT(*) AS cnt FROM Users");
      int count = r.getInt("cnt");
      r = st.executeQuery("SELECT name, wins FROM Users ORDER BY wins DESC");
      // We actually guarantee that we have at least one score
      arr = new MyUtils.PlayerWinsArray(count);
      int i = 0;
      while (r.next()) {
        arr.arr[i] = new MyUtils.PlayerWins(r.getInt("wins"), r.getString("name"));
        i++;
      }
      return arr;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean incrementPlayerWins(String name) {
    try {
      PreparedStatement st = connection.prepareStatement("UPDATE Users SET wins = wins + 1 WHERE name LIKE ?");
      st.setString(1, name);
      int ret = st.executeUpdate();
      return 1 == ret;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
