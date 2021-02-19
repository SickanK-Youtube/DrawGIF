import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class DrawGIF extends JavaPlugin {
    public static final String MAPS_YML = "maps.yml";
    public static final String IMAGE_FOLDER = "images";
    public static final String DATA_FOLDER = "plugins/DrawGIF/";
    public static final int CONFIG_VERSION = 1;

    // get request
    // make all frames and assign to maps
    // save all frames in an yaml file
    // Give player one "magic" frame (Complete picture?) and make it place the whole thing when placed in an item frame
    // On remove give back the original picture

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        File mapsConfig = new File(DATA_FOLDER, "maps.yml");
        File imageFolder = new File(DATA_FOLDER, "images");

        try {
            if(mapsConfig.createNewFile()){
               // add default config
            }

            if(imageFolder.mkdir()){
                // add default config
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Routes routes = new Routes();
        routes.AddImage(this);
        routes.HandleOptions();
    }
}

