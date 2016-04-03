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
    @JoinColumn(name = "userId")
    private UserDataSet user;

    @Column(name = "score")
    private long score;

    public GameResultDataSet() { this.id = -1; }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public UserDataSet getUser() { return user; }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    public long getScore() { return score; }

    public void setScore(long score) { this.score = score; }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("login", this.user.getLogin());
        jsonObject.put("score", this.score);
        return jsonObject;
    }

    public GameResultDataSet(JSONObject initObj) {
        this.id = -1;
    }

}
