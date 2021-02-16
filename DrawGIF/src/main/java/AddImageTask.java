import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.image.BufferedImage;

public class AddImageTask extends BukkitRunnable {
    DrawGIF.ImageResponse data;

    public AddImageTask(DrawGIF.ImageResponse data) {
        this.data = data;
    }

    @Override
    public void run() {
        for (int wf = 0; wf < data.widthFrames; wf++) {
            for (int hf = 0; hf < data.heightFrames; hf++) {
                int[] pixels = stringArrayToIntArray(data.pixelData.split(","));

                ItemStack m = new ItemStack(Material.FILLED_MAP);
                MapMeta meta = (MapMeta) m.getItemMeta();
                MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));

                for (MapRenderer mr : map.getRenderers()) {
                    map.removeRenderer(mr);
                }

                int finalWf = wf;
                int finalHf = hf;
                map.addRenderer(new MapRenderer() {
                    @Override
                    public void render(MapView mapview, MapCanvas mapcanvas, Player player) {
                        int realWidth = data.width / data.widthFrames;
                        int realHeight = data.height / data.heightFrames;
                        BufferedImage image = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_INT_RGB);
                        int index = realWidth * finalWf +  realHeight * finalHf;
                        for (int j = 0; j < realHeight; j++) {
                            for (int i = 0; i < realWidth; i++) {
                                image.setRGB(j, i, pixels[index]);
                                index += 1;
                            }
                            index += data.width - 1 - realWidth;
                        }
                        mapcanvas.drawImage(0, 0, image);
                    }
                });

                meta.setMapView(map);
                m.setItemMeta(meta);

                for (Player player : Bukkit.getWorlds().get(0).getPlayers()) {
                    player.getInventory().addItem(m);
                }
            }
        }
    }

    public int[] stringArrayToIntArray(String[] stringArray) {
        int[] intArray = new int[stringArray.length / 4];

        for (int i = 0; i < stringArray.length; i += 4) {
            int red = Integer.parseInt(stringArray[i]);
            int green = Integer.parseInt(stringArray[i + 1]);
            int blue = Integer.parseInt(stringArray[i + 2]);
            int alpha = Integer.parseInt(stringArray[i + 3]);
            int color = (alpha << 24) | (red << 16) | (green << 8) | blue;
            intArray[i / 4] = color;
        }

        return intArray;
    }
}

