package database.datasets;

import database.dao.UserDataSetDAO;
import org.json.JSONObject;
import org.json.JSONStringer;

import javax.persistence.*;

/**
 * Created by Installed on 26.03.2016.
 */
@Entity
@Table(name = "gameResults")
public class GameResultDataSet {

    @SuppressWarnings("InstanceVariableNamingConvention")
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userIdFirst")
    private UserDataSet userFirst;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userIdSecond")
    private UserDataSet userSecond;

    @Column(name = "scoreFirst")
    private long scoreFirst;

    @Column(name = "scoreSecond")
    private long scoreSecond;

    @Column(name = "scoreWinner")
    private long scoreWinner;

    public GameResultDataSet() { this.id = -1; }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public UserDataSet getUserFirst() { return userFirst; }

    public UserDataSet getUserSecond() { return userSecond; }

    public void setUserFirst(UserDataSet user) {
        this.userFirst = user;
    }

    public void setUserSecond(UserDataSet user) {
        this.userSecond = user;
    }

    public long getScoreFirst() { return scoreFirst; }

    public long getScoreSecond() { return scoreSecond; }

    public long getScoreWinner() { return scoreWinner; }

    public void setScoreFirst(long score) { this.scoreFirst = score; }

    public void setScoreSecond(long score) { this.scoreSecond = score; }

    public void setScoreWinner(long score) { this.scoreWinner = score; }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("userIdFirst", this.userFirst.getId());
        jsonObject.put("userIdSecond", this.userSecond.getId());
        jsonObject.put("scoreWinner", this.scoreWinner);
        jsonObject.put("scoreFirst", this.scoreFirst);
        jsonObject.put("scoreSecond", this.scoreSecond);
        return jsonObject;
    }

    public GameResultDataSet(JSONObject initObj) {
        this.id = -1;
    }

}
