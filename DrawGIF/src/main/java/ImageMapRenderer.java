import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImageMapRenderer extends MapRenderer {
    private Image image = null;
    private boolean firstRender = true;

    public ImageMapRenderer(File imageFile){
        try {
            this.image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(MapView mapview, MapCanvas mapcanvas, Player player) {
        if(this.image != null && this.firstRender){
            mapcanvas.drawImage(0, 0, this.image);
            this.firstRender = false;
        }
    }

}
