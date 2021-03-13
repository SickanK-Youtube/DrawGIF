import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.slf4j.Logger;
import org.testng.annotations.Listeners;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DrawGIF extends JavaPlugin implements Listener {
    public static final String MAPS_YML = "maps.yml";
    public static final String IMAGE_FOLDER = "images";
    public static final String DATA_FOLDER = "plugins/DrawGIF/";
    public static final int CONFIG_VERSION = 1;
    private boolean gifsLoaded = false;

    private static ArrayList<String> loadedGifs = new ArrayList<>();

    public static boolean isLoadedGifs(String gif) {
        return loadedGifs.contains(gif);
    }

    public static void pushLoadedGifs(String gif) {
        if (!loadedGifs.contains(gif)) {
            loadedGifs.add(gif);
        }
    }

    static {
        ConfigurationSerialization.registerClass(ImageInfo.class);
        ConfigurationSerialization.registerClass(ImagePiece.class);
        ConfigurationSerialization.registerClass(PlacedFrame.class);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!gifsLoaded) {
            new GifLoaderTask(event).runTask(this);
            this.gifsLoaded = true;
        }
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getDataFolder().mkdir();
        File mapsConfig = new File(DATA_FOLDER, MAPS_YML);
        File imageFolder = new File(DATA_FOLDER, IMAGE_FOLDER);

        try {
            if (mapsConfig.createNewFile()) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(mapsConfig);
                config.set("version", CONFIG_VERSION);
                config.set("maps", new HashMap<String, ImageInfo>());
                config.set("pieces", new HashMap<String, ImagePiece>());
                config.set("reference", new HashMap<String, String>());
                config.set("frames", new HashMap<String, PlacedFrame>());
                config.save(mapsConfig);
            }

            imageFolder.mkdir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                RouteHandler routeHandler = new RouteHandler();
                routeHandler.AddImage(DrawGIF.getPlugin(DrawGIF.class));
                routeHandler.HandleOptions();
            }
        }.runTaskAsynchronously(this);

        MapConfigHandler configHandler = new MapConfigHandler(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));

        new BukkitRunnable() {
            @Override
            public void run() {
                ImagePiece[] imagePieces = configHandler.getAllImagePieces();
                for (ImagePiece image : imagePieces) {
                    @SuppressWarnings("deprecation")
                    MapView mapView = Bukkit.getMap(image.mapViewId);
                    if (mapView != null) {
                        for (MapRenderer mr : mapView.getRenderers()) {
                            mapView.removeRenderer(mr);
                        }

                        File imageFile = new File(DrawGIF.DATA_FOLDER + DrawGIF.IMAGE_FOLDER,
                                image.filename);
                        mapView.addRenderer(new ImageMapRenderer(imageFile));
                    }
                }
            }
        }.runTaskAsynchronously(this);


        new BukkitRunnable() {
            @Override
            public void run() {
                ImageInfo[] imageInfos = configHandler.getAllImageInfo();
                for (ImageInfo image : imageInfos) {
                    ImagePiece[] localImagePiece = configHandler.getImagePieces(image);
                    MagicMapHandler magicMap = new MagicMapHandler(image, localImagePiece, null);
                    Bukkit.getPluginManager().registerEvents(magicMap, DrawGIF.getPlugin(DrawGIF.class));
                }
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandHandler commandHandler = new CommandHandler(sender, command, label, args);
        if (command.getName().equalsIgnoreCase("drawgif")) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
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

