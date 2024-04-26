package io.github.mikip98.automation.structures;

import java.util.ArrayList;
import java.util.Map;

public class SupportedMod {
    public final String modName;
    public final String modId;
    public final String searchPhrase;
    public final ArrayList<Color> Colors;
    public final ArrayList<LightSource> lightSourceBlocks;  // Block id, color name
    public final ArrayList<LightSource> lightSourceItems;  // Item id, color name

    public SupportedMod(String modName, String modId, String searchPhrase, ArrayList<Color> Colors, ArrayList<LightSource> lightSourceBlocks, ArrayList<LightSource> lightSourceItems) {
        this.modName = modName;
        this.modId = modId;
        this.searchPhrase = searchPhrase;
        this.Colors = Colors;
        this.lightSourceBlocks = lightSourceBlocks;
        this.lightSourceItems = lightSourceItems;
    }
}
