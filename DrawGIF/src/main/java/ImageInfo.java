import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class ImageInfo implements ConfigurationSerializable {
    String id;
    String name;
    int width;
    int height;
    int x;
    int y;
    String[] imagePieces;
    String type;
    int length;
    int delay;

    public ImageInfo(
            String id,
            String name,
            int width,
            int height,
            int x,
            int y,
            String[] imagePieces,
            String type,
            int length,
            int delay
    ) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.imagePieces = imagePieces;
        this.type = type;
        this.length = length;
        this.delay = delay;
    }

    public ImageInfo(Map<?, ?> map) {
        this.id = map.get("id").toString();
        this.name = map.get("name").toString();
        this.width = (Integer) map.get("width");
        this.height = (Integer) map.get("height");
        this.x = (Integer) map.get("x");
        this.y = (Integer) map.get("y");
        this.type = map.get("type").toString();
        this.length = (Integer) map.get("length");
        this.delay = (Integer) map.get("delay");

        Map<String, Object> pieces = (Map<String, Object>) map.get("imagePieces");
        String[] imagePieces = new String[this.width*this.height*this.length];
        int index = 0;
        for(String piece : pieces.keySet()){
            imagePieces[index] = piece;
            index += 1;
        }

        this.imagePieces =  imagePieces;
    }

    public String getId() {
        return id;
    }
    public String getFilename() {
        return name;
    }

    public String[] getImagePieces() {
        return imagePieces;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        m.put("name", name);
        m.put("width", width);
        m.put("height", height);
        m.put("x", x);
        m.put("y", y);
        m.put("type", type);
        m.put("length", length);
        m.put("delay", delay);

        Map<String, String> pieces = new HashMap<>();
        for(String piece : imagePieces) pieces.put(piece, piece);
        m.put("imagePieces", pieces);

        return m;
    }

}
