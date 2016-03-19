package fakeclasses;


import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FakeRequest {

    private final ConcurrentMap<String, String> params = new ConcurrentHashMap<>();
    private FakeSessionImpl session;
    private String requestURI;

    public FakeRequest(String login, String password, String email, String sessionId, String reqURI) {
        setParameter("login", login);
        setParameter("password", password);
        setParameter("email", email);
        setSession(sessionId);
        setRequestURI(reqURI);
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public String getRequestURI() { return requestURI; }

    public StringBuffer getRequestURL() { return new StringBuffer(requestURI); }


    public void setParameter(String key, String value) {
        params.put(key, value);
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(String sessionId) {
        session = new FakeSessionImpl(sessionId);
    }

    public void setRequestURI(String reqURI) {
        requestURI = reqURI;
    }

}
