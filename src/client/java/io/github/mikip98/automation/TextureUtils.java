//package io.github.mikip98;
//
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.render.VertexConsumerProvider;
//import net.minecraft.client.render.model.BakedModel;
//import net.minecraft.client.texture.NativeImage;
//import net.minecraft.client.texture.Sprite;
//import net.minecraft.client.util.ModelIdentifier;
//import net.minecraft.util.Identifier;
////import net.minecraft.util.math.Matrix4f;
//import net.minecraft.world.BlockRenderView;
//import org.joml.Matrix4f;
//
//public class TextureUtils {
//
//    public static int[] getAverageColor(BlockRenderView blockView, ModelIdentifier modelId) {
//        MinecraftClient minecraftClient = MinecraftClient.getInstance();
//        BakedModel model = minecraftClient.getBakedModelManager().getModel(modelId);
//
//        int width = 16; // Assuming a standard block size
//        int height = 16;
//
//        int[] pixels = new int[width * height];
//
//        Matrix4f matrix = new Matrix4f();
//        model.getQuads(null, null, null).get(0).getSpriteDependencies().forEach(sprite -> {
//            matrix.loadIdentity();
//            VertexConsumerProvider.Immediate immediate = minecraftClient.getBufferBuilders().getEntityVertexConsumers();
//            minecraftClient.getTextureManager().bindTexture(sprite.getAtlasId());
//            sprite.upload(immediate, 0, sprite.getAnnotatedSprite().getMaxU(), sprite.getAnnotatedSprite().getMaxV(), sprite.getAnnotatedSprite().getMinU(), sprite.getAnnotatedSprite().getMinV(), matrix);
//            immediate.draw();
//
//            NativeImage image = new NativeImage(width, height, true);
//            image.loadFromTextureImage(0, false);
//
//            for (int y = 0; y < height; y++) {
//                for (int x = 0; x < width; x++) {
//                    int color = image.getPixelColor(x, y);
//                    pixels[x + y * width] = color;
//                }
//            }
//
//            image.close();
//        });
//
//        return calculateAverageColor(pixels);
//    }
//
//    private static int[] calculateAverageColor(int[] pixels) {
//        int totalR = 0, totalG = 0, totalB = 0;
//
//        for (int pixel : pixels) {
//            totalR += (pixel >> 16) & 0xFF;
//            totalG += (pixel >> 8) & 0xFF;
//            totalB += pixel & 0xFF;
//        }
//
//        int pixelCount = pixels.length;
//        int avgR = totalR / pixelCount;
//        int avgG = totalG / pixelCount;
//        int avgB = totalB / pixelCount;
//
//        return new int[]{avgR, avgG, avgB};
//    }
//}
//
