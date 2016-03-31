package fakeclasses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FakeResponse {

    private int statusCode;
    private String contentType;
    private FakeWriter writer;

    public FakeResponse() {
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writer = new FakeWriter(out);
        }
        catch (IOException e) {
            writer = null;
        }
    }

    public int getStatusCode() {return statusCode;}

    public void setStatus(int status) {statusCode = status;}

    public String getContentType() { return contentType; }

    public void setContentType(String contentT) { contentType = contentT; }

    public String getContent() { return writer.getData(); }

    public FakeWriter getWriter() { return writer; }
}
