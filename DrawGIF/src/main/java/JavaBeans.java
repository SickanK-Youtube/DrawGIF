import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class JavaBeans {
    public static class ImagePosition implements ConfigurationSerializable {
        int x;
        int y;

        public ImagePosition() {}

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> m = new HashMap();
            m.put("x", x);
            m.put("y", y);
            return m;
        }
    }

    public static class ImageInfo implements ConfigurationSerializable {
        String name;
        int width;
        int height;
        int x;
        int y;
        HashMap<String, ImagePosition>[] imagePieces;

        public ImageInfo() {}

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getWidth() {
            return this.width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int x) {
            this.y = y;
        }

        public HashMap<String, ImagePosition>[] getImagePieces() {
            return this.imagePieces;
        }

        public void setImagePieces(HashMap<String, ImagePosition>[] imagePieces) {
            this.imagePieces = imagePieces;
        }

        @Override
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
