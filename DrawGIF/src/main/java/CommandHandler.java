import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CommandHandler {
    CommandSender sender;
    Command command;
    String label;
    String[] args;
    DrawGIF drawGIF;

    public CommandHandler(CommandSender sender, Command command, String label, String[] args, DrawGIF drawGIF) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        this.drawGIF = drawGIF;
    }

    public void get() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));

        ConfigurationSection mapSection = config.getConfigurationSection("maps");
        if (mapSection != null) {
            mapSection.getValues(false).forEach((id, img) -> {
                ImageInfo image = (ImageInfo) img;

                ConfigurationSection pieceSection = config.getConfigurationSection("pieces");
                if (pieceSection != null) {
                    for (Object piece : Arrays.stream(image.imagePieces).toArray()) {
                        ImagePiece[] imagePieces = new ImagePiece[image.height * image.width];
                        String strPiece = piece.toString();
                        Set<String> keys = pieceSection.getKeys(false);

                        int index = 0;
                        for (String key : keys) {
                            imagePieces[index] = (ImagePiece) pieceSection.get(key);
                            index += 1;
                        }

                        System.out.println(image.toString());
                        MagicMapHandler magicMap = new MagicMapHandler(image, imagePieces);
                        Bukkit.getPluginManager().registerEvents(magicMap, drawGIF);
                    }
                }
            });
            } else {
                    sender.sendMessage("There doesn't seem to be any images uploaded...");
                }
            }

            public void remove () {
                System.out.println("remove");
            }

            public void list () {
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

            public void key () {
                System.out.println("key");
            }

            public void newKey () {
                System.out.println("newKey");
            }

            public void help () {
                System.out.println("help");
            }
        }
