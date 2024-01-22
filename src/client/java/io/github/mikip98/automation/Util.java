package io.github.mikip98.automation;

import io.github.mikip98.automation.structures.Color;
import io.github.mikip98.config.Config;

public class Util {
    public static short bias(Color color) {
        if (color != null && Config.enableRadiusColorCompensation) {
            short sum = (short) (color.red + color.green + color.blue);
            if (sum <= 255) {
                return 1;
            } else if (sum > 510) {
                return -1;
            }
        }
        return 0;
    }

    public static int clampLight(int value) {
        return Math.max(0, Math.min(15, value));
    }
}
