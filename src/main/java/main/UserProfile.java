package main;

public class UserProfile {
    private final long userId;
    private String login;
    private String password;
    private String email;

    public UserProfile(long id, String login, String password, String email) {
        this.userId = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public long getId() { return userId; }

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

}
