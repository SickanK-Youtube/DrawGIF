import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GifLoaderTask extends BukkitRunnable {
    PlayerJoinEvent event;

    public GifLoaderTask(PlayerJoinEvent event){
        this.event = event;
    }

    @Override
    public void run() {
        MapConfigHandler configHandler = new MapConfigHandler(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));

        PlacedFrame[] frames = configHandler.getAllPlacedFrames();
        Map<String, ArrayList<GifMapRenderer>> gifRenderers = new HashMap<>();
        if (frames != null) {
            for (PlacedFrame frame : frames) {
                ItemFrame itemFrame = locateItemFrame(new Location(event.getPlayer().getWorld(), frame.x, frame.y, frame.z));

                if (itemFrame != null) {
                    if (!gifRenderers.containsKey(frame.id)) {
                        gifRenderers.put(frame.id, new ArrayList<>());
                    }

                    String xy = frame.filename.split("_")[1];
                    int x = Integer.parseInt(xy.split("")[0]);
                    int y = Integer.parseInt(xy.split("")[1]);

                    ArrayList<GifMapRenderer> gifMapRendererList = gifRenderers.get(frame.id);
                    gifMapRendererList.add(new GifMapRenderer(itemFrame, frame.id, x, y));
                    gifRenderers.put(frame.id, gifMapRendererList);
                } else {
                    Map<String, PlacedFrame> framesWithKeys = configHandler.getAllPlacedFramesWithKeys();
                    ArrayList<String> keyToRemove = new ArrayList<>();

                    if (framesWithKeys != null) {
                        for (String key : framesWithKeys.keySet()) {
                            if (framesWithKeys.get(key).filename.equals(frame.filename)) {
                                keyToRemove.add(key);
                            }
                        }

                        if (keyToRemove.toArray().length > 0) {
                            for (String key : keyToRemove) {
                                framesWithKeys.remove(key);
                            }

                            File mapsConfig = new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML);
                            YamlConfiguration config = YamlConfiguration.loadConfiguration(mapsConfig);
                            config.set("frames", framesWithKeys);

                            try {
                                config.save(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            for (String key : gifRenderers.keySet()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new GifMapHandler(gifRenderers.get(key).toArray(new GifMapRenderer[0]), 60);
                    }
                }.runTaskAsynchronously(DrawGIF.getPlugin(DrawGIF.class));
            }
        }
    }

    public ItemFrame locateItemFrame(Location location) {
        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, 0.2, 0.2, 0.2);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof ItemFrame) {
                Location entityLocation = entity.getLocation();
                return (ItemFrame) entity;
            }
        }

        return null;
    }
}
