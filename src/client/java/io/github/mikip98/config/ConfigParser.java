package io.github.mikip98.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import static io.github.mikip98.ShimmerSupportLayerClient.LOGGER;
import static io.github.mikip98.ShimmerSupportLayerClient.MOD_ID;

public class ConfigParser {
    public static void loadConfig() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir,  MOD_ID + ".json");

        if (configFile.exists()) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject configJson = gson.fromJson(reader, JsonObject.class);
                boolean needsUpdating = false;
                if (configJson != null) {
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "deleteUnusedConfigs");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableRadiusColorCompensation");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableBloom");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsByte, "bloomMinRadius");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "auto_alpha");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "auto_block_alpha");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "auto_item_alpha");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "debugAssetsCopy");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "debugAutoSupportGeneration");
                }
                if (needsUpdating) {
                    LOGGER.info("Updating config file to include new values");
                    saveConfig();  // Update the config file to include new values
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static <T> boolean tryLoad(JsonObject configJson, Function<JsonElement, T> getter, String fieldName) {
        try {
            T value = getter.apply(configJson.get(fieldName));
            Config.class.getField(fieldName).set(Config.class, value);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        
        // Create the config directory if it doesn't exist
        if (!configDir.exists()) {
            configDir.mkdir();
        }
        
        File configFile = new File(configDir,  MOD_ID + ".json");
        
        // Create the config file if it doesn't exist
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonObject configJson = new JsonObject();
        configJson.addProperty("deleteUnusedConfigs", Config.deleteUnusedConfigs);
        configJson.addProperty("enableRadiusColorCompensation", Config.enableRadiusColorCompensation);
        configJson.addProperty("enableBloom", Config.enableBloom);
        configJson.addProperty("bloomMinRadius", Config.bloomMinRadius);
        configJson.addProperty("auto_alpha", Config.auto_alpha);
        configJson.addProperty("auto_block_alpha", Config.auto_block_alpha);
        configJson.addProperty("auto_item_alpha", Config.auto_item_alpha);
        configJson.addProperty("debugAssetsCopy", Config.debugAssetsCopy);
        configJson.addProperty("debugAutoSupportGeneration", Config.debugAutoSupportGeneration);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
