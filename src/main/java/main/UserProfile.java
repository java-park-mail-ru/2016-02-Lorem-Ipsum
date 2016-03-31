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

    @SuppressWarnings("MisspelledEquals")
    public boolean equal(UserProfile second) {
        return this.userId == second.userId && semanticEqual(second);
    }

    public boolean semanticEqual(UserProfile second) {
        return this.login.equals(second.login) &&
                this.password.equals(second.password) && this.email.equals(second.email);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public UserProfile clone() {
        return new UserProfile(userId, login, password, email);
    }

    public String toJSON() {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.
                object()
                .key("id").value(this.userId)
                .key("login").value(this.login)
                .key("email").value(this.email);
        jsonStringer.endObject();
        return jsonStringer.toString();
    }

}
