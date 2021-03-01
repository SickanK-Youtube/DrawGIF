import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageMapRenderer extends MapRenderer {
    private Image image = null;
    private boolean firstRender = true;

    public ImageMapRenderer(File imageFile) {
        try {
            this.image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MapView mapview, MapCanvas mapcanvas, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (image != null && firstRender) {
                    mapcanvas.drawImage(0, 0, image);
                    firstRender = false;
                }
            }
        }.runTaskAsynchronously(DrawGIF.getPlugin(DrawGIF.class));
    }

}
