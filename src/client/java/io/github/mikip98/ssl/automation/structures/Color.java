package io.github.mikip98.ssl.automation.structures;

public class Color {
    public final String name;
    public final int red;
    public final int green;
    public final int blue;
    public final int alpha;

    public Color(String name, int red, int green, int blue, int alpha) {
        this.name = name;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(String name, int hex, float alpha) {
        this.name = name;
        this.red = (hex >> 16) & 0xFF;
        this.green = (hex >> 8) & 0xFF;
        this.blue = hex & 0xFF;
        this.alpha = (int) (alpha * 255);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Color color = (Color) obj;
//        return name.equals(color.name);
        return red == color.red && green == color.green && blue == color.blue && alpha == color.alpha && name.equals(color.name);
    }
}
