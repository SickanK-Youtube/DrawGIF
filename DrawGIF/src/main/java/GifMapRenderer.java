import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class GifMapRenderer {

    private final ItemFrame frame;
    private final String id;
    private int delay;
    private final int x;
    private final int y;

    private final ArrayList<ItemStack> mapsList = new ArrayList<>();
    private ItemStack[] maps = new ItemStack[0];
    private int index = 0;

    public GifMapRenderer(ItemFrame frame, String id, int x, int y) {
        this.frame = frame;
        this.id = id;
        this.delay = delay;
        this.x = x;
        this.y = y;

        this.getIds();
        this.render();
    }

    private void getIds() {
        MapConfigHandler configHandler = new MapConfigHandler(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));
        ImagePiece[] imagePieces = configHandler.getAllImagePieces();
        ArrayList<Integer> mapIds = new ArrayList<>();

        for (ImagePiece imagePiece : imagePieces) {
            if (imagePiece.getFilename().contains(id + "_" + this.x + this.y)) {
                mapIds.add(imagePiece.mapViewId);
            }
        }

        Collections.sort(mapIds);

        for (int mapId : mapIds) {
            ItemStack map = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapView(Bukkit.getMap(mapId));
            map.setItemMeta(meta);
            mapsList.add(map);
        }

        maps = mapsList.toArray(new ItemStack[0]);

    }

    public void next() {
        index += 1;
        if (index == maps.length) index = 0;
        this.render();
    }

    private void render() {
        frame.setItem(maps[index]);
    }
}
