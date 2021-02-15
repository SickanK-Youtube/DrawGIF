import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;


public class DrawGIF extends JavaPlugin {

    private int[] stringArrayToIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length];

        for(String string : stringArray){
            for(int i=0; i<stringArray.length; i++) {
                intArray[i] = Integer.parseInt(stringArray[i]);
            }
        }

        return intArray;
    }

    public class Status {
        int status;
        String message;

        public Status(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    public class ImageResponse {
        int[] pixelData;
        int density;
        int width;
        int height;
        int widthFrames;
        int heightFrames;

        public ImageResponse(String pixelData, int density, int width, int height, int widthFrames, int heightFrames) {
            String[] pixelStringArray = pixelData.split(",");
            this.pixelData = stringArrayToIntArray(pixelStringArray);
            this.density = density;
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

            ItemStack m = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) m.getItemMeta();
            MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));
            map.setScale(MapView.Scale.NORMAL);

            for (MapRenderer mr : map.getRenderers()) {
                map.removeRenderer(mr);
            }

            map.addRenderer(new MapRenderer(){
                @Override
                public void render(MapView mapview, MapCanvas mapcanvas, Player player){
                    BufferedImage image = new BufferedImage(data.width, data.height, BufferedImage.TYPE_INT_ARGB);
                    WritableRaster raster = (WritableRaster) image.getData();
                    raster.setPixels(0,0,data.width,data.height,data.pixelData);
                    mapcanvas.drawImage(0, 0, image);
                }
            });

            meta.setMapView(map);
            m.setItemMeta(meta);

            for(Player player : Bukkit.getWorlds().get(0).getPlayers()) {
                player.getInventory().addItem(m);
            }

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

