package io.github.mikip98.ssl.automation.structures;

public class LightSource {
    public final String blockId;
    public final String colorName;
    public final int radius;
    public final String extraStates;
    public boolean isEnabled = true;

    public LightSource(String blockId, String colorName, int radius) {
        this.blockId = blockId;
        this.colorName = colorName;
        this.radius = radius;
        this.extraStates = null;
    }
    public LightSource(String blockId, String colorName, int radius, String extraStates) {
        this.blockId = blockId;
        this.colorName = colorName;
        this.radius = radius;
        this.extraStates = extraStates;
    }
    public LightSource(String blockId, String colorName, int radius, boolean isEnabled) {
        this.blockId = blockId;
        this.colorName = colorName;
        this.radius = radius;
        this.extraStates = null;
        this.isEnabled = isEnabled;
    }
    public LightSource(String blockId, String colorName, int radius, String extraStates, boolean isEnabled) {
        this.blockId = blockId;
        this.colorName = colorName;
        this.radius = radius;
        this.extraStates = extraStates;
        this.isEnabled = isEnabled;
    }
}
