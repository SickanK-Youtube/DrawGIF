import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlacedFrame implements ConfigurationSerializable {
    String id;
    String filename;
    double x;
    double y;
    double z;

    public PlacedFrame(String id, String filename, double x, double y, double z) {
        this.id = id;
        this.filename = filename;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PlacedFrame(Map<?, ?> map) {
        this.id = map.get("id").toString();
        this.filename = map.get("filename").toString();
        this.x = (Double) map.get("x");
        this.y = (Double) map.get("y");
        this.z = (Double) map.get("z");
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return y;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> m = new HashMap();
        m.put("id", id);
        m.put("filename", filename);
        m.put("x", x);
        m.put("y", y);
        m.put("z", z);
        return m;
    }
}
