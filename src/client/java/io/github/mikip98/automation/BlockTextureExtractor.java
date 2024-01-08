package io.github.mikip98.automation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static io.github.mikip98.ShimmerSupportLayer.LOGGER;

public class BlockTextureExtractor {
    public static int[] getColor(BlockState blockState) {
//        ModelLoadingPlugin .
//        PreparableModelLoadingPlugin.DataLoader

        // Get the minecraft client
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        // Get the resource manager
        ResourceManager resourceManager = minecraftClient.getResourceManager();

        // Get the texture manager
        TextureManager textureManager = minecraftClient.getTextureManager();
//        textureManager.getTexture(blockState.getTopTexture(resourceManager, blockState, null)).getGlId();

        // Get the the model for the block and then get textures from the model
//        String modelJSONText = MinecraftClient.getInstance().;
        BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModel(blockState);
//        String modelString = model.;
//        LOGGER.info(modelString);
        // Parse the model string to json

//        // Process the texture data (calculate average color, save to file, etc.)
//        int[] rgb = processTexture(nativeImage);
//
//        // Don't forget to close the NativeImage after use
//        nativeImage.close();

        return new int[]{0, 0, 0};
    }

    private static int[] getColorFromTexture(NativeImage nativeImage) {
        // Your code to process the texture (calculate average color, save to file, etc.)
        // This is where you can implement your custom logic.
        int[] pixels = nativeImage.copyPixelsRgba();
        int r_sum = 0, g_sum = 0, b_sum = 0;
        int r_weight = 0, g_weight = 0, b_weight = 0;
        for (int pixel : pixels) {
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = pixel & 0xFF;
            r_sum += r * r;
            g_sum += g * g;
            b_sum += b * b;
            r_weight += r;
            g_weight += g;
            b_weight += b;
        }

        return new int[]{r_sum/r_weight, g_sum/g_weight, b_sum/b_weight};
    }

//    public static int[] getAVG(BlockState blockState) {
//        return extractTexture(blockState);
//    }

    // Example of how to use the above method
//    public static void main(String[] args) {
//        BlockState blockState = /* Get your BlockState */;
//        extractTexture(blockState);
//    }
}
