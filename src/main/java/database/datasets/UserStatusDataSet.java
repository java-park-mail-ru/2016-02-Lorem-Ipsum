package database.datasets;

import datacheck.Constraints;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Installed on 26.03.2016.
 */
@Entity
@Table(name = "status")
public class UserStatusDataSet implements Serializable {

    @SuppressWarnings("InstanceVariableNamingConvention")
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sessionId", nullable = true, length = Constraints.MAX_SESSION_LENGTH)
    private String sessionId;

    @Column(name = "isActive", nullable = false)
    long isActive;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    private UserDataSet user;

    public UserStatusDataSet() {
        this.id = -1;
        this.sessionId = null;
        this.isActive = 0;
    }

    public String getSessionId() { return sessionId; }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public boolean isActive() { return isActive != 0; }

    public void setActive(boolean active) {
        if(active)
            isActive = 1;
        else
            isActive = 0;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getIsActive() {
        return isActive;
    }

    public void setIsActive(long isActive) {
        this.isActive = isActive;
    }

    public UserDataSet getUser() {
        return user;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }
}
