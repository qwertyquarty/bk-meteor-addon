package org.bknibb.bk_meteor_addon;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.config.Config;

public class ConfigModifier {
    private static ConfigModifier INSTANCE;

    public final SettingGroup sgBkMeteorAddon = Config.get().settings.createGroup("BkMeteorAddon");

    public final Setting<Boolean> checkForUpdates = sgBkMeteorAddon.add(new BoolSetting.Builder()
            .name("check-for-updates")
            .description("Check for BkMeteorAddon updates.")
            .defaultValue(true)
            .build()
    );

    public static ConfigModifier get() {
        if (INSTANCE == null) INSTANCE = new ConfigModifier();
        return INSTANCE;
    }
}
