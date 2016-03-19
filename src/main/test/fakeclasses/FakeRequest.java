package fakeclasses;


import org.json.JSONStringer;

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

    public FakeSessionImpl getSession() {return session;}

    public String getParameter(String param) { return params.get(param); }

    public String getRequestURI() {return requestURI;}

    public void setParameter(String key, String value) {
        params.put(key, value);
    }

    public void setSession(String sessionId) {
        session = new FakeSessionImpl(sessionId);
    }

    public void setRequestURI(String reqURI) {
        requestURI = reqURI;
    }

    public String toJSON() {
        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object();
        for (String param : params.keySet()) {
            jsonStringer.key(param).value(params.get(param));
        }
        jsonStringer.key("session").value(session.getId());
        jsonStringer.endObject();
        return jsonStringer.toString();
    }
}
