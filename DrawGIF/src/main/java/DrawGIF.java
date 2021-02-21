import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class DrawGIF extends JavaPlugin {
    public static final String MAPS_YML = "maps.yml";
    public static final String IMAGE_FOLDER = "images";
    public static final String DATA_FOLDER = "plugins/DrawGIF/";
    public static final int CONFIG_VERSION = 1;

    // get request <- Done!
    // make all frames and assign to maps <- Done!
    // save all frames in an yaml file <- Done!
    // Give player one "magic" frame (Complete picture?) and make it place the whole thing when placed in an item frame
    // On remove give back the original picture

    // TODO:
    // - Get and list maps (Command)
    // - Automatic placement
    // - GIFs
    static {
        ConfigurationSerialization.registerClass(ImageInfo.class);
        ConfigurationSerialization.registerClass(ImagePiece.class);
    }


    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        File mapsConfig = new File(DATA_FOLDER, MAPS_YML);
        File imageFolder = new File(DATA_FOLDER, IMAGE_FOLDER);

        try {
            if(mapsConfig.createNewFile()){
                YamlConfiguration config = YamlConfiguration.loadConfiguration(mapsConfig);
                config.set("version", CONFIG_VERSION);
                config.set("maps", new HashMap<String, ImageInfo>());
                config.set("pieces", new HashMap<String, ImagePiece>());
                config.set("reference", new HashMap<String, String>());
                config.save(mapsConfig);
            }

            imageFolder.mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RouteHandler routeHandler = new RouteHandler();
        routeHandler.AddImage(this);
        routeHandler.HandleOptions();

        MapConfigHandler configHandler = new MapConfigHandler(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));
        ImagePiece[] imagePieces = configHandler.getAllImagePieces();

        for (ImagePiece image : imagePieces) {
            @SuppressWarnings("deprecation")
            MapView mapView = Bukkit.getMap(image.mapViewId);

            for (MapRenderer mr : mapView.getRenderers()) {
                mapView.removeRenderer(mr);
            }

            File imageFile = new File(DrawGIF.DATA_FOLDER + DrawGIF.IMAGE_FOLDER,
                    image.filename);
            mapView.addRenderer(new ImageMapRenderer(imageFile));
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandHandler commandHandler = new CommandHandler(sender, command, label, args, this);
        if(command.getName().equalsIgnoreCase("drawgif")){
            switch (args[0].toLowerCase(Locale.ROOT)){
                case "get":
                    commandHandler.get();
                    return true;
                case "remove":
                    commandHandler.remove();
                    return true;
                case "list":
                    commandHandler.list();
                    return true;
                case "key":
                    commandHandler.key();
                    return true;
                case "newKey":
                    commandHandler.newKey();
                    return true;
                case "help":
                    commandHandler.help();
                    return true;
            }
        }
        return false;
    }
}

