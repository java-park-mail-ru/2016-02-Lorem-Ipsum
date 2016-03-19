package fakeclasses;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FakeResponse {

    private int statusCode;
    private String contentType;
    private final FakeWriter writer;

    public FakeResponse() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer = new FakeWriter(out);
    }

    public int getStatusCode() {return statusCode;}

    public void setStatus(int status) {statusCode = status;}

    public String getContentType() { return contentType; }

    public void setContentType(String contentT) { contentType = contentT; }

    public String getContent() { return writer.getData(); }

    public FakeWriter getWriter() { return writer; }
}
