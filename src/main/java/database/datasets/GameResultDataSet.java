package database.datasets;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Installed on 26.03.2016.
 */
@Entity
@Table(name = "gameResults")
public class GameResultDataSet {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserDataSet user;

    @Column(name = "score")
    private long score;

    public GameResultDataSet() { this.id = -1; }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public UserDataSet getUser() { return user; }

    public long getScore() { return score; }

    public void setScore(long score) { this.score = score; }
}
