package io.github.mikip98;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.mikip98.automation.AutomationClient;
import io.github.mikip98.automation.modSupport.HandMadeSupport;
import io.github.mikip98.automation.modSupport.SemiAutomaticSupport;
import io.github.mikip98.automation.structures.Color;
import io.github.mikip98.automation.structures.LightSource;
import io.github.mikip98.automation.structures.SupportedMod;
import io.github.mikip98.config.Config;
import io.github.mikip98.config.ConfigParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static io.github.mikip98.automation.Util.bias;
import static io.github.mikip98.automation.Util.clampLight;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class ShimmerSupportLayerClient implements ClientModInitializer {
	public static final String MOD_ID = "shimmer-support-layer";
	public static final String MOD_NAME = "Shimmer Support Layer";
	public static final String MOD_CAMEL = "ShimmerSupportLayer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_CAMEL);

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		LOGGER.info(MOD_NAME + " is initializing!");

		ConfigParser.loadConfig();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(literal("generateShimmerSupport")
						.executes(AutomationClient::dumpling)
				));


		boolean shimmerInstalled = false;

		if (getInstance().isModLoaded("shimmer")) {
			shimmerInstalled = true;
		} else {
			// Go through all mods in the mods folder and check if any of them are Shimmer
			Path gameDirPath = FabricLoader.getInstance().getGameDir();
			File modsFolder = new File(gameDirPath + "/mods");
			File[] modFiles = modsFolder.listFiles();
			if (modFiles != null) {
				for (File modFile : modFiles) {
					if (modFile.getName().contentEquals("shimmer")) {
						shimmerInstalled = true;
						break;
					}
				}
			}
		}

		if (shimmerInstalled) {
			LOGGER.info("Shimmer is loaded!");
			LOGGER.info("Checking for supported mods...");
			for (SupportedMod mod : HandMadeSupport.getSupportedMods()) {
				checkIfModIsLoaded(mod);
			}
			for (SupportedMod mod : SemiAutomaticSupport.getSupportedMods()) {
				checkIfModIsLoaded(mod);
			}
		}
	}

	private static void checkIfModIsLoaded(SupportedMod mod) {
		boolean modFound = false;

		if (getInstance().isModLoaded(mod.modId)) {
			modFound = true;
		} else {
			// Go through all mods in the mods folder and check if any of them are Shimmer
			Path gameDirPath = FabricLoader.getInstance().getGameDir();
			File modsFolder = new File(gameDirPath + "/mods");
			File[] modFiles = modsFolder.listFiles();
			if (modFiles != null) {
				for (File modFile : modFiles) {
					if (modFile.getName().contentEquals(mod.searchPhrase)) {
						modFound = true;
						break;
					}
				}
			}
		}

		if (modFound) {
			LOGGER.info("Adding Shimmer support for: " + mod.modName);
			generateSupportConfig(mod);
		} else {
			if (Config.deleteUnusedConfigs) {
				// Check if config file exists
				Path gameDirPath = FabricLoader.getInstance().getGameDir();
				File configFile = new File(gameDirPath + "/config/shimmer/" + mod.modId + ".json");
				if (configFile.exists()) {
					LOGGER.info("Deleting config for mod: " + mod.modName);
					configFile.delete();
				}
			}
		}
	}

	public static void generateSupportConfig(SupportedMod mod) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Path gameDirPath = FabricLoader.getInstance().getGameDir();
		File configFile = new File(gameDirPath + "/config/shimmer/" + mod.modId + ".json");

		// Create json using gson
		JsonObject configJson = new JsonObject();

		JsonObject colorReference = new JsonObject();
		for (Color color : mod.Colors) {
			JsonObject colorJson = new JsonObject();
			colorJson.addProperty("r", color.red);
			colorJson.addProperty("g", color.green);
			colorJson.addProperty("b", color.blue);
			colorJson.addProperty("a", color.alpha);
			colorReference.add(color.name, colorJson);
		}
		configJson.add("ColorReference", colorReference);

		JsonArray lightBlocks = new JsonArray();
		for (LightSource lightSourceBlock : mod.lightSourceBlocks) {
			JsonObject entry = new JsonObject();
			if (lightSourceBlock.blockId.equals("lava")) {
				entry.addProperty("fluid", mod.modId + ':' + lightSourceBlock.blockId);
			} else {
				entry.addProperty("block", mod.modId + ':' + lightSourceBlock.blockId);
			}
//			entry.addProperty("block", mod.modId + ':' + lightSourceBlock.blockId);
			entry.addProperty("color", '#' + lightSourceBlock.colorName);
			entry.addProperty("radius", clampLight(lightSourceBlock.radius + bias(getColor(lightSourceBlock.colorName, mod.Colors))));
			if (lightSourceBlock.extraStates != null) {
				JsonObject extraStates = new JsonObject();
				for (String extraState : lightSourceBlock.extraStates.split(",")) {
					String[] extraStateSplit = extraState.split("=");
//					LOGGER.info("extraStateSplit: " + extraStateSplit[0] + ", " + extraStateSplit[1]);
					extraStates.addProperty(extraStateSplit[0], extraStateSplit[1]);
				}
//				LOGGER.info("extraStates: " + extraStates);
				entry.add("state", extraStates);
			}
			lightBlocks.add(entry);
		}
		configJson.add("LightBlock", lightBlocks);

		if (mod.lightSourceItems != null) {
			JsonArray lightItems = new JsonArray();
			for (LightSource lightSourceItem : mod.lightSourceItems) {
				JsonObject entry = new JsonObject();
				entry.addProperty("item_id", mod.modId + ':' + lightSourceItem.blockId);
				entry.addProperty("color", '#' + lightSourceItem.colorName);
				entry.addProperty("radius", clampLight(lightSourceItem.radius + bias(getColor(lightSourceItem.colorName, mod.Colors))));
				lightItems.add(entry);
			}
			configJson.add("LightItem", lightItems);
		}

		// if file doesn't exist, then create it, else overwrite it
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Write json to file
		try (FileWriter writer = new FileWriter(configFile)) {
			gson.toJson(configJson, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Color getColor(String colorName, ArrayList<Color> colors) {
		for (Color color : colors) {
			if (color.name.contentEquals(colorName)) {
				return color;
			}
		}
		return null;
	}
}