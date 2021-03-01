public class JsonTypes {
    public static class ImageResponse {
        String pixelData;
        String name;
        String id;
        String type;
        int width;
        int height;
        int widthFrames;
        int heightFrames;
        int length;
        int delay;

        public ImageResponse(String pixelData, String name, String id, String type, int width, int height, int widthFrames, int heightFrames, int length, int delay) {
            this.id = id;
            this.name = name;
            this.pixelData = pixelData;
            this.width = width;
            this.height = height;
            this.widthFrames = widthFrames;
            this.heightFrames = heightFrames;
            this.type = type;
            this.length = length;
            this.delay = delay;
        }
    }

    public static class Status {
        int status;
        String message;

        public Status(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

}
