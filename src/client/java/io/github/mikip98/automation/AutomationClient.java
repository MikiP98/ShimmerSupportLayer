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
//		FabricClientCommandSource source = context.getSource();
//
//        assert MinecraftClient.getInstance().player != null;
//        MinecraftClient.getInstance().player.sendMessage(Text.of("Executing /shimmer dumpLightBlockStates"), false);
//		MinecraftClient.getInstance().player.getCommandSource().sendFeedback(() -> Text.of("/shimmer dumpLightBlockStates"), false);
//		return 1;

//		FabricClientCommandSource source = context.getSource();
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.sendMessage(Text.of("works"), false);

        StringBuilder dumpCSVString = new StringBuilder();

        for (Block block : Registries.BLOCK) {
            // Check if the block emits light
            BlockState blockState = block.getDefaultState();
            if (blockState.getLuminance() > 0) {
                // Extract texture and calculate average color
                // Save the information to your file
//				raw.append(blockState.getBlock()).append(" ").append(blockState.getLuminance()).append("\n");

//				BlockTextureExtractor.getColor(blockState);

//				LOGGER.info("blockState: " + blockState.getBlock());
                BlockInfo blockInfo = parseBlockString(blockState.getBlock().toString());

                assert blockInfo != null;
                String modID = blockInfo.getModId();
                String blockID = blockInfo.getBlockId();

                String entry = modID + ';' + blockID + ';' + blockState.getLuminance() + '\n';
                dumpCSVString.append(entry);


//				Identifier blockTexture = new Identifier(modID, "block/" + blockID);

                // Get Minecraft client instance
                MinecraftClient minecraftClient = MinecraftClient.getInstance();

                Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(new Identifier(modID, "block/" + blockID));
                LOGGER.info("sprite: " + sprite);

                // Get the Identifier of the atlas
                Identifier atlasId = sprite.getAtlasId();

                // Use MinecraftClient to get the Texture
                AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(atlasId);

                // Access pixel colors
                int width = sprite.getX();
                int height = sprite.getY();
                LOGGER.info("width: " + width + ", height: " + height);
//				int[] pixels = new int[width * height];

                // Ensure the texture is loaded before accessing pixel colors
                LOGGER.info("texture class: " + texture.getClass().toString());
//				if (texture instanceof NativeImageBackedTexture) {
////				if (true) {
//					NativeImageBackedTexture imageTexture = (NativeImageBackedTexture) texture;
////					SpriteAtlasTexture imageTexture = (SpriteAtlasTexture) texture;
//					NativeImage nativeImage = imageTexture.getImage();
//
////					nativeImage.getPixels().getRGB(0, 0, width, height, pixels, 0, width);
//
//					int[] pixels = nativeImage.copyPixelsRgba();
//					int r_sum = 0, g_sum = 0, b_sum = 0;
//					int r_weight = 0, g_weight = 0, b_weight = 0;
//					for (int pixel : pixels) {
//						int r = (pixel >> 16) & 0xFF;
//						int g = (pixel >> 8) & 0xFF;
//						int b = pixel & 0xFF;
//						r_sum += r * r;
//						g_sum += g * g;
//						b_sum += b * b;
//						r_weight += r;
//						g_weight += g;
//						b_weight += b;
//					}
//
//					// Calculate average color
//					int totalR = 0, totalG = 0, totalB = 0;
//					int pixelCount = pixels.length;
//
//					for (int pixel : pixels) {
//						totalR += (pixel >> 16) & 0xFF;
//						totalG += (pixel >> 8) & 0xFF;
//						totalB += pixel & 0xFF;
//					}
//
//					int avgR = totalR / pixelCount;
//					int avgG = totalG / pixelCount;
//					int avgB = totalB / pixelCount;
//
//					// Use avgR, avgG, avgB as the average color
//					LOGGER.info("avgR: " + avgR + ", avgG: " + avgG + ", avgB: " + avgB);
//				}

//				int[] pixels = new int[width * height];
//				sprite.getAtlas().getTexture().getImage().getRGB(0, 0, width, height, pixels, 0, width);

//				BakedModel model = minecraftClient.getBlockRenderManager().getModel(blockState);
//
//				for (BakedQuad quad : model.getQuads(blockState, null, null)) {
//					int[] vertexData = quad.getVertexData();
//					// The texture information is typically stored in the vertex data
//					// You can extract the texture coordinates and use them to identify the texture
//					// The exact structure of vertex data may vary based on the Minecraft version and rendering pipeline
//
//				}

//				ModelIdentifier modelId = ModelHelper.stateModelMap.get(blockState);
//				BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(modelId);


                // Get the sprite for the given block texture
//				SpriteAtlasTexture spriteAtlas = (SpriteAtlasTexture) minecraftClient.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
//				Sprite sprite = spriteAtlas.getSprite(blockTexture);
//				LOGGER.info("sprite: " + sprite);

                // Now you can use the sprite to get texture information
                // For example, you can get the sprite's width and height


                // Do whatever you need with the texture information
            }
        }

        // Create files dump and dump2 in the game directory > config > shimmer > compatibility
        Path gameDirPath = FabricLoader.getInstance().getGameDir();
        File dumpCSVFile = new File(gameDirPath + "/config/shimmer/compatibility/light_blocks_plus_radius.csv");

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

        return 1;
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
