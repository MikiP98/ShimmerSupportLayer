package io.github.mikip98.config;

public class Config {
    public static boolean deleteUnusedConfigs = false;  // TODO: Implement this
    public static boolean enableRadiusColorCompensation = true;
    public static boolean enableBloom = true;
    public static byte bloomMinRadius = 0;

    @Deprecated
    public static short auto_alpha = 170;
    public static short auto_block_alpha = 170;
    public static short auto_item_alpha = 228;

    public static boolean debugAssetsCopy = false;
    public static boolean debugAutoSupportGeneration = false;
}
