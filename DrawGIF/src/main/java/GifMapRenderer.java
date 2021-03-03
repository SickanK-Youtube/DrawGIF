import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GifMapRenderer {

    private ItemFrame frame;
    private String id;
    private int delay = 100;
    private Boolean loaded;

    private ArrayList<ItemStack> maps = new ArrayList<>();

    public GifMapRenderer(ItemFrame frame, String id, int i) {
        this.frame = frame;
        this.id = id;
        this.delay = delay;
        this.loaded = DrawGIF.isLoadedGifs(this.id);

        this.getIds();
        this.render();
    }

    private void getIds() {
        MapConfigHandler configHandler = new MapConfigHandler(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));
        ImagePiece[] imagePieces = configHandler.getAllImagePieces();
        ArrayList<Integer> mapIds = new ArrayList<>();

        for (ImagePiece imagePiece : imagePieces) {
            if (imagePiece.getFilename().contains(id)) {
                mapIds.add(imagePiece.mapViewId);
            }
        }

        Collections.sort(mapIds);

        for (int mapId : mapIds) {
            ItemStack map = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapView(Bukkit.getMap(mapId));
            map.setItemMeta(meta);
            maps.add(map);
        }
    }

    private void render() {
        while (true) {
            for (ItemStack map : maps.toArray(new ItemStack[0])) {
                try {
                    if (!this.loaded) {
                        Thread.sleep(1000);
                    } else {
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                frame.setItem(map);
            }

            if (!this.loaded) {
                this.loaded = true;
                DrawGIF.pushLoadedGifs(this.id);
            }
        }
    }
}
