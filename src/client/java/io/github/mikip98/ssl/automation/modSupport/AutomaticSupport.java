package io.github.mikip98.ssl.automation.modSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.mikip98.ssl.automation.structures.LightSource;
import io.github.mikip98.ssl.automation.structures.SupportBlock;
import io.github.mikip98.ssl.automation.structures.SupportedMod;
import io.github.mikip98.ssl.config.Config;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

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

import javax.imageio.ImageIO;

import static io.github.mikip98.ssl.ShimmerSupportLayerClient.LOGGER;
import static io.github.mikip98.ssl.ShimmerSupportLayerClient.generateSupportConfig;

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
            LOGGER.info("Generating support for mod '{}'", modId);

            supportedMods.add(new SupportedMod(modId, modId, searchPhrases.getOrDefault(modId, modId), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            temp_java_code.append("// Mod ID: ").append(modId).append('\n');

            temp_java_code.append("ArrayList<Color> ").append(modId).append("Colors = new ArrayList<>();\n");
            temp_java_code.append("ArrayList<LightSource> ").append(modId).append("LightSourceBlocks = new ArrayList<>();\n");
            temp_java_code.append("ArrayList<LightSource> ").append(modId).append("LightSourceItems = new ArrayList<>();\n\n");

            String lastColorName = "";
            for (SupportBlock supportBlock : entry.getValue()) {
                dLog("Generating support for '" + supportBlock.blockId + "' in mod '" + modId + '\'');
                int[] color = getColor(modId, supportBlock.blockId);
                dLog("Color - r: " + color[0] + ", g: " + color[1] + ", b: " + color[2]);
                String colorName = supportBlock.blockId + "_weight_average_color";

                io.github.mikip98.ssl.automation.structures.Color newColor = new io.github.mikip98.ssl.automation.structures.Color(colorName, color[0], color[1], color[2], Config.auto_alpha);  // TODO: fix
                if (!newColor.name.equals(lastColorName)) {
                    supportedMods.get(index).Colors.add(newColor);
                    temp_java_code.append(modId).append("Colors.add(new Color(\"").append(colorName).append("\", ").append(color[0]).append(", ").append(color[1]).append(", ").append(color[2]).append(", Config.auto_alpha));\n");
                }

                boolean noBlockStateRules = false;
                if (supportBlock.blockStateRules != null) {
                    if (supportBlock.blockStateRules.length > 0) {
                        StringBuilder blockStateRulesString = new StringBuilder();
                        for (String blockStateRule : supportBlock.blockStateRules) {
                            blockStateRulesString.append(blockStateRule).append(',');
                        }
                        blockStateRulesString = new StringBuilder(blockStateRulesString.substring(0, blockStateRulesString.length() - 1));
                        supportedMods.get(index).lightSourceBlocks.add(new LightSource(supportBlock.blockId, colorName, supportBlock.radius, blockStateRulesString.toString()));
                        temp_java_code.append(modId).append("LightSourceBlocks.add(new LightSource(\"").append(supportBlock.blockId).append("\", \"").append(colorName).append("\", ").append(supportBlock.radius).append(", \"").append(blockStateRulesString).append("\"));\n");
                    } else noBlockStateRules = true;
                } else noBlockStateRules = true;
                if (noBlockStateRules) {
                    supportedMods.get(index).lightSourceBlocks.add(new LightSource(supportBlock.blockId, colorName, supportBlock.radius));
                    temp_java_code.append(modId).append("LightSourceBlocks.add(new LightSource(\"").append(supportBlock.blockId).append("\", \"").append(colorName).append("\", ").append(supportBlock.radius).append("));\n");
                }

                if (!newColor.name.equals(lastColorName)) {
                    if (checkItem(modId, supportBlock.blockId)) {
                        supportedMods.get(index).lightSourceItems.add(new LightSource(supportBlock.blockId, colorName, supportBlock.radius));
                        temp_java_code.append(modId).append("LightSourceItems.add(new LightSource(\"").append(supportBlock.blockId).append("\", \"").append(colorName).append("\", ").append(supportBlock.radius).append("));\n");
                    }
                }
                lastColorName = newColor.name;
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

        // if the file doesn't exist, then create it, else overwrite it
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

        LOGGER.info("Automatic support generation finished");
    }

    private static boolean checkItem(String modId, String blockId) {
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        Path itemModelPath = Paths.get(gameDirPath + "/config/shimmer/compatibility/temp/assets/" + modId + "/models/item/" + blockId + ".json");
        return itemModelPath.toFile().exists();
    }

    private static int[] getColor(String modId, String blockId) {
        dLog("Getting color for '" + blockId + "' in mod '" + modId + '\'');

//        if (modId.equals("minecraft")) {
//            Set<String> texturesPaths = VanillaBlocksBlockstateAtlas.get(blockId);
//            return extractColorFromTextures(Objects.requireNonNullElseGet(texturesPaths, Set::of));
//        }
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        Path blockstatePath = Paths.get(gameDirPath + "/config/shimmer/compatibility/temp/assets/" + modId + "/blockstates/" + blockId + ".json");

        Set<String> modelPaths = new HashSet<>();

        // Read the blockstate JSON
        try (FileReader reader = new FileReader(String.valueOf(blockstatePath))) {
            Gson gson = new Gson();
            JsonObject blockstateData = gson.fromJson(reader, JsonObject.class);

            dLog("Blockstate JSON: " + blockstateData);

            Set<String> keys = blockstateData.keySet();
            dLog("Keys: " + keys);

            if (keys.contains("variants")) {
                dLog("Blockstate type: 'variants'");
                dLog("Type of variants: " + blockstateData.get("variants").getClass().getName());

                JsonObject variants = blockstateData.getAsJsonObject("variants");

                Set<String> variantKeys = variants.keySet();
                dLog("Keys: " + variantKeys);

                for (String key : variantKeys) {
                    dLog("Key: " + key);
                    String[] keyParts = key.split(",");

                    if (!Arrays.asList(keyParts).contains("lit=false")) {
                        Object modelModule = variants.get(key);

                        if (modelModule instanceof JsonArray) {
                            dLog("Model module is JsonArray");
                            for (Object elementObj : (JsonArray) modelModule) {
                                JsonObject element = (JsonObject) elementObj;
                                modelPaths.add(element.get("model").getAsString());
                            }
                        } else {
                            dLog("Model module is JsonObject");
                            modelPaths.add(((JsonObject) modelModule).get("model").getAsString());
                        }

                    } else {
                        dLog("Key part is 'lit=false'");
                    }
                }

            } else if (keys.contains("multipart")) {
                dLog("Blockstate type: 'multipart'");
                JsonArray multipart = (JsonArray) blockstateData.get("multipart");

                for (Object partObj : multipart) {
                    JsonObject part = (JsonObject) partObj;
                    Object apply = part.get("apply");
                    if (apply instanceof JsonArray) {
                        dLog("Apply is JSONArray");
                        for (Object elementObj : (JsonArray) apply) {
                            JsonObject element = (JsonObject) elementObj;
                            modelPaths.add(element.get("model").getAsString());
                        }
                    } else {
                        dLog("Apply is JSONObject");
                        modelPaths.add(((JsonObject) apply).get("model").getAsString());
                    }
                }

            } else {
                LOGGER.error("Blockstate JSON for '{}' in mod '{}' does not contain 'variants' nor 'multipart'!", blockId, modId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dLog("Model paths: " + modelPaths);

        // Convert minecraft paths to real relative paths
        Set<String> realModelPaths = generateRelativePaths(modelPaths);

        dLog("Real model paths: " + realModelPaths);

        // Read the model JSONs and extract textures paths
        Set<String> texturePaths = new HashSet<>();

        for (String realModelPath : realModelPaths) {
            Path modelPath = Paths.get(gameDirPath + "/config/shimmer/compatibility/temp/" + realModelPath);
            dLog("Model path: " + modelPath);
            try (FileReader reader = new FileReader(String.valueOf(modelPath))) {
                Gson gson = new Gson();
                JsonObject modelData = gson.fromJson(reader, JsonObject.class);

                dLog("Model JSON: " + modelData);

                if (modelData.keySet().contains("textures")) {
                    JsonObject textures = (JsonObject) modelData.get("textures");
                    Set<String> textureKeys = textures.keySet();
                    dLog("Texture keys: " + textureKeys);
                    for (String textureKey : textureKeys) {
                        String texturePath = textures.get(textureKey).getAsString();

                        // Convert minecraft paths to real relative paths while adding them to the texturePaths set
                        if (texturePath.startsWith("block/")) {
                            texturePaths.add("assets/minecraft/textures/" + texturePath + ".png");
                        } else {
                            try {
                                String[] texturePathParts = texturePath.split(":");
                                String modIdPart = texturePathParts[0];
                                String texturePathPart = texturePathParts[1];
                                texturePaths.add("assets/" + modIdPart + "/textures/" + texturePathPart + ".png");
                            } catch (ArrayIndexOutOfBoundsException e) {
                                LOGGER.warn("Unsplittable texture path: {}", texturePath);
                            }
                        }
                    }
                } else {
                    LOGGER.warn("Model JSON for '{}' in mod '{}' does not contain 'textures'!\nSSL currently does not support models inheriting textures from parent model.", blockId, modId);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dLog("Texture paths: " + texturePaths);
        return extractColorFromTextures(texturePaths);
    }

    private static @NotNull Set<String> generateRelativePaths(Set<String> modelPaths) {
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
        return realModelPaths;
    }

    private static int[] extractColorFromTextures(Set<String> texturePaths) {
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
                dLog("New path: " + newPath);
                double[] data = VanillaBlocksTextureAtlas.get(newPath);
                r_sum += data[0];
                g_sum += data[1];
                b_sum += data[2];
                total_weight += data[3];

            } else {
                try {
                    String relativeTexturePath = relativeBeginningPath + texturePath;
                    dLog("Texture path: " + relativeTexturePath);
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
//            float h = hsb[0];
            float s = hsb[1];
            float v = hsb[2];

            float max_chan_value = Math.max(r, Math.max(g, b));

            double weight = Math.sqrt(r_f*r_f + g_f*g_f + b_f*b_f) * Math.pow(max_chan_value, 2) * (0.25 + (s * 0.75)) * v * a_f;

            r_sum += r_f * weight;
            g_sum += g_f * weight;
            b_sum += b_f * weight;
            total_weight += weight;
        }

        if (total_weight == 0) {
            LOGGER.warn("Total weight is 0 for texture paths: {}", texturePaths);
        }

        return new int[] {
                (int) Math.round(r_sum * 255 / total_weight),
                (int) Math.round(g_sum * 255 / total_weight),
                (int) Math.round(b_sum * 255 / total_weight)
        };
    }

    private static void dLog(String message) {
        if (Config.debugAutoSupportGeneration) LOGGER.info(message);
    }
}