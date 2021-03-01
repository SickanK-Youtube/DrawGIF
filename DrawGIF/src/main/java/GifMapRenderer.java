import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GifMapRenderer {

    private ItemStack map;
    private int delay = 100;

    public GifMapRenderer(ItemStack map, int delay) {
            this.map = map;
            this.delay = delay;

            //render()
    }

    private void render(ItemStack map){

    }

}
