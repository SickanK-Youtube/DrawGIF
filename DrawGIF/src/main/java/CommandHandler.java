import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class CommandHandler {
    CommandSender sender;
    Command command;
    String label;
    String[] args;

    public CommandHandler(CommandSender sender, Command command, String label, String[] args){
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public void get(){
        System.out.println("get");
    }

    public void remove(){
        System.out.println("remove");
    }

    public void list(){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DrawGIF.DATA_FOLDER + DrawGIF.MAPS_YML));;

        ConfigurationSection mapSection = config.getConfigurationSection("maps");
        if(mapSection != null){
            Map<String, Object> maps = mapSection.getValues(true);
            maps.forEach((id, imageInfo) -> {
                ConfigTypes.ImageInfo image = ((ConfigTypes.ImageInfo) imageInfo);
                sender.sendMessage(image.name + " (" + image.width + "x" + image.height + ")");
            });
        } else {
            sender.sendMessage("There doesn't seem to be any images uploaded...");
        }
    }

    public void key(){
        System.out.println("key");
    }

    public void newKey(){
        System.out.println("newKey");
    }

    public void help(){
        System.out.println("help");
    }
}
