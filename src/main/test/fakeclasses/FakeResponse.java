package fakeclasses;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FakeResponse {

    private int statusCode;
    private String contentType;
    private FakeWriter writer;
    private ByteArrayOutputStream out;

    public FakeResponse() {
        out = new ByteArrayOutputStream();
        writer = new FakeWriter(out);
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getStatusCode() {return statusCode;}

    public String getContentType() { return contentType; }

    public FakeWriter getWriter() {
        return writer;
    }

    public String getContent() { return writer.getData(); }
}
