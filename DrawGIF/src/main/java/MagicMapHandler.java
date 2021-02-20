
// Create map, enchanted :)
// When that map is placed it will automatically place maps in all itemframes present
// When ANY of the maps are removed everything will come down together and you'll receive the original map


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MagicMapHandler implements Listener {
    ImageInfo imageInfo;
    ImagePiece[] imagePieces;
    Vector<ConfigTypes.MapPiece> maps;

    public MagicMapHandler(ImageInfo imageInfo, ImagePiece[] imagePieces) {
        this.imageInfo = imageInfo;
        this.imagePieces = imagePieces;
        if (imageInfo != null && imagePieces != null) {
            this.maps = this.createMaps();
        }
    }

    private Vector<ConfigTypes.MapPiece> createMaps() {
        ItemStack m = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) m.getItemMeta();
        Vector<ConfigTypes.MapPiece> maps = new Vector();

        try {
            for (ImagePiece image : imagePieces) {
                int id = image.mapViewId;
                int x = image.x;
                int y = image.y;
                String filename = image.toString();
                maps.add(new ConfigTypes.MapPiece(Bukkit.getMap(id), filename, x, y));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (ConfigTypes.MapPiece piece : maps) {
            for (MapRenderer mr : piece.mapView.getRenderers()) {
                piece.mapView.removeRenderer(mr);
            }
            piece.mapView.addRenderer(new MapRenderer() {
                @Override
                public void render(MapView mapview, MapCanvas mapcanvas, Player player) {
                    File imageFile = new File(DrawGIF.DATA_FOLDER + DrawGIF.IMAGE_FOLDER, piece.filename);

                    try {
                        Image image = ImageIO.read(imageFile);
                        mapcanvas.drawImage(0, 0, image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return maps;
    }

    private void placeMaps(Location initialLocation) {
        int height = imageInfo.height;

        for (ConfigTypes.MapPiece map : this.maps) {
            ItemStack m = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) m.getItemMeta();
            meta.setMapView(map.mapView);
            m.setItemMeta(meta);

            Location newLocation = initialLocation.clone();
            newLocation.add(map.x, map.y, 0);

            newLocation.getWorld().spawn(newLocation, ItemFrame.class, itemFrame -> {
                itemFrame.setFacingDirection(BlockFace.NORTH, false);
            });

            ItemFrame frame = (ItemFrame) newLocation.getWorld().getBlockAt(newLocation);
            frame.setItem(m);
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        System.out.println("Block placed");
        placeMaps(event.getBlock().getLocation());
    }
}
