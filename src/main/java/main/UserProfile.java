package main;

import org.json.JSONStringer;

public class UserProfile {
    private long userId;
    private String login;
    private String password;
    private String email;

    public UserProfile(long id, String login, String password, String email) {
        this.userId = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() { return password; }

    public String getEmail() {
        return email;
    }

    public void setLogin(String login) { this.login = login; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public boolean equal(UserProfile second) {
        return this.getUserId() == second.getUserId() && this.getLogin().equals(second.getLogin()) &&
                this.getPassword().equals(second.getPassword()) && this.getEmail().equals(second.getEmail());
    }

    public boolean semanticEqual(UserProfile second) {
        return this.getLogin().equals(second.getLogin()) &&
                this.getPassword().equals(second.getPassword()) && this.getEmail().equals(second.getEmail());
    }

    public UserProfile clone() {
        return new UserProfile(userId, login, password, email);
    }

    public String toJSON() {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.
                object()
                .key("id").value(getUserId())
                .key("login").value(getLogin())
                .key("email").value(getEmail());
        jsonStringer.endObject();
        return jsonStringer.toString();
    }

}
