import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ImagePiece implements ConfigurationSerializable {
    int mapViewId;
    int nextMapViewId;
    String filename;
    int x;
    int y;

    public ImagePiece(int mapViewId, int nextMapViewId, String filename, int x, int y){
        this.mapViewId = mapViewId;
        this.nextMapViewId = nextMapViewId;
        this.filename = filename;
        this.x = x;
        this.y = y;
    }

    public ImagePiece(Map<?, ?> map) {
        this.filename = map.get("filename").toString();
        this.x = (Integer) map.get("x");
        this.y = (Integer) map.get("y");
        this.mapViewId = (Integer) map.get("mapViewId");
        this.nextMapViewId = (Integer) map.get("nextMapViewId");
    }

    public String getFilename() {
        return filename;
    }

    public int getMapViewId() {
        return mapViewId;
    }

    public int getNextMapViewId() { return nextMapViewId; }

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
        m.put("nextMapViewId", nextMapViewId);
        m.put("filename", filename);
        m.put("x", x);
        m.put("y", y);
        return m;
    }
}

