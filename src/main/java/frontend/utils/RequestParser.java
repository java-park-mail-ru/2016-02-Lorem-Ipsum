package frontend.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Installed on 03.04.2016.
 */
public class RequestParser {

    private static final int STRING_BUFFER_SIZE = 255;

    public static JSONObject parseRequestBody(BufferedReader bfReader) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        char[] charBuffer = new char[STRING_BUFFER_SIZE];

        int nBytesReaded;
        while ((nBytesReaded = bfReader.read(charBuffer)) != -1) {
            stringBuffer.append(charBuffer, 0, nBytesReaded);
        }

        return new JSONObject(stringBuffer.toString());
    }

}
