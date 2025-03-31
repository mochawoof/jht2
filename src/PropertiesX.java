import java.util.Properties;
import java.io.*;

class PropertiesX extends Properties {
    public String path = "jht2.properties";

    public PropertiesX() {
        try {
            FileInputStream in = new FileInputStream(path);
            load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String get(String k, String def) {
        String got = getProperty(k);
        return (got == null) ? def : got;
    }

    public void set(String k, String o) {
        setProperty(k, o);
        save();
    }

    public void save() {
        try {
            FileOutputStream out = new FileOutputStream(path, false);
            store(out, "");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}