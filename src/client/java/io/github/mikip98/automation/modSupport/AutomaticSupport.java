package io.github.mikip98.automation.modSupport;

import io.github.mikip98.automation.structures.LightSource;
import io.github.mikip98.automation.structures.SupportBlock;
import io.github.mikip98.automation.structures.SupportedMod;
import io.github.mikip98.config.Config;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;

import static io.github.mikip98.ShimmerSupportLayerClient.LOGGER;
import static io.github.mikip98.ShimmerSupportLayerClient.generateSupportConfig;

public class AutomaticSupport {
    // This Class contains the scripts that automatically generate support for all mods.

    private final static Map<String, String> searchPhrases = Map.of(
            "minecraft", "jar",
            "betternether", "better-nether",
            "betterend", "better-end"
    );

    public static void generateAutoSupport(Map<String, ArrayList<SupportBlock>> activeBlocksByMod) {
        ArrayList<SupportedMod> supportedMods = new ArrayList<>();
        int index = 0;
        StringBuilder temp_java_code = new StringBuilder();

        for (Map.Entry<String, ArrayList<SupportBlock>> entry : activeBlocksByMod.entrySet()) {
            String modId = entry.getKey();

            supportedMods.add(new SupportedMod(modId, modId, searchPhrases.getOrDefault(modId, modId), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            temp_java_code.append("// Mod ID: ").append(modId).append('\n');

            temp_java_code.append("ArrayList<Color> ").append(modId).append("Colors = new ArrayList<>();\n");
            temp_java_code.append("ArrayList<LightSource> ").append(modId).append("LightSourceBlocks = new ArrayList<>();\n");
            temp_java_code.append("ArrayList<LightSource> ").append(modId).append("LightSourceItems = new ArrayList<>();\n\n");

            for (SupportBlock supportBlock : entry.getValue()) {
                LOGGER.info("Generating support for '" + supportBlock.blockId + "' in mod '" + modId + '\'');
                int[] color = getColor(modId, supportBlock.blockId);
                LOGGER.info("Color - r: " + color[0] + ", g: " + color[1] + ", b: " + color[2]);
                String colorName = supportBlock.blockId + "_weight_average_color";

                supportedMods.get(index).Colors.add(new io.github.mikip98.automation.structures.Color(colorName, color[0], color[1], color[2], Config.auto_block_alpha));
                supportedMods.get(index).lightSourceBlocks.add(new LightSource(supportBlock.blockId, colorName, supportBlock.radius));

                temp_java_code.append(modId).append("Colors.add(new Color(\"").append(colorName).append("\", ").append(color[0]).append(", ").append(color[1]).append(", ").append(color[2]).append(", Config.auto_block_alpha));\n");
                temp_java_code.append(modId).append("LightSourceBlocks.add(new LightSource(\"").append(supportBlock.blockId).append("\", \"").append(colorName).append("\", ").append(supportBlock.radius).append("));\n");
            }

            String searchPhrase = searchPhrases.getOrDefault(modId, modId);
            temp_java_code.append("supportedMods.add(new SupportedMod(\"").append(modId).append("\", \"").append(modId).append("\", \"").append(searchPhrase).append("\", ").append(modId).append("Colors, ").append(modId).append("LightSourceBlocks, ").append(modId).append("LightSourceItems));\n\n");

            ++index;
        }

        for (SupportedMod supportedMod : supportedMods) {
            generateSupportConfig(supportedMod);
        }

        // Dump the generated code to '{game directory} > config > shimmer > compatibility > java_code.txt' file
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        Path javaCodePath = Paths.get(gameDirPath + "/config/shimmer/compatibility/java_code.txt");
        File javaCodeFile = new File(String.valueOf(javaCodePath));

        // if file doesn't exist, then create it, else overwrite it
        if (!javaCodeFile.exists()) {
            try {
                javaCodeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write csv to the file
        try (FileWriter writer = new FileWriter(javaCodeFile)) {
            writer.write(temp_java_code.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] getColor(String modId, String blockId) {
        LOGGER.info("Getting color for '" + blockId + "' in mod '" + modId + '\'');

        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        Path blockstatePath = Paths.get(gameDirPath + "/config/shimmer/compatibility/temp/assets/" + modId + "/blockstates/" + blockId + ".json");

        Set<String> modelPaths = new HashSet<>();

        // Read the blockstate JSON
        try (FileReader reader = new FileReader(String.valueOf(blockstatePath))) {
            JSONObject blockstateData = new JSONObject(new JSONTokener(reader));
            LOGGER.info("Blockstate JSON: " + blockstateData);

            Set<String> keys = blockstateData.keySet();
            LOGGER.info("Keys: " + keys);

            if (keys.contains("variants")) {
                LOGGER.info("Blockstate type: 'variants'");
                LOGGER.info("Type of variants: " + blockstateData.get("variants").getClass().getName());
                JSONObject variants = (JSONObject) blockstateData.get("variants");
                Set<String> variantsKeys = variants.keySet();
                LOGGER.info("Keys: " + variantsKeys);
                for (String key: variantsKeys) {
                    LOGGER.info("Key: " + key);
                    String[] keyParts = key.split(",");
                    boolean addModelPath = true;
                    for (String keyPart : keyParts) {
//                        LOGGER.info("Key part: " + keyPart);
                        if (keyPart.equals("lit=false")) {
                            LOGGER.info("Key part is 'lit=false'");
                            addModelPath = false;
                        }
                    }
                    if (addModelPath) {
                        Object modelModule = variants.get(key);
                        if (modelModule instanceof JSONArray) {
                            LOGGER.info("Model module is JSONArray");
                            for (Object elementObj : (JSONArray) modelModule) {
                                JSONObject element = (JSONObject) elementObj;
                                modelPaths.add((String) element.get("model"));
                            }
                        } else {
                            LOGGER.info("Model module is JSONObject");
                            modelPaths.add((String) ((JSONObject) modelModule).get("model"));
                        }
                    }
                }

            } else if (keys.contains("multipart")) {
                LOGGER.info("Blockstate type: 'multipart'");
                JSONArray multipart = (JSONArray) blockstateData.get("multipart");
                for (Object partObj : multipart) {
                    JSONObject part = (JSONObject) partObj;
                    Object apply = part.get("apply");
                    if (apply instanceof JSONArray) {
                        LOGGER.info("Apply is JSONArray");
                        for (Object elementObj : (JSONArray) apply) {
                            JSONObject element = (JSONObject) elementObj;
                            modelPaths.add((String) element.get("model"));
                        }
                    } else {
                        LOGGER.info("Apply is JSONObject");
                        modelPaths.add((String) ((JSONObject) apply).get("model"));
                    }
                }
            } else {
                LOGGER.error("Blockstate JSON for '" + blockId + "' in mod '" + modId + "' does not contain 'variants' nor 'multipart'!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("Model paths: " + modelPaths);

        // Convert minecraft paths to real relative paths
        Set<String> realModelPaths = new HashSet<>();
        for (String modelPath : modelPaths) {
            if (modelPath.startsWith("block/")) {
                realModelPaths.add("assets/minecraft/models/" + modelPath + ".json");
            } else {
                String[] modelPathParts = modelPath.split(":");
                String modIdPart = modelPathParts[0];
                String modelPathPart = modelPathParts[1];
                realModelPaths.add("assets/" + modIdPart + "/models/" + modelPathPart + ".json");
            }
        }

        LOGGER.info("Real model paths: " + realModelPaths);

        // Read the model JSONs and extract textures paths
        Set<String> texturePaths = new HashSet<>();

        for (String realModelPath : realModelPaths) {
            Path modelPath = Paths.get(gameDirPath + "/config/shimmer/compatibility/temp/" + realModelPath);
            LOGGER.info("Model path: " + modelPath);
            try (FileReader reader = new FileReader(String.valueOf(modelPath))) {
                JSONObject modelData = new JSONObject(new JSONTokener(reader));
                LOGGER.info("Model JSON: " + modelData);
                if (modelData.keySet().contains("textures")) {
                    JSONObject textures = (JSONObject) modelData.get("textures");
                    Set<String> textureKeys = textures.keySet();
                    LOGGER.info("Texture keys: " + textureKeys);
                    for (String textureKey : textureKeys) {
                        String texturePath = (String) textures.get(textureKey);

                        // Convert minecraft paths to real relative paths while adding them to the texturePaths set
                        if (texturePath.startsWith("block/")) {
                            texturePaths.add("assets/minecraft/textures/" + texturePath + ".png");
                        } else {
                            String[] texturePathParts = texturePath.split(":");
                            String modIdPart = texturePathParts[0];
                            String texturePathPart = texturePathParts[1];
                            texturePaths.add("assets/" + modIdPart + "/textures/" + texturePathPart + ".png");
                        }
                    }
                } else {
                    LOGGER.warn("Model JSON for '" + blockId + "' in mod '" + modId + "' does not contain 'textures'!\n" +
                            "SSL currently does not support models inheriting textures from parent model.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info("Texture paths: " + texturePaths);
        return extructColorFromTextures(texturePaths);
    }

    private static int[] extructColorFromTextures(Set<String> texturePaths) {
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        String relativeBeginningPath = gameDirPath + "/config/shimmer/compatibility/temp/";

        List<int[]> pixels = new ArrayList<>();

        double r_sum = 0;
        double g_sum = 0;
        double b_sum = 0;
        double total_weight = 0;

        for (String texturePath : texturePaths) {
            if (texturePath.startsWith("assets/minecraft")) {
//                String newPath = texturePath.replaceFirst("assets/minecraft/textures/", "");
                String newPath = texturePath.substring(26);
                LOGGER.info("New path: " + newPath);
                double[] data = VanillaBlocksTextureDictionary.get(newPath);
                r_sum += data[0];
                g_sum += data[1];
                b_sum += data[2];
                total_weight += data[3];

            } else {
                try {
                    String relativeTexturePath = relativeBeginningPath + texturePath;
                    LOGGER.info("Texture path: " + relativeTexturePath);
                    BufferedImage image = ImageIO.read(new File(relativeTexturePath));
                    int width = image.getWidth();
                    int height = image.getHeight();
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++){
                            int[] pixel = new int[4];

                            int color = image.getRGB(x, y);
                            pixel[0] += (color >> 16) & 0xFF;
                            pixel[1] += (color >> 8) & 0xFF;
                            pixel[2] += color & 0xFF;
                            pixel[3] += (color >> 24) & 0xFF;

                            pixels.add(pixel);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (int[] pixel : pixels) {
            int r = pixel[0];
            int g = pixel[1];
            int b = pixel[2];
            int a = pixel[3];
            float r_f = ((float) r) / 255;
            float g_f = ((float) g) / 255;
            float b_f = ((float) b) / 255;
            float a_f = ((float) a) / 255;

            float[] hsb = Color.RGBtoHSB(r, g, b, null);
            float h = hsb[0];
            float s = hsb[1];
            float v = hsb[2];

            float max_chan_value = Math.max(r, Math.max(g, b));

            double weight = Math.sqrt(r_f*r_f + g_f*g_f + b_f*b_f) * Math.pow(max_chan_value, 2) * (0.5 + (s * 0.5)) * v * a_f;
//            double weight = Math.pow((r_f + g_f + b_f) / 3, 2) * (1 - Math.abs(0.5 - v)) * (1 - Math.abs(0.5 - s)) * max_chan_value;

            r_sum += r * weight;
            g_sum += g * weight;
            b_sum += b * weight;
            total_weight += weight;
        }

        if (total_weight == 0) {
            LOGGER.warn("Total weight is 0 for texture paths: " + texturePaths);
        }

        int[] avrageColor = {
                (int) Math.round(r_sum / total_weight),
                (int) Math.round(g_sum / total_weight),
                (int) Math.round(b_sum / total_weight)
        };
        return avrageColor;
    }
}