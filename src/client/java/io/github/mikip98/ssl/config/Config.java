package io.github.mikip98.ssl.config;

public class Config {
    public static boolean deleteUnusedConfigs = false;  // TODO: Implement this
    public static boolean enableRadiusColorCompensation = true;
    public static boolean enableBloom = true;
    public static byte bloomMinRadius = 8;

    public static boolean doNotSplitColorReference = false;  // Potential fix for some mods, Shimmer bug fix
    public static boolean resourcePackGeneration = false;  // Potential fix for some mods, Shimmer bug fix

    @Deprecated
    public static short auto_alpha = 195;
    public static short auto_block_alpha = 170;
    public static short auto_item_alpha = 228;

    public static boolean debugAssetsCopy = false;
    public static boolean debugAutoSupportGeneration = false;
}
