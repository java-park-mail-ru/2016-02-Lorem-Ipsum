package fakeclasses;

import java.io.OutputStream;
import java.io.PrintWriter;

public class FakeWriter extends PrintWriter {

    public FakeWriter(OutputStream out) {
        super(out);
    }

    private StringBuilder data = new StringBuilder();

    public void println (String input) {
        data.append(input);
    }

    public String getData() {
        String output = data.toString();
        data.setLength(0);
        return output;
    }
}
