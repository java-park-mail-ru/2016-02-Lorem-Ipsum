package fakeclasses;

public class FakeSession {

    private String sessionId;

    public FakeSession(String id) { this.sessionId = id; }

    public String getId() { return sessionId; }

    public void setId(String id) { this.sessionId = id; }

}
