import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

public class DrawGIF extends JavaPlugin {

    @Override
    public void onEnable() {
        Spark.get("/", (req, res) -> {
            for(Player player : Bukkit.getWorlds().get(0).getPlayers()) {
                player.sendMessage("Hello!");
            }
            return "return val";
        });
    }
}

