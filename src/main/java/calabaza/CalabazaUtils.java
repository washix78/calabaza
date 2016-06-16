package calabaza;

import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class CalabazaUtils {

    public static Object convertFromJson(Object json) {
        if (json instanceof String) {
            String src = (String) json;
            if (src.startsWith("[")) {
                return reconvertFromJson(new JSONArray(src));
            } else if (src.startsWith("{")) {
                return reconvertFromJson(new JSONObject(src));
            } else {
                return null;
            }
        } else if (json instanceof JSONObject || json instanceof JSONArray) {
            return reconvertFromJson(json);
        } else {
            return null;
        }
    }

    public static Object convertToJson(Object obj) {
        if (obj instanceof Map || obj instanceof List) {
            return reconvertToJson(obj);
        } else {
            throw new RuntimeException("not supported class.");
        }
    }

    public static String getTextFromFile(Path filePath) throws IOException {
        try (
            BufferedReader in = Files.newBufferedReader(filePath, Charset.forName("UTF-8"));
        ) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    public static void outputToFile(Path filePath, String text) throws IOException {
        try (
            BufferedWriter out = Files.newBufferedWriter(filePath, Charset.forName("UTF-8"), WRITE, CREATE, TRUNCATE_EXISTING);
        ) {
            out.write(text);
        }
    }

    public static String getTextFromReader(BufferedReader in) throws IOException {
        try {
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            in.close();
        }
    }
    
    public static void outputToStream(PrintWriter out, String text) throws IOException {
        try {
            out.write(text);
        } finally {
            out.close();
        }
    }

    private static Object reconvertFromJson(Object obj) {
        Object result = null;
        if (obj instanceof JSONObject) {
            Map<String, Object> map = new HashMap<>();
            JSONObject jo = (JSONObject) obj;
            for (Iterator<String> ite = jo.keys(); ite.hasNext();) {
                String key = ite.next();
                map.put(key, reconvertFromJson(jo.get(key)));
            }
            result = map;
        } else if (obj instanceof JSONArray) {
            List<Object> list = new ArrayList<>();
            JSONArray ja = (JSONArray) obj;
            for (int i = 0, end = ja.length(); i != end; i++) {
                list.add(reconvertFromJson(ja.get(i)));
            }
            result = list;
        } else if (obj instanceof Integer) {
            result = (Integer) obj;
        } else if (obj != JSONObject.NULL) {
            result = (String) obj;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Object reconvertToJson(Object obj) {
        Object result = null;
        if (obj == null) {
            result = JSONObject.NULL;
        } else if (obj instanceof Map) {
            JSONObject jo = new JSONObject();
            Map<String, Object> map = (Map<String, Object>) obj;
            for (String key : map.keySet()) {
                jo.put(key, reconvertToJson(map.get(key)));
            }
            result = jo;
        } else if (obj instanceof List) {
            JSONArray ja = new JSONArray();
            List<Object> list = (List<Object>) obj;
            for (Object lo : list) {
                ja.put(reconvertToJson(lo));
            }
            result = ja;
        } else {
            result = obj;
        }
        return result;
    }

}
