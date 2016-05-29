package database.datasets;

import datacheck.Constraints;
import main.UserProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Installed on 26.03.2016.
 */
@Entity
@Table(name = "users")
public class UserDataSet implements Serializable {

    @SuppressWarnings("InstanceVariableNamingConvention")
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", nullable = false, length = Constraints.MAX_LOGIN_LENGTH)
    private String login;

    @Column(name = "password", nullable = false, length = Constraints.MAX_PASSWORD_LENGTH)
    private String password;

    @Column(name = "email", nullable = false, length = Constraints.MAX_EMAIL_LENGTH)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "creationTime",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "modificationTime",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    private Date modificationTime;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "user")
    private UserStatusDataSet currentSatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="userFirst")
    private List<GameResultDataSet> gameResults;

    public UserDataSet() {
        this.id = -1;
    }

    public UserDataSet(String login, String password, String email) {
        this.id = -1;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Date getCreationTime() { return creationTime; }

    public void setCreationTime(Date creationTime) { this.creationTime = creationTime; }

    public Date getModificationTime() { return modificationTime; }

    public void setModificationTime(Date modificationTime) { this.modificationTime = modificationTime; }

    public UserStatusDataSet getCurrentSatus() { return currentSatus; }

    public void setCurrentSatus(UserStatusDataSet currentSatus) { this.currentSatus = currentSatus; }

    public List<GameResultDataSet> getGameResults() {
        return gameResults;
    }

    public void setGameResults(List<GameResultDataSet> gameResults) {
        this.gameResults = gameResults;
    }

    public UserProfile toUserProfile() {
        return new UserProfile(id, login, password, email);
    }
}
