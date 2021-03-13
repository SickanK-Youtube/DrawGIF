import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.awt.*;
import java.io.File;
import java.util.*;

public class MapConfigHandler {
    YamlConfiguration config;

    public MapConfigHandler(File configFile) {
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public ImageInfo getImageInfo(String id) {
        ConfigurationSection mapSection = config.getConfigurationSection("maps");
        if (mapSection == null || id == null) {
            return null;
        }
        return (ImageInfo) mapSection.get(id);
    }

    public ImageInfo[] getAllImageInfo() {
        ConfigurationSection mapSection = config.getConfigurationSection("maps");
        if (mapSection == null) {
            return null;
        }

        Vector<ImageInfo> imageInfos = new Vector<>();

        for(String key : mapSection.getKeys(false)){
            imageInfos.add((ImageInfo) mapSection.get(key));
        };

        return imageInfos.toArray(new ImageInfo[0]);
    }

    public ImagePiece[] getImagePieces(ImageInfo imageInfo) {
        ConfigurationSection pieceSection = this.config.getConfigurationSection("pieces");

        if (pieceSection == null) {
            return null;
        }

        ImagePiece[] imagePieces = new ImagePiece[imageInfo.height * imageInfo.width * imageInfo.length];

        int index = 0;
        for (String key : imageInfo.imagePieces) {
            imagePieces[index] = (ImagePiece) pieceSection.get(key);
            index += 1;
        }

        return imagePieces;
    }

    public ImagePiece[] getAllImagePieces() {
        ConfigurationSection pieceSection = this.config.getConfigurationSection("pieces");

        if (pieceSection == null) {
            return null;
        }

        Vector<ImagePiece> imagePieces = new Vector<>();

        for(String key : pieceSection.getKeys(false)){
            imagePieces.add((ImagePiece) pieceSection.get(key));
        };

        return imagePieces.toArray(new ImagePiece[0]);
    }

    public PlacedFrame[] getAllPlacedFrames() {
        ConfigurationSection frameSection = this.config.getConfigurationSection("frames");

        if (frameSection == null) {
            return null;
        }

        Vector<PlacedFrame> frames = new Vector<>();

        for(String key : frameSection.getKeys(false)){
            frames.add((PlacedFrame) frameSection.get(key));
        };

        return frames.toArray(new PlacedFrame[0]);
    }

    public Map<String, PlacedFrame> getAllPlacedFramesWithKeys() {
        ConfigurationSection frameSection = this.config.getConfigurationSection("frames");
        System.out.println(frameSection);

        if (frameSection == null) {
            return null;
        }

        Map<String, PlacedFrame> frames = new HashMap<>();

        for(String key : frameSection.getKeys(false)){
            frames.put(key, (PlacedFrame) frameSection.get(key));
        };

        return frames;
    }

    public HashMap[] getReference(String name) {
        ConfigurationSection referenceSection = config.getConfigurationSection("reference");
        if (referenceSection == null || name == null) {
            return null;
        }

        ArrayList<HashMap<String, String>> matchingKeys = new ArrayList<>();
        for (String key : referenceSection.getKeys(false)) {
            if (key.equalsIgnoreCase(name)) {
                HashMap<String, String> map = new HashMap();
                map.put("name", key);
                map.put("id", referenceSection.get(key).toString());
                matchingKeys.add(map);
            }
        }

        return matchingKeys.toArray(new HashMap[0]);
    }

}
