import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddImageTask extends BukkitRunnable {
    JsonTypes.ImageResponse data;

    public AddImageTask(JsonTypes.ImageResponse data) {
        this.data = data;
    }

    @Override
    public void run() {
        int arraySize = data.widthFrames * data.heightFrames;
        int currentIndex = 0;
        Map<String, ImagePiece> imagePieces = new HashMap<>();
        String[] imagePieceNames = new String[arraySize];

        // Create all maps
        for (int wf = 0; wf < data.widthFrames; wf++) {
            for (int hf = 0; hf < data.heightFrames; hf++) {
                int[] pixels = stringArrayToIntArray(data.pixelData.split(","));
                BufferedImage image = drawImage(wf, hf, pixels);

                String filename = data.id + "_" + wf + hf + ".jpg";
                File imageFile = new File(DrawGIF.DATA_FOLDER + DrawGIF.IMAGE_FOLDER, filename);

                try {
                    ImageIO.write(image, "jpg", imageFile);
                    MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
                    mapView.addRenderer(new ImageMapRenderer(imageFile));

                    String name = data.id + "_" + wf + hf;
                    imagePieces.put(name, new ImagePiece(mapView.getId(), filename, wf, hf));
                    imagePieceNames[currentIndex] = name;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                currentIndex += 1;
            }
        }

        // Add maps to configuration file
        ImageInfo imageInfo = new ImageInfo(data.name, data.widthFrames, data.heightFrames, 0, 0, imagePieceNames);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DrawGIF.DATA_FOLDER + DrawGIF.MAPS_YML));

        ConfigurationSection mapsSection = config.getConfigurationSection("maps");
        if (mapsSection != null) {
            Map<String, Object> maps = mapsSection.getValues(false);
            maps.put(data.id, imageInfo);
            config.set("maps", maps);

        } else {
            Map<String, ImageInfo> imageData = new HashMap();
            imageData.put(data.id, imageInfo);
            config.set("maps", imageData);
        }

        ConfigurationSection pieceSection = config.getConfigurationSection("pieces");
        Map<String, ImagePiece> pieces = new HashMap<>();

        if (pieceSection != null) {
            Map<String, Object> list = pieceSection.getValues(false);
            list.forEach((id, imgPiece) -> {
                pieces.put(id, (ImagePiece) imgPiece);
            });
        }
        imagePieces.forEach(pieces::put);
        config.set("pieces", pieces);

        ConfigurationSection mapReferenceSection = config.getConfigurationSection("reference");
        if (mapReferenceSection != null) {
            Map<String, Object> list = mapReferenceSection.getValues(false);

            Map<String, String> mapReference = new HashMap();
            list.forEach((name, id) -> {
                mapReference.put(name, id.toString());
            });
            mapReference.put(data.name, data.id);

            config.set("reference", mapReference);

        } else {
            Map<String, String> mapReference = new HashMap();
            mapReference.put(data.name, data.id);
            config.set("reference", mapReference);
        }

        try {
            config.save(new File(DrawGIF.DATA_FOLDER, DrawGIF.MAPS_YML));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] stringArrayToIntArray(String[] stringArray) {
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

    private BufferedImage drawImage(int widthPosition, int heightPosition, int[] pixels) {
        int realWidth = data.width / data.widthFrames;
        int realHeight = data.height / data.heightFrames;
        BufferedImage image = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_INT_RGB);
        int index = realWidth * widthPosition + data.width * realHeight * heightPosition;
        for (int j = 0; j < realHeight; j++) {
            for (int i = 0; i < realWidth; i++) {
                image.setRGB(i, j, pixels[index]);
                index += 1;
            }
            index += data.width - realWidth;
        }

        return image;
    }

}

