package io.github.mikip98;

import java.util.ArrayList;

public class SupportedMods {
    public static ArrayList<SupportedMod> getSupportedMods() {
        ArrayList<SupportedMod> supportedMods = new ArrayList<>();

        // Add supported mods here
        // Test mod
//        ArrayList<Color> testModColors = new ArrayList<>();
//        testModColors.add(new Color("blue", 0, 0, 255, 255));
//        ArrayList<LightSource> testModLightSourceBlocks = new ArrayList<>();
//        testModLightSourceBlocks.add(new LightSource("stone", "blue", 10));
//        ArrayList<LightSource> testModLightSourceItems = new ArrayList<>();
//        testModLightSourceItems.add(new LightSource("stone", "blue", 10));
//        supportedMods.add(new SupportedMod("Test Mod", "minecraft", "testmod", testModColors, testModLightSourceBlocks, testModLightSourceItems));
        // End of test mod

        return supportedMods;
    }
}