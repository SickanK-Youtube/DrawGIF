public class JsonTypes {
    public static class ImageResponse {
        String pixelData;
        String name;
        String id;
        int width;
        int height;
        int widthFrames;
        int heightFrames;

        public ImageResponse(String pixelData, String name, String id, int density, int width, int height, int widthFrames, int heightFrames) {
            this.id = id;
            this.name = name;
            this.pixelData = pixelData;
            this.width = width;
            this.height = height;
            this.widthFrames = widthFrames;
            this.heightFrames = heightFrames;
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
