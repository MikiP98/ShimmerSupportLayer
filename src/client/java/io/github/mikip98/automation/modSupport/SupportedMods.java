package io.github.mikip98.automation.modSupport;

import io.github.mikip98.automation.structures.SupportedMod;

import java.util.ArrayList;

public class SupportedMods {
    public static ArrayList<SupportedMod> getSupportedMods() {
        ArrayList<SupportedMod> supportedMods = new ArrayList<>();

        supportedMods.addAll(HandMadeSupport.getSupportedMods());
        supportedMods.addAll(SemiAutomaticSupport.getSupportedMods());

        // Add mods with native support to not interfere with them (only modId is required)
        supportedMods.add(new SupportedMod("Humility AFM", "humility-afm",
                null, null, null, null));
        supportedMods.add(new SupportedMod("Supplementaries", "supplementaries",
                null, null, null, null));

        return supportedMods;
    }

    public static ArrayList<SupportedMod> getSSLSupportedMods() {
        ArrayList<SupportedMod> supportedMods = new ArrayList<>();

        supportedMods.addAll(HandMadeSupport.getSupportedMods());
        supportedMods.addAll(SemiAutomaticSupport.getSupportedMods());

        return supportedMods;
    }
}
