package io.github.mikip98.automation;

import io.github.mikip98.automation.structures.SupportBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActiveBlocks {
    private static final Map<String, ArrayList<SupportBlock>> activeBlocksByMod = new HashMap<>();
    public static void addActiveBlock(String modId, SupportBlock supportBlock) {
        if (activeBlocksByMod.containsKey(modId)) {
            activeBlocksByMod.get(modId).add(supportBlock);
        } else {
            ArrayList<SupportBlock> supportBlocks = new ArrayList<>();
            supportBlocks.add(supportBlock);
            activeBlocksByMod.put(modId, supportBlocks);
        }
    }
    public static Map<String, ArrayList<SupportBlock>> getActiveBlocksByMod() {
        return activeBlocksByMod;
    }

    public static String staticToString() {
        StringBuilder activeBlocksByModString = new StringBuilder();
        for (Map.Entry<String, ArrayList<SupportBlock>> entry : activeBlocksByMod.entrySet()) {
            String modId = entry.getKey();
            for (SupportBlock supportBlock : entry.getValue()) {
                activeBlocksByModString.append(modId).append(';').append(supportBlock).append('\n');
            }
        }
        return activeBlocksByModString.substring(0, activeBlocksByModString.length() - 1);
    }
}
