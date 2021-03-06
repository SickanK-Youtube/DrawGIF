import jdk.internal.joptsimple.util.RegexMatcher;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MagicMapHandler implements Listener {
    ImageInfo imageInfo;
    ImagePiece[] imagePieces;
    Player player;
    ItemStack map = null;
    NamespacedKey key;
    Vector<MapPiece> maps;
    YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DrawGIF.DATA_FOLDER + DrawGIF.MAPS_YML));

    public MagicMapHandler(ImageInfo imageInfo, ImagePiece[] imagePieces, Player player) {
        this.imageInfo = imageInfo;
        this.imagePieces = imagePieces;
        this.player = player;
        this.key = new NamespacedKey(DrawGIF.getPlugin(DrawGIF.class), "id");
        if (imageInfo != null && imagePieces != null) {
            this.maps = this.createMaps();
        }
    }

    private Vector<MapPiece> createMaps() {
        if (player != null) {
            ItemStack m = new ItemStack(Material.MAP);
            m.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
            m.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            ItemMeta meta = m.getItemMeta();
            meta.getPersistentDataContainer().set(this.key, PersistentDataType.STRING, imageInfo.id);
            meta.setDisplayName(imageInfo.name);
            m.setItemMeta(meta);
            this.map = m;
            player.getInventory().addItem(m);
        }

        Vector<MapPiece> maps = new Vector<>();

        try {
            for (ImagePiece image : imagePieces) {
                if (image.filename.matches(imageInfo.id + "_[0-9][0-9]_0.png")) {
                    int id = image.mapViewId;
                    int x = image.x;
                    int y = image.y;
                    String filename = image.filename;
                    maps.add(new MapPiece(Bukkit.getMap(id), filename, x, y));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maps;
    }

    private void placeMaps(BlockFace blockFace, Player player, Location initialLocation) {
        ArrayList<GifMapRenderer> gifMapRenderers = new ArrayList<>();
        Map<String, PlacedFrame> frames = new HashMap<>();
        for (MapPiece map : this.maps) {
            BlockFace heightDirection = getHeightDirection(blockFace, player);
            BlockFace widthDirection = getWidthDirection(blockFace, player);

            Location newLocation = initialLocation.clone();
            int x = 0;
            int y = 0;
            int z = 0;

            if (heightDirection != null && widthDirection != null) {
                switch (heightDirection) {
                    case NORTH:
                        z -= this.imageInfo.height - map.y - 1;
                        break;
                    case SOUTH:
                        z += this.imageInfo.height - map.y - 1;
                        break;
                    case WEST:
                        x -= this.imageInfo.height - map.y - 1;
                        break;
                    case EAST:
                        x += this.imageInfo.height - map.y - 1;
                        break;
                    case UP:
                        y += this.imageInfo.height - map.y - 1;
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

                    frames.put(imageInfo.id + "_" + Math.round(newLocation.getX()) + Math.round(newLocation.getY()) + Math.round(newLocation.getZ()), new PlacedFrame(imageInfo.id, map.filename, newLocation.getX(), newLocation.getY(), newLocation.getZ()));

                    if (imageInfo.type.equals("GIF")) {
                        gifMapRenderers.add(new GifMapRenderer(frame, imageInfo.id, map.x, map.y));
                    } else {
                        ItemStack m = new ItemStack(Material.FILLED_MAP);
                        MapMeta meta = (MapMeta) m.getItemMeta();
                        meta.setMapView(map.mapView);
                        m.setItemMeta(meta);
                        frame.setItem(m);
                    }

                } catch (IllegalArgumentException e) {
                }
            }

            ConfigurationSection frameSection = config.getConfigurationSection("frames");
            if (frameSection != null) {
                Map<String, Object> existingFrames = frameSection.getValues(false);

                existingFrames.forEach((name, placedFrame) -> {
                    frames.put(name, (PlacedFrame) placedFrame);
                });

                config.set("frames", frames);
            }

            try {
                config.save(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (imageInfo.type.equals("GIF")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    new GifMapHandler(gifMapRenderers.toArray(new GifMapRenderer[0]), 60);
                }
            }.runTaskAsynchronously(DrawGIF.getPlugin(DrawGIF.class));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();

        ItemStack mainHand = event.getPlayer().getInventory().getItemInMainHand();
        String mainHandId = mainHand.getItemMeta() == null ? null : mainHand.getItemMeta().getPersistentDataContainer().get(this.key, PersistentDataType.STRING);
        ItemStack offHand = event.getPlayer().getInventory().getItemInOffHand();
        String offHandId = offHand.getItemMeta() == null ? null : offHand.getItemMeta().getPersistentDataContainer().get(this.key, PersistentDataType.STRING);

        if (clickedBlock != null && (Objects.equals(mainHandId, imageInfo.id) || Objects.equals(offHandId, imageInfo.id))) {
            event.setCancelled(true);

            if (Objects.equals(mainHandId, imageInfo.id)) {
                mainHand.setAmount(mainHand.getAmount() - 1);
            }

            if (Objects.equals(offHandId, imageInfo.id)) {
                offHand.setAmount(offHand.getAmount() - 1);
            }

            placeMaps(event.getBlockFace(), event.getPlayer(), clickedBlock.getLocation());
        }
    }

    private static Rotation facingToRotation(BlockFace heightDirection, BlockFace widthDirection) {
        switch (heightDirection) {
            case NORTH:
            case SOUTH:
                return widthDirection == BlockFace.WEST ? Rotation.NONE : Rotation.CLOCKWISE;
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
