package io.github.mikip98.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ConfigScreen {
    public static Screen createScreen(Screen parentScreen) {
        ConfigParser.loadConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setSavingRunnable(ConfigParser::saveConfig)
                .setParentScreen(parentScreen)
                .setTitle(Text.literal("Shimmer Support Layer: Config")
                );

        // Create a root category
        ConfigCategory rootCategory = builder.getOrCreateCategory(Text.literal("General Settings"));

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Delete Unused Configs"), Config.deleteUnusedConfigs)
                .setDefaultValue(false)
                .setTooltip(Text.of("Deletes unused configs on startup.\n(Requires a restart to take effect)"))
                .setSaveConsumer(value -> Config.deleteUnusedConfigs = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Radius Color Compensation"), Config.enableRadiusColorCompensation)
                .setDefaultValue(true)
                .setTooltip(Text.of("Tries to reduce overshoot without losing out on saturation and brightness"))
                .setSaveConsumer(value -> Config.enableRadiusColorCompensation = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Bloom"), Config.enableBloom)
                .setDefaultValue(true)
                .setTooltip(Text.of("Enables bloom effect for light sources"))
                .setSaveConsumer(value -> Config.enableBloom = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Bloom Min Radius"), Config.bloomMinRadius, 0, 255)
                .setDefaultValue(8)
                .setTooltip(Text.of("Minimum radius for bloom effect to be applied"))
                .setSaveConsumer(value -> Config.bloomMinRadius = value.byteValue())
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Universal Auto Alpha"), Config.auto_alpha, 0, 255)
                .setDefaultValue(195)
                .setTooltip(Text.of("Universal alpha for coloured light (both block and corresponding item)\n(It should not be used by the support, it's just a fallback)"))
                .setSaveConsumer(value -> Config.auto_alpha = value.shortValue())
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Auto Block Alpha"), Config.auto_block_alpha, 0, 255)
                .setDefaultValue(170)
                .setTooltip(Text.of("Alpha of the block's coloured light\n(Used by Auto and Semi-Auto Support)"))
                .setSaveConsumer(value -> Config.auto_block_alpha = value.shortValue())
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Auto Item Alpha"), Config.auto_item_alpha, 0, 255)
                .setDefaultValue(228)
                .setTooltip(Text.of("Alpha of the item's coloured light\n(Used by Auto and Semi-Auto Support)"))
                .setSaveConsumer(value -> Config.auto_item_alpha = value.shortValue())
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Debug Assets Copy"), Config.debugAssetsCopy)
                .setDefaultValue(false)
                .setTooltip(Text.of("Enables debug logging for assets coping.\n(May cause a short freeze and huge log files during generation of automatic support)"))
                .setSaveConsumer(value -> Config.debugAssetsCopy = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Debug Auto Support Generation"), Config.debugAutoSupportGeneration)
                .setDefaultValue(false)
                .setTooltip(Text.of("Enables debug logging for automatic support generation.\n(May slightly increase time required for, and log file size during generation of automatic support)"))
                .setSaveConsumer(value -> Config.debugAutoSupportGeneration = value)
                .build()
        );

        // Go through every mod in HandMadeSupport
        // Check if

        // Go through every json in path "{game directory} > config > shimmer"
//        Path gameDirPath = FabricLoader.getInstance().getGameDir();
//        Path folderPath = Paths.get(gameDirPath + "/config/shimmer");
//        if (folderPath.toFile().exists()) {
//            for (File file : Objects.requireNonNull(folderPath.toFile().listFiles())) {
//                if (file.isFile() && file.getName().endsWith(".json")) {
//                    // Create a category for each json
//                    ConfigCategory category = builder.getOrCreateCategory(Text.of(file.getName().replace(".json", "")));
//                }
//            }
//        }

        return builder.build();
    }
}