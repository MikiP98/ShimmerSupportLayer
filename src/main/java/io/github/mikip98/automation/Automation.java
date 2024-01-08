package io.github.mikip98.automation;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Automation {
    public static int dumpling(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        source.sendFeedback(() -> Text.literal("%s × %s = %s".formatted(2, 2, 4)), false);
        // Execute the "/shimmer dumpLightBlockStates" command
//		source.sendMessage(Text.literal("/shimmer dumpLightBlockStates"));
//		source.getServer().getCommandManager().execute(source.getServer().getCommandManager().dispatcher.parse(command, source), "/shimmer dumpLightBlockStates");
//		source.getServer().getCommandManager().executeWithPrefix(source, "/shimmer dumpLightBlockStates");

        // Get the player from the context
//		ServerPlayerEntity player = source.getPlayer();
//
//		// Execute the "/shimmer dumpLightBlockStates" command on the player
//		player.sendMessage(Text.of("/shimmer dumpLightBlockStates"));

        StringBuilder debug = new StringBuilder();
//		StringBuilder debug2 = new StringBuilder();

        for (Block block : Registries.BLOCK) {
            // Check if the block emits light
            BlockState blockState = block.getDefaultState();
            if (blockState.getLuminance() > 0) {
                // Extract texture and calculate average color
                // Save the information to your file
                debug.append(blockState.getBlock()).append(" ").append(blockState.getLuminance()).append("\n");
//				debug2.append(block);
            }
        }

        // Create files dump and dump2 in the game directory > config > shimmer > compatibility
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        File dumpFile = new File(gameDirPath + "/config/shimmer/compatibility/dump.txt");
        File dumpFile2 = new File(gameDirPath + "/config/shimmer/compatibility/dump2.txt");

        // if file doesn't exist, then create it, else overwrite it
        if (!dumpFile.exists()) {
            try {
                dumpFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//		if (!dumpFile2.exists()) {
//			try {
//				dumpFile2.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}

        // Write debug to file
        try (FileWriter writer = new FileWriter(dumpFile)) {
            writer.write(debug.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//		try (FileWriter writer = new FileWriter(dumpFile2)) {
//			writer.write(debug2.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

        return 1;
    }
}
