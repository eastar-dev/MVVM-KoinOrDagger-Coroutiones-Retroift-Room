package android.volley;

import android.log.Log;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class Pojo {
    private String filename;
    private String json;
    private String pojo;

    private Pojo() { }

    public static Pojo create(String json) {
        return create(new Exception().getStackTrace()[1].getFileName(), json);
    }
    public static Pojo create(String filename, String json) {
        final Pojo pojo = new Pojo();
        pojo.filename = filename;
        pojo.json = json;
        pojo.gen();
        return pojo;
    }

    public Pojo gen() {
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject jo = new JSONObject(json);
            sb.append("public Data data;");
            generateClass(sb, "Data", jo);
            sb.insert("public Data data;public ".length(), " static ");
        } catch (JSONException ee) {
            ee.printStackTrace();
        }

        this.pojo = sb.toString();
        return this;
    }

    public Pojo toLog() {
        Log.e("generate pojo class for " + filename, "START>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        Log.e(json);
        System.out.println(pojo);
        System.out.println("(" + filename + ":0)");
        Log.e("generate pojo class for " + filename, "END  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return this;
    }

    public Pojo toFile() {
        try {
            final File dirPath = new File(Environment.getExternalStorageDirectory(), "/_flog");
            if (!dirPath.exists())
                dirPath.mkdirs();
            final File pojo = new File(dirPath, filename);
            if (!pojo.exists()) {
                try {
                    pojo.createNewFile();
                } catch (IOException e) {
                }
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(pojo));
            writer.write(this.pojo);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public String toString() {
        return pojo;
    }

    private void generateClass(StringBuilder sb, String name, Object value) {
        sb.append("public class " + getClzName(name) + "{");
        JSONObject jo = (JSONObject) value;
        Iterator<String> it = jo.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            Object o = jo.opt(key);
            if (o instanceof Boolean)
                sb.append("public boolean " + key + ";");
            if (o instanceof String)
                sb.append("public String " + key + ";");
            if (o instanceof Integer)
                sb.append("public int " + key + ";");
            if (o instanceof Long)
                sb.append("public long " + key + ";");
            if (o instanceof Double)
                sb.append("public double " + key + ";");
            if (o instanceof JSONArray)
                generateArray(sb, key, o);
            if (o instanceof JSONObject) {
                sb.append("public " + getClzName(key) + " " + key + ";");
                generateClass(sb, key, o);
            }
        }
        sb.append("}");
    }

    private void generateArray(StringBuilder sb, String name, Object value) {
        JSONArray ja = (JSONArray) value;
        for (int i = 0; i < 1; i++) {
            Object o = ja.opt(i);
            if (o instanceof Boolean)
                sb.append("public List<Boolean> " + name + ";");
            if (o instanceof String)
                sb.append("public List<String> " + name + ";");
            if (o instanceof Integer)
                sb.append("public List<Integer> " + name + ";");
            if (o instanceof Long)
                sb.append("public List<Long> " + name + ";");
            if (o instanceof Double)
                sb.append("public List<Double> " + name + ";");
            if (o instanceof JSONArray)
                generateArray(sb, name, o);
            if (o instanceof JSONObject) {
                sb.append("public List<" + getClzName(name) + "> " + name + ";");
                generateClass(sb, name, o);
            }
        }
    }

    public String getClzName(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1, (text.charAt(text.length() - 1) == 's' ? text.length() - 1 : text.length()));
    }
}