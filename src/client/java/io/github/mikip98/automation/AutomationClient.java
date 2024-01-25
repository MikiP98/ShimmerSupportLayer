package io.github.mikip98.automation;

import com.mojang.brigadier.context.CommandContext;
import io.github.mikip98.automation.modSupport.AutomaticSupport;
import io.github.mikip98.automation.modSupport.SupportedMods;
import io.github.mikip98.automation.structures.SupportBlock;
import io.github.mikip98.automation.structures.SupportedMod;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.mikip98.ShimmerSupportLayerClient.LOGGER;

public class AutomationClient {
    @SuppressWarnings("unused")
    public static int dumpling(CommandContext<FabricClientCommandSource> context) {
//        copyModAssets();  // TODO: Uncomment this line when the mod is released

        LOGGER.info("Started the command...");
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Started the command..."), false);
        }
        LOGGER.info("Started the csv dump generation...");
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Started the csv dump generation..."), false);
        }

        for (Block block : Registries.BLOCK) {
            BlockState blockState = block.getDefaultState();

            BooleanProperty litProperty = Properties.LIT;
            boolean hasLitProperty = blockState.getProperties().contains(litProperty);
            boolean isLit = false;
            // Check if the block has the "lit" property
            if (hasLitProperty) {
                isLit = blockState.get(litProperty);
            }

            generateEntry(blockState);
            if (hasLitProperty) {
                blockState = blockState.with(Properties.LIT, !isLit);
                generateEntry(blockState);
            }
        }

        // Create dump file in the game directory > config > shimmer > compatibility
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        File dumpCSVFile = new File(gameDirPath + "/config/shimmer/compatibility/light_blocks_plus_radius.csv");

        //  if the directory doesn't exist, then create it
        if (!dumpCSVFile.getParentFile().exists()) {
            dumpCSVFile.getParentFile().mkdirs();
        }

        // if the file doesn't exist, then create it, else overwrite it
        if (!dumpCSVFile.exists()) {
            try {
                dumpCSVFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write csv to the file
        try (FileWriter writer = new FileWriter(dumpCSVFile)) {
            writer.write(ActiveBlocks.staticToString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("Ended the csv dump generation...");
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Ended the csv dump generation..."), false);
        }
        LOGGER.info("Started generating full auto support...");
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Started generating full auto support..."), false);
        }

        Map<String, ArrayList<SupportBlock>> unsupportedActiveBlocksByMod = new HashMap<>();
        // Go through every mod in ActiveBlocks and check if it's supported
        // If it's not supported, add it to unsupportedActiveBlocksByMod
        for (Map.Entry<String, ArrayList<SupportBlock>> entry : ActiveBlocks.getActiveBlocksByMod().entrySet()) {
            String modId = entry.getKey();
            ArrayList<SupportBlock> supportBlocks = entry.getValue();
            boolean isSupported = false;
            for (SupportedMod supportedMod : SupportedMods.getSupportedMods()) {
                if (modId.equals(supportedMod.modId)) {
                    isSupported = true;
                    break;
                }
            }
            if (!isSupported) {
                LOGGER.info("Mod '" + modId + "' is not supported");
                unsupportedActiveBlocksByMod.put(modId, supportBlocks);
            }
        }
        if (!unsupportedActiveBlocksByMod.isEmpty()) {
            copyModAssets();
            AutomaticSupport.generateAutoSupport(unsupportedActiveBlocksByMod);
            deleteModAssets();
        }

        LOGGER.info("Ended generating full auto support...");
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Ended generating full auto support..."), false);
        }
        LOGGER.info("Ended the command...");
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Ended the command..."), false);
        }

        return 1;
    }

    private static void generateEntry(BlockState blockState) {
        if (blockState.getLuminance() > 0) {
//				LOGGER.info("blockState: " + blockState.getBlock());
            BlockInfo blockInfo = parseBlockString(blockState.getBlock().toString());

            if (blockInfo == null) return;
            String modID = blockInfo.modId();
            String blockID = blockInfo.blockId();

            BooleanProperty litProperty = Properties.LIT;
            // Check if the block has the "lit" property
            if (blockState.getProperties().contains(litProperty)) {
                boolean isLit = blockState.get(litProperty);

                if (isLit) {
                    ActiveBlocks.addActiveBlock(modID, new SupportBlock(blockID, (byte) blockState.getLuminance(), new String[]{"lit=true"}));
                } else {
                    ActiveBlocks.addActiveBlock(modID, new SupportBlock(blockID, (byte) blockState.getLuminance(), new String[]{"lit=false"}));
                }
            } else {
                ActiveBlocks.addActiveBlock(modID, new SupportBlock(blockID, (byte) blockState.getLuminance(), null));
            }
        }
    }

    public static BlockInfo parseBlockString(String blockString) {
        // Define a regex pattern for extracting mod_id and block_id
        Pattern pattern = Pattern.compile("Block\\{([^:]+):([^}]+)\\}");

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(blockString);

        // Check if the pattern matches the input string
        if (matcher.matches()) {
            String modId = matcher.group(1);
            String blockId = matcher.group(2);

            return new BlockInfo(modId, blockId);
        }

        // Return null if the input string doesn't match the expected format
        LOGGER.error("Block string '" + blockString + "' doesn't match the expected format");
        return null;
    }

    private record BlockInfo(String modId, String blockId) {}

    private static void copyModAssets() {
        LOGGER.info("Copying mod assets...");
        // Get the path to the game directory
        Path gameDirPath = FabricLoader.getInstance().getGameDir();

        // Get the path to the "game directory" > config > shimmer > compatibility > temp
        Path tempAssetsDirPath = gameDirPath.resolve("config").resolve("shimmer").resolve("compatibility").resolve("temp");
        // Create "temp" directory if it doesn't exist
        if (!tempAssetsDirPath.toFile().exists()) {
            tempAssetsDirPath.toFile().mkdirs();
        }

        // Get the path to the mod folder
        Path modDirPath = gameDirPath.resolve("mods");
        LOGGER.info("modDirPath: " + modDirPath);

        // Get the folder containing the mods
        File modDir = new File(modDirPath.toString());
        LOGGER.info("Found '" + modDir.listFiles().length + "' mods");
        // Iterate over all the mods
        for (File mod : modDir.listFiles()) {
            LOGGER.info("file: " + mod);

            // Check if the mod is a jar file
            if (mod.isFile() && mod.getName().endsWith(".jar")) {
                // Get the path to the mod jar file
                Path modJarPath = modDirPath.resolve(mod.getName());
                LOGGER.info("modJarPath: " + modJarPath);

                try (JarFile jar = new JarFile(modJarPath.toFile())) {
                    LOGGER.info("jar: " + jar.getName());
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().startsWith("assets/")) {
                            LOGGER.info("entry: " + entry.getName());

                            // If the entry is a directory, create it
                            // Create only the directories "blockstates", "models", "textures" and parent directories of those
                            if (entry.isDirectory()) {
                                LOGGER.info("entry is directory");
                                Path outputPath = tempAssetsDirPath.resolve(entry.getName());

                                // Check if entry does not contain "blockstates", "models", or "textures" and does not contain 2 '/', if so, skip it
                                if (!(entry.getName().contains("blockstates") || entry.getName().contains("models") || entry.getName().contains("textures")) && entry.getName().chars().filter(ch -> ch == '/').count() != 2) {
                                    continue;
                                }
                                // If the condition is not met, create directories if they don't exist
                                if (!outputPath.toFile().exists()) {
                                    LOGGER.info("outputPath: " + outputPath);
                                    outputPath.toFile().mkdirs();
                                }
                            } else {
                                // If the entry is a file, copy it to the output folder
                                LOGGER.info("entry is file");

                                // check if the entry is inside "blockstates", "models" or "textures" folders, if so copy it
                                if (entry.getName().contains("blockstates") || entry.getName().contains("models") || entry.getName().contains("textures")) {
                                    Path outputPath = tempAssetsDirPath.resolve(entry.getName());
                                    LOGGER.info("outputPath: " + outputPath);
                                    if (outputPath.getParent().toFile().exists()) {
                                        Files.copy(jar.getInputStream(entry), outputPath, StandardCopyOption.REPLACE_EXISTING);
                                    }
                                    LOGGER.info("entry copied");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                // Get the path to the "game directory" > config > shimmer > compatibility > temp > assets
                // Remove every empty folder inside "assets"
                Path tempAssetsDirAssetsPath = tempAssetsDirPath.resolve("assets");
                LOGGER.info("tempAssetsDirAssetsPath: " + tempAssetsDirAssetsPath);
                File tempAssetsDirAssetsFile = tempAssetsDirAssetsPath.toFile();
                File[] tempAssetsDirFolders = tempAssetsDirAssetsFile.listFiles();
                if (tempAssetsDirFolders != null) {
                    for (File tempAssetsDirFolder : tempAssetsDirFolders) {
                        if (tempAssetsDirFolder.isDirectory()) {
                            if (Objects.requireNonNull(tempAssetsDirFolder.listFiles()).length == 0) {
                                if (tempAssetsDirFolder.delete()) {
                                    LOGGER.info("tempAssetsDirFolder '" + tempAssetsDirFolder.getName() + "' deleted");
                                } else {
                                    LOGGER.error("tempAssetsDirFolder '" + tempAssetsDirFolder.getName() + "' NOT deleted");
                                }
                            }
                        }
                    }
                }
//                try {
//                    Files.walk(tempAssetsDirAssetsPath)
//                            .filter(Files::isDirectory)
//                            .filter(path -> !path.equals(tempAssetsDirAssetsPath))
//                            .forEach(path -> {
//                                LOGGER.info("path: " + path);
//                                try {
//                                    if (path.toFile().listFiles().length == 0) {
//                                        Files.delete(path);
//                                    }
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    private static void deleteModAssets() {
        LOGGER.info("Deleting mod assets...");
        // Get the path to the game directory
        Path gameDirPath = FabricLoader.getInstance().getGameDir();

        // Get the path to the "game directory" > config > shimmer > compatibility > temp
        Path tempAssetsDirPath = gameDirPath.resolve("config").resolve("shimmer").resolve("compatibility").resolve("temp");

        // Delete the "temp" directory
        File tempAssetsDirFile = tempAssetsDirPath.toFile();
        if (deleteFolder(tempAssetsDirFile)) {
            LOGGER.info("tempAssets folder deleted");
        } else {
            LOGGER.error("tempAssets folder NOT deleted");
        }
    }
    private static boolean deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Recursive call for subdirectories
                        if (!deleteFolder(file)) {
                            LOGGER.warn("Unable to delete all files");
                            return false; // Unable to delete all files
                        }
                    } else {
                        // Delete files in the folder
                        if (!file.delete()) {
                            LOGGER.warn("Unable to delete a file '" + file.getName() + "'");
                            return false; // Unable to delete file
                        }
                    }
                }
            }

            // Delete the empty folder
            return folder.delete();
        }

        return false; // Folder doesn't exist
    }
}
