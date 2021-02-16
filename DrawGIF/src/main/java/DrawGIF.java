import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

public class DrawGIF extends JavaPlugin {

    public class Status {
        int status;
        String message;

        public Status(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public class ImageResponse {
        String pixelData;
        int width;
        int height;
        int widthFrames;
        int heightFrames;

        public ImageResponse(String pixelData, int density, int width, int height, int widthFrames, int heightFrames) {
            this.pixelData = pixelData;
            this.width = width;
            this.height = height;
            this.widthFrames = widthFrames;
            this.heightFrames = heightFrames;
        }
    }

    @Override
    public void onEnable() {
        Gson gson = new Gson();
        Spark.post("/addImage", (req, res) -> {
            ImageResponse data = gson.fromJson(req.body(), ImageResponse.class);
            new AddImageTask(data).runTaskAsynchronously(this);

            System.out.println("Sent");
            Status status = new Status(200, "Image added");
            return gson.toJson(status);
        });

        Spark.options("/*",
                        (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            Status status = new Status(200, "OK");
            return gson.toJson(status);
        });

        Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }
}

