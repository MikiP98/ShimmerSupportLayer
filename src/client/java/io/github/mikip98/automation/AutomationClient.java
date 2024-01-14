package io.github.mikip98.automation;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.mikip98.ShimmerSupportLayer.LOGGER;

public class AutomationClient {
    public static int dumpling(CommandContext<FabricClientCommandSource> context) {
        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Started the dump generation..."), false);
        }

        StringBuilder dumpCSVString = new StringBuilder();

        for (Block block : Registries.BLOCK) {
            // Check if the block emits light
            BlockState blockState = block.getDefaultState();

            BooleanProperty litProperty = Properties.LIT;
            boolean hasLitProperty = blockState.getProperties().contains(litProperty);
            boolean isLit = false;
            // Check if the block has the "lit" property
            if (hasLitProperty) {
                isLit = blockState.get(litProperty);
            }

            String entry = generateEntry(blockState);
            if (entry != null) {
                dumpCSVString.append(entry);
            }
            if (hasLitProperty) {
//                LOGGER.info("blockStateLitBefore: " + blockState.get(Properties.LIT));
                blockState = blockState.with(Properties.LIT, !isLit);
//                LOGGER.info("blockStateLitAfter: " + blockState.get(Properties.LIT));
                entry = generateEntry(blockState);
                if (entry != null) {
//                    if (dumpCSVString.length() - entry.length() > 0) {
//                        if (entry.equals(dumpCSVString.substring(dumpCSVString.length() - entry.length()))) {
//                            continue;
//                        }
//                    }
                    dumpCSVString.append(entry);
                }
            }
        }

        // Create files dump and dump2 in the game directory > config > shimmer > compatibility
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        File dumpCSVFile = new File(gameDirPath + "/config/shimmer/compatibility/light_blocks_plus_radius.csv");

        //  if the directory doesn't exist, then create it
        if (!dumpCSVFile.getParentFile().exists()) {
            dumpCSVFile.getParentFile().mkdirs();
        }

        // if file doesn't exist, then create it, else overwrite it
        if (!dumpCSVFile.exists()) {
            try {
                dumpCSVFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write debug to file
        try (FileWriter writer = new FileWriter(dumpCSVFile)) {
            writer.write(dumpCSVString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (MinecraftClient.getInstance().player != null) {
            MinecraftClient.getInstance().player.sendMessage(Text.of("Ended the dump generation..."), false);
        }
        return 1;
    }

    private static String generateEntry(BlockState blockState) {
        String entry = null;
        if (blockState.getLuminance() > 0) {
//				LOGGER.info("blockState: " + blockState.getBlock());
            BlockInfo blockInfo = parseBlockString(blockState.getBlock().toString());

            assert blockInfo != null;
            String modID = blockInfo.getModId();
            String blockID = blockInfo.getBlockId();

            BooleanProperty litProperty = Properties.LIT;
            boolean isLit = false;
            // Check if the block has the "lit" property
            if (blockState.getProperties().contains(litProperty)) {
                isLit = blockState.get(litProperty);

                if (isLit) {
                    entry = modID + ';' + blockID + ';' + blockState.getLuminance() + ";lit=true\n";
                } else {
                    entry = modID + ';' + blockID + ';' + blockState.getLuminance() + ";lit=false\n";
                }
            } else {
                entry = modID + ';' + blockID + ';' + blockState.getLuminance() + ";\n";
            }
        }
        return entry;
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
        return null;
    }

    static class BlockInfo {
        private final String modId;
        private final String blockId;

        public BlockInfo(String modId, String blockId) {
            this.modId = modId;
            this.blockId = blockId;
        }

        public String getModId() {
            return modId;
        }

        public String getBlockId() {
            return blockId;
        }
    }
}
