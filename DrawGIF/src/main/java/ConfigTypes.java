import java.util.HashMap;
import java.util.Map;

public class ConfigTypes {
    public static class ImagePosition {
        int x;
        int y;

        public ImagePosition(int x, int y){
            this.x = x;
            this.y = y;
        }

        public Map<String, Object> serialize() {
            Map<String, Object> m = new HashMap();
            m.put("x", x);
            m.put("y", y);
            return m;
        }
    }

    public static class ImageInfo {
        int mapViewId;
        String name;
        int width;
        int height;
        int x;
        int y;
        HashMap<String, Object>[] imagePieces;

        public ImageInfo(
                int mapViewId,
                String name,
                int width,
                int height,
                int x,
                int y,
                HashMap<String, Object>[] imagePieces
        ) {
            this.mapViewId = mapViewId;
            this.name = name;
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.imagePieces = imagePieces;
        }

        public Map<String, Object> serialize() {
            Map<String, Object> m = new HashMap();
            m.put("name", name);
            m.put("width", width);
            m.put("height", height);
            m.put("x", x);
            m.put("y", y);
            m.put("imagePieces", imagePieces);
            return m;
        }
    }
}
