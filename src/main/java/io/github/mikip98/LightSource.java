package io.github.mikip98;

public class LightSource {
    public final String blockId;
    public final String colorName;
    public final int radius;
    public final String extraStates;

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
}
