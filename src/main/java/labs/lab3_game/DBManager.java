package labs.lab3_game;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DBManager {
  private SessionFactory session;

  public void connect() {
    session = HibernateSessionFactoryUtil.getSessionFactory();
    session.openSession().close();
  }

  public void registerPlayer(String name) {
    Session s = session.openSession();
    Transaction t = s.beginTransaction();
    try {
      s.save(new PlayerDB(name));
      t.commit();
      s.close();
    } catch (Exception e) {
      t.rollback();
      //e.printStackTrace();
    } finally {
      s.close();
    }
  }

  public int getPlayerWins(String name) {
    Session s = session.openSession();
    //Query q = s.createQuery("FROM Players WHERE name = :name");
    Query<PlayerDB> q = s.createQuery("FROM labs.lab3_game.PlayerDB WHERE name LIKE :name");
    q.setParameter("name", name);
    List<PlayerDB> players = (List<PlayerDB>)q.list();
    s.close();
    return players.get(0).wins;
  }

  public MyUtils.PlayerWinsArray getLeaderBoard() {
    MyUtils.PlayerWinsArray arr = null;
    List<PlayerDB> players = (List<PlayerDB>)session.openSession().createQuery("FROM labs.lab3_game.PlayerDB").list();
    int sz = players.size();
    arr = new MyUtils.PlayerWinsArray(sz);
    int i = 0;
    for (PlayerDB p : players) {
      arr.arr[i] = new MyUtils.PlayerWins(p);
      i++;
    }
    return arr;
  }

  public void incrementPlayerWins(String name) {
    Session s = session.openSession();
    Query<PlayerDB> q = s.createQuery("FROM labs.lab3_game.PlayerDB WHERE name LIKE :name");
    q.setParameter("name", name);
    List<PlayerDB> players = (List<PlayerDB>)q.list();
    PlayerDB p = players.get(0);
    p.wins++;
    Transaction t = s.beginTransaction();
    s.update(p);
    t.commit();
    s.close();
  }
}

/* 
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
      st.executeUpdate("CREATE TABLE IF NOT EXISTS Players ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, wins INTEGER DEFAULT 0 );");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void registerPlayer(String name) {
    try {
      PreparedStatement st = connection.prepareStatement("INSERT OR IGNORE INTO Players (name) VALUES (?)");
      st.setString(1, name);
      st.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int getPlayerWins(String name) {
    try {
      PreparedStatement st = connection.prepareStatement("SELECT wins FROM Players WHERE name LIKE ?");
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
      ResultSet r = st.executeQuery("SELECT COUNT(*) AS cnt FROM Players");
      int count = r.getInt("cnt");
      r = st.executeQuery("SELECT name, wins FROM Players ORDER BY wins DESC");
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

  public void incrementPlayerWins(String name) {
    try {
      PreparedStatement st = connection.prepareStatement("UPDATE Players SET wins = wins + 1 WHERE name LIKE ?");
      st.setString(1, name);
      st.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}

*/
