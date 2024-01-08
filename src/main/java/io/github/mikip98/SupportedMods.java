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
        // Vanilla Minecraft
        ArrayList<Color> vanillaMinecraftColors = new ArrayList<>();


        // Auto-generated code

        // Mod ID: minecraft
        ArrayList<Color> minecraftColors = new ArrayList<>();
        ArrayList<LightSource> minecraftLightSourceBlocks = new ArrayList<>();
        minecraftColors.add(new Color("lava_weight_average_color", 213, 94, 20, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("lava", "lava_weight_average_color", 15));
        minecraftColors.add(new Color("brown_mushroom_weight_average_color", 188, 143, 113, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("brown_mushroom", "brown_mushroom_weight_average_color", 1));
        minecraftColors.add(new Color("torch_weight_average_color", 235, 196, 85, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("torch", "torch_weight_average_color", 14));
        minecraftColors.add(new Color("wall_torch_weight_average_color", 235, 196, 85, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("wall_torch", "wall_torch_weight_average_color", 14));
        minecraftColors.add(new Color("redstone_torch_weight_average_color", 245, 85, 36, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("redstone_torch", "redstone_torch_weight_average_color", 7));
        minecraftColors.add(new Color("redstone_wall_torch_weight_average_color", 245, 85, 36, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("redstone_wall_torch", "redstone_wall_torch_weight_average_color", 7));
        minecraftColors.add(new Color("soul_torch_weight_average_color", 131, 209, 202, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("soul_torch", "soul_torch_weight_average_color", 10));
        minecraftColors.add(new Color("soul_wall_torch_weight_average_color", 131, 209, 202, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("soul_wall_torch", "soul_wall_torch_weight_average_color", 10));
        minecraftColors.add(new Color("glowstone_weight_average_color", 233, 200, 142, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("glowstone", "glowstone_weight_average_color", 15));
        minecraftColors.add(new Color("jack_o_lantern_weight_average_color", 222, 151, 48, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("jack_o_lantern", "jack_o_lantern_weight_average_color", 15));
        minecraftColors.add(new Color("enchanting_table_weight_average_color", 144, 201, 190, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("enchanting_table", "enchanting_table_weight_average_color", 7));
        minecraftColors.add(new Color("brewing_stand_weight_average_color", 190, 166, 124, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("brewing_stand", "brewing_stand_weight_average_color", 1));
        minecraftColors.add(new Color("lava_cauldron_weight_average_color", 213, 94, 20, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("lava_cauldron", "lava_cauldron_weight_average_color", 15));
        minecraftColors.add(new Color("end_portal_weight_average_color", 52, 36, 75, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("end_portal", "end_portal_weight_average_color", 15));
        minecraftColors.add(new Color("end_portal_frame_weight_average_color", 216, 221, 159, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("end_portal_frame", "end_portal_frame_weight_average_color", 1));
        minecraftColors.add(new Color("dragon_egg_weight_average_color", 44, 1, 49, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("dragon_egg", "dragon_egg_weight_average_color", 1));
        minecraftColors.add(new Color("ender_chest_weight_average_color", 52, 36, 75, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("ender_chest", "ender_chest_weight_average_color", 7));
        minecraftColors.add(new Color("beacon_weight_average_color", 152, 225, 223, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("beacon", "beacon_weight_average_color", 15));
        minecraftColors.add(new Color("sea_lantern_weight_average_color", 204, 222, 213, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("sea_lantern", "sea_lantern_weight_average_color", 15));
        minecraftColors.add(new Color("end_rod_weight_average_color", 244, 234, 219, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("end_rod", "end_rod_weight_average_color", 14));
        minecraftColors.add(new Color("magma_block_weight_average_color", 228, 115, 31, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("magma_block", "magma_block_weight_average_color", 3));
        minecraftColors.add(new Color("sea_pickle_weight_average_color", 162, 193, 139, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("sea_pickle", "sea_pickle_weight_average_color", 6));
        minecraftColors.add(new Color("conduit_weight_average_color", 180, 158, 134, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("conduit", "conduit_weight_average_color", 15));
        minecraftColors.add(new Color("lantern_weight_average_color", 227, 176, 100, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("lantern", "lantern_weight_average_color", 15));
        minecraftColors.add(new Color("soul_lantern_weight_average_color", 120, 220, 225, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("soul_lantern", "soul_lantern_weight_average_color", 10));
        minecraftColors.add(new Color("campfire_weight_average_color", 225, 176, 71, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("campfire", "campfire_weight_average_color", 15));
        minecraftColors.add(new Color("soul_campfire_weight_average_color", 111, 221, 224, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("soul_campfire", "soul_campfire_weight_average_color", 10));
        minecraftColors.add(new Color("shroomlight_weight_average_color", 246, 157, 82, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("shroomlight", "shroomlight_weight_average_color", 15));
        minecraftColors.add(new Color("crying_obsidian_weight_average_color", 107, 7, 191, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("crying_obsidian", "crying_obsidian_weight_average_color", 10));
        minecraftColors.add(new Color("amethyst_cluster_weight_average_color", 192, 151, 223, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("amethyst_cluster", "amethyst_cluster_weight_average_color", 5));
        minecraftColors.add(new Color("large_amethyst_bud_weight_average_color", 193, 156, 220, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("large_amethyst_bud", "large_amethyst_bud_weight_average_color", 4));
        minecraftColors.add(new Color("medium_amethyst_bud_weight_average_color", 192, 149, 222, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("medium_amethyst_bud", "medium_amethyst_bud_weight_average_color", 2));
        minecraftColors.add(new Color("small_amethyst_bud_weight_average_color", 151, 116, 213, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("small_amethyst_bud", "small_amethyst_bud_weight_average_color", 1));
        minecraftColors.add(new Color("sculk_sensor_weight_average_color", 23, 108, 124, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("sculk_sensor", "sculk_sensor_weight_average_color", 1));
        minecraftColors.add(new Color("calibrated_sculk_sensor_weight_average_color", 168, 155, 209, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("calibrated_sculk_sensor", "calibrated_sculk_sensor_weight_average_color", 1));
        minecraftColors.add(new Color("sculk_catalyst_weight_average_color", 133, 170, 153, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("sculk_catalyst", "sculk_catalyst_weight_average_color", 6));
        minecraftColors.add(new Color("ochre_froglight_weight_average_color", 249, 242, 198, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("ochre_froglight", "ochre_froglight_weight_average_color", 15));
        minecraftColors.add(new Color("verdant_froglight_weight_average_color", 228, 244, 227, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("verdant_froglight", "verdant_froglight_weight_average_color", 15));
        minecraftColors.add(new Color("pearlescent_froglight_weight_average_color", 245, 239, 239, Config.auto_alpha));
        minecraftLightSourceBlocks.add(new LightSource("pearlescent_froglight", "pearlescent_froglight_weight_average_color", 15));
        supportedMods.add(new SupportedMod("minecraft", "minecraft", "jar", minecraftColors, minecraftLightSourceBlocks, null));

        // Mod ID: betternether
        ArrayList<Color> betternetherColors = new ArrayList<>();
        ArrayList<LightSource> betternetherLightSourceBlocks = new ArrayList<>();
        betternetherColors.add(new Color("willow_torch_weight_average_color", 196, 221, 240, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("willow_torch", "willow_torch_weight_average_color", 15));
        betternetherColors.add(new Color("cincinnasite_lantern_weight_average_color", 246, 170, 56, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("cincinnasite_lantern", "cincinnasite_lantern_weight_average_color", 15));
        betternetherColors.add(new Color("blue_weeping_obsidian_weight_average_color", 28, 111, 228, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("blue_weeping_obsidian", "blue_weeping_obsidian_weight_average_color", 14));
        betternetherColors.add(new Color("weeping_obsidian_weight_average_color", 141, 16, 230, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("weeping_obsidian", "weeping_obsidian_weight_average_color", 14));
        betternetherColors.add(new Color("blue_crying_obsidian_weight_average_color", 19, 87, 194, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("blue_crying_obsidian", "blue_crying_obsidian_weight_average_color", 10));
        betternetherColors.add(new Color("giant_mold_sapling_weight_average_color", 208, 196, 188, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("giant_mold_sapling", "giant_mold_sapling_weight_average_color", 7));
        betternetherColors.add(new Color("jellyfish_mushroom_sapling_weight_average_color", 182, 151, 245, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("jellyfish_mushroom_sapling", "jellyfish_mushroom_sapling_weight_average_color", 9));
        betternetherColors.add(new Color("eye_seed_weight_average_color", 226, 215, 215, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("eye_seed", "eye_seed_weight_average_color", 9));
        betternetherColors.add(new Color("ink_bush_seed_weight_average_color", 114, 64, 48, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("ink_bush_seed", "ink_bush_seed_weight_average_color", 9));
        betternetherColors.add(new Color("black_apple_seed_weight_average_color", 217, 144, 23, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("black_apple_seed", "black_apple_seed_weight_average_color", 9));
        betternetherColors.add(new Color("whispering_gourd_lantern_weight_average_color", 126, 106, 213, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("whispering_gourd_lantern", "whispering_gourd_lantern_weight_average_color", 15));
        betternetherColors.add(new Color("pig_statue_respawner_weight_average_color", 223, 164, 78, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("pig_statue_respawner", "pig_statue_respawner_weight_average_color", 15));
        betternetherColors.add(new Color("geyser_weight_average_color", 222, 112, 32, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("geyser", "geyser_weight_average_color", 10));
        betternetherColors.add(new Color("nether_brewing_stand_weight_average_color", 232, 183, 113, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("nether_brewing_stand", "nether_brewing_stand_weight_average_color", 1));
        betternetherColors.add(new Color("rubeus_cone_weight_average_color", 191, 219, 241, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("rubeus_cone", "rubeus_cone_weight_average_color", 15));
        betternetherColors.add(new Color("nether_sakura_leaves_weight_average_color", 167, 113, 126, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("nether_sakura_leaves", "nether_sakura_leaves_weight_average_color", 13));
        betternetherColors.add(new Color("soul_lily_sapling_weight_average_color", 210, 118, 48, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("soul_lily_sapling", "soul_lily_sapling_weight_average_color", 9));
        betternetherColors.add(new Color("lumabus_seed_weight_average_color", 68, 218, 200, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("lumabus_seed", "lumabus_seed_weight_average_color", 9));
        betternetherColors.add(new Color("golden_lumabus_seed_weight_average_color", 240, 252, 245, Config.auto_alpha));
        betternetherLightSourceBlocks.add(new LightSource("golden_lumabus_seed", "golden_lumabus_seed_weight_average_color", 9));
        supportedMods.add(new SupportedMod("betternether", "betternether", "better-nether", betternetherColors, betternetherLightSourceBlocks, null));

        return supportedMods;
    }
}