import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Config structure
// maps:
//   id:
//     name,
//     width,
//     height,
//     images: [
//              filename:
//                  x,
//                  y,
//             ]

public class AddImageTask extends BukkitRunnable {
    JsonTypes.ImageResponse data;

    public AddImageTask(JsonTypes.ImageResponse data) {
        this.data = data;
    }

    @Override
    public void run() {
        int arraySize = data.widthFrames*data.heightFrames;
        int currentIndex = 0;
        HashMap<String, JavaBeans.ImagePosition>[] imagePieces = new HashMap[arraySize];

        for (int wf = 0; wf < data.widthFrames; wf++) {
            for (int hf = 0; hf < data.heightFrames; hf++) {
                int[] pixels = stringArrayToIntArray(data.pixelData.split(","));
                BufferedImage image = drawImage(wf, hf, pixels);

                String filename = data.id + "_" + wf + hf + ".jpg";
                File imageFile = new File(DrawGIF.DATA_FOLDER + DrawGIF.IMAGE_FOLDER,filename);

                try {
                    ImageIO.write(image, "jpg", imageFile);

                    JavaBeans.ImagePosition position = new JavaBeans.ImagePosition();
                    position.setX(wf);
                    position.setY(hf);

                    HashMap<String, JavaBeans.ImagePosition> imagePiece = new HashMap();
                    imagePiece.put(filename, position);
                    imagePieces[currentIndex] = imagePiece;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                currentIndex += 1;
            }
        }

        MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));

        JavaBeans.ImageInfo imageInfo = new JavaBeans.ImageInfo();
        imageInfo.setName(data.name);
        imageInfo.setWidth(data.widthFrames);
        imageInfo.setHeight(data.heightFrames);
        imageInfo.setX(0);
        imageInfo.setY(0);
        imageInfo.setImagePieces(imagePieces);


        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(DrawGIF.DATA_FOLDER + DrawGIF.MAPS_YML));;
        config.set("version", DrawGIF.CONFIG_VERSION);

        ConfigurationSection section = config.getConfigurationSection("maps");
        if(section != null){
            Map<String, Object> maps = section.getValues(false);
            maps.put(data.id, imageInfo);
            config.set("maps", maps);
            System.out.println("Yes exist");

        } else {
            Map <String, JavaBeans.ImageInfo> imageData = new HashMap();
            imageData.put(data.id, imageInfo);
            config.set("maps", imageData);
            System.out.println("No exist");
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

    private BufferedImage drawImage(int widthPosition, int heightPosition, int[] pixels){
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

