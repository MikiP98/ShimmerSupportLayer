package io.github.mikip98.automation.modSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VanillaBlocksBlockstateAtlas {
    private static final Map<String, Set<String>> BLOCKSTATE_TEXTURES_MAP = new HashMap<>();

    public static Set<String> get(String blockstate) {
        return BLOCKSTATE_TEXTURES_MAP.get(blockstate);
    }
}
