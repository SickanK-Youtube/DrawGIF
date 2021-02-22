import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class CommandHandler {
    CommandSender sender;
    Command command;
    String label;
    String[] args;

    public CommandHandler(CommandSender sender, Command command, String label, String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    public void get() {
        MapConfigHandler configHandler = new MapConfigHandler(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));

        if (args.length < 2) {
            sender.sendMessage("You must supply an Image!\n/drawgif get [image]");
            return;
        }

        Map[] matchingReferences = configHandler.getReference(args[1]);

        if(matchingReferences == null || matchingReferences.length == 0){
            sender.sendMessage("There seems to be an error... Have you supplied the correct Image?");
            return;
        }

        System.out.println(matchingReferences[0]);
        ImageInfo image = configHandler.getImageInfo((String) matchingReferences[0].get("id"));

        if(image == null){
            sender.sendMessage("There seems to be an error. (47)");
            return;
        }

        ImagePiece[] imagePieces = configHandler.getImagePieces(image);

        if(imagePieces == null){
            sender.sendMessage("There seems to be an error... Have you supplied the correct Image?");
            return;
        }

        MagicMapHandler magicMap = new MagicMapHandler(image, imagePieces, sender.getServer().getPlayer(sender.getName()));
        Bukkit.getPluginManager().registerEvents(magicMap, DrawGIF.getPlugin(DrawGIF.class));
    }

    public void remove() {
        System.out.println("remove");
    }

    public void list() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));

        ConfigurationSection mapSection = config.getConfigurationSection("maps");
        if (mapSection != null) {
            mapSection.getValues(false).forEach((id, img) -> {
                ImageInfo image = (ImageInfo) img;
                sender.sendMessage(image.name + " (" + image.width + "x" + image.height + ")");
            });

        } else {
            sender.sendMessage("There doesn't seem to be any images uploaded...");
        }
    }

    public void key() {
        System.out.println("key");
    }

    public void newKey() {
        System.out.println("newKey");
    }

    public void help() {
        System.out.println("help");
    }
}
