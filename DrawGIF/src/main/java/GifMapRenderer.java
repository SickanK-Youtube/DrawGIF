import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GifMapRenderer extends MapRenderer {

    private Image[] images = null;
    private int delay = 100;

    public GifMapRenderer(File[] imageFiles, int delay) {
        try {
            ArrayList<Image> imagesList = new ArrayList<>();

            for (File imageFile : imageFiles) {
                imagesList.add(ImageIO.read(imageFile));
            }

            this.images = imagesList.toArray(new Image[0]);
            this.delay = delay;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MapView mapview, MapCanvas mapcanvas, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (images != null) {
                    for (Image image : images) {
                        mapcanvas.drawImage(0, 0, image);
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.runTaskAsynchronously(DrawGIF.getPlugin(DrawGIF.class));
    }
}
