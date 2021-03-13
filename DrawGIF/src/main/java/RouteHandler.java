import com.google.gson.Gson;
import spark.Spark;

public class RouteHandler {
    private Gson gson = new Gson();

    public void AddImage(DrawGIF plugin){
        Spark.post("/addImage", (req, res) -> {
            JsonTypes.ImageResponse data = gson.fromJson(req.body(), JsonTypes.ImageResponse.class);
            new AddImageTask(data).runTaskAsynchronously(plugin);

            System.out.println("Added new image");
            JsonTypes.Status status = new JsonTypes.Status(200, "Image added");
            return gson.toJson(status);
        });
    }

    public void HandleOptions(){
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

                    JsonTypes.Status status = new JsonTypes.Status(200, "OK");
                    return gson.toJson(status);
                });

        Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }
}
