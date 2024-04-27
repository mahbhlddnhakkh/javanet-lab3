package labs.lab3_game;

import jakarta.persistence.*;

@Entity
@Table (name = "players")
public class PlayerDB {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int id;

  @Column(name = "name")
  public String name;
  
  @Column(name = "wins")
  public long wins;

  public PlayerDB() {}
}
