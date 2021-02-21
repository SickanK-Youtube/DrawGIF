
// Create map, enchanted :)
// When that map is placed it will automatically place maps in all itemframes present
// When ANY of the maps are removed everything will come down together and you'll receive the original map


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Wall;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

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
                String filename = image.filename;
                maps.add(new ConfigTypes.MapPiece(Bukkit.getMap(id), filename, x, y));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maps;
    }

    private void placeMaps(BlockFace blockFace, Player player, Location initialLocation) {
        for (ConfigTypes.MapPiece map : this.maps) {
            ItemStack m = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) m.getItemMeta();
            meta.setMapView(map.mapView);
            m.setItemMeta(meta);

            BlockFace heightDirection = getHeightDirection(blockFace, player);
            BlockFace widthDirection = getWidthDirection(blockFace, player);

            Location newLocation = initialLocation.clone();
            int x = 0;
            int y = 0;
            int z = 0;

            if (heightDirection != null && widthDirection != null) {
                switch (heightDirection) {
                    case NORTH:
                        z -= this.imageInfo.height - map.y;
                        break;
                    case SOUTH:
                        z += this.imageInfo.height - map.y;
                        break;
                    case WEST:
                        x -= this.imageInfo.height - map.y;
                        break;
                    case EAST:
                        x += this.imageInfo.height - map.y;
                        break;
                    case UP:
                        y += this.imageInfo.height - map.y;
                        break;
                    default:
                        break;
                }

                switch (widthDirection) {
                    case NORTH:
                        z -= map.x;
                        break;
                    case SOUTH:
                        z += map.x;
                        break;
                    case WEST:
                        x += map.x;
                        break;
                    case EAST:
                        x -= map.x;
                        break;
                    case UP:
                        y += map.x;
                        break;
                    case DOWN:
                        y -= map.x;
                        break;
                    default:
                        break;
                }

                switch (blockFace) {
                    case NORTH:
                        z -= 1.5;
                        break;
                    case SOUTH:
                        z += 1.5;
                        break;
                    case WEST:
                        x -= 1.5;
                        break;
                    case EAST:
                        x += 1.5;
                        break;
                    case UP:
                        y += 1.5;
                        break;
                    case DOWN:
                        y -= 1.5;
                        break;
                    default:
                        break;
                }

                newLocation.add(x, y, z);
                try {
                    ItemFrame frame = player.getWorld().spawn(
                            player.getWorld().getBlockAt(newLocation).getLocation(), ItemFrame.class);
                    frame.setFacingDirection(blockFace);
                    frame.setRotation(facingToRotation(heightDirection, widthDirection));
                    frame.setItem(m);
                } catch (IllegalArgumentException e) {
                    System.out.println(newLocation.toString());
                }

            }
        }

    }

    /*
        west= +x
        east= -x
        south= +z
        north= -z
     */

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        placeMaps(event.getBlockFace(), event.getPlayer(), event.getClickedBlock().getLocation());
    }

    private static Rotation facingToRotation(BlockFace heightDirection, BlockFace widthDirection) {
        switch (heightDirection) {
            case NORTH:
            case SOUTH:
                return widthDirection == BlockFace.WEST ?  Rotation.NONE : Rotation.CLOCKWISE;
            case WEST:
                return Rotation.CLOCKWISE_135;
            case EAST:
                return Rotation.CLOCKWISE_45;
            default:
                return Rotation.NONE;
        }
    }

    private BlockFace getWidthDirection(BlockFace face, Player player) {
        float yaw = (360.0f + player.getLocation().getYaw()) % 360.0f;
        switch (face) {
            case NORTH:
                return BlockFace.EAST;
            case SOUTH:
                return BlockFace.WEST;
            case EAST:
                return BlockFace.NORTH;
            case WEST:
                return BlockFace.SOUTH;
            case UP:
            case DOWN:
                if (yaw >= 45.0 && yaw < 135.0)
                    return BlockFace.NORTH;
                else if (yaw >= 135.0 && yaw < 225.0)
                    return BlockFace.WEST;
                else if (yaw >= 225.0 && yaw < 315.0)
                    return BlockFace.SOUTH;
                else
                    return BlockFace.EAST;
            default:
                return null;
        }
    }

    private BlockFace getHeightDirection(BlockFace face, Player player) {
        float yaw = (360.0f + player.getLocation().getYaw()) % 360.0f;
        switch (face) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                return BlockFace.UP;
            case UP:
                if (yaw >= 45.0 && yaw < 135.0)
                    return BlockFace.WEST;
                else if (yaw >= 135.0 && yaw < 225.0)
                    return BlockFace.NORTH;
                else if (yaw >= 225.0 && yaw < 315.0)
                    return BlockFace.EAST;
                else
                    return BlockFace.SOUTH;
            case DOWN:
                if (yaw >= 45.0 && yaw < 135.0)
                    return BlockFace.EAST;
                else if (yaw >= 135.0 && yaw < 225.0)
                    return BlockFace.SOUTH;
                else if (yaw >= 225.0 && yaw < 315.0)
                    return BlockFace.WEST;
                else
                    return BlockFace.NORTH;
            default:
                return null;
        }
    }
}
