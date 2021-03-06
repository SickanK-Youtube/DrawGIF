import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ImagePiece implements ConfigurationSerializable {
    int mapViewId;
    String filename;
    int x;
    int y;

    public ImagePiece(int mapViewId, String filename, int x, int y){
        this.mapViewId = mapViewId;
        this.filename = filename;
        this.x = x;
        this.y = y;
    }

    public ImagePiece(Map<?, ?> map) {
        this.filename = map.get("filename").toString();
        this.x = (Integer) map.get("x");
        this.y = (Integer) map.get("y");
        this.mapViewId = (Integer) map.get("mapViewId");
    }

    public String getFilename() {
        return filename;
    }

    public int getMapViewId() {
        return mapViewId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> m = new HashMap();
        m.put("mapViewId", mapViewId);
        m.put("filename", filename);
        m.put("x", x);
        m.put("y", y);
        return m;
    }
}

