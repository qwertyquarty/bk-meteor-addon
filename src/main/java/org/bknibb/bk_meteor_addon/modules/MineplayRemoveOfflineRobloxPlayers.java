package org.bknibb.bk_meteor_addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import org.bknibb.bk_meteor_addon.BkMeteorAddon;
import org.bknibb.bk_meteor_addon.MineplayUtils;

import java.util.ArrayList;
import java.util.List;

public class MineplayRemoveOfflineRobloxPlayers extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Boolean> hidePlayerEntity = sgGeneral.add(new BoolSetting.Builder()
            .name("hide-player-entity")
            .description("Hide Player Entity.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> hidePlayerShadow = sgGeneral.add(new BoolSetting.Builder()
        .name("hide-player-shadow")
        .description("Hide Player Shadow.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> hidePlayerListEntry = sgGeneral.add(new BoolSetting.Builder()
            .name("hide-player-list-entry")
            .description("Hide Player List Entry.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> hidePlayerInNametags = sgGeneral.add(new BoolSetting.Builder()
            .name("hide-player-in-name-tags")
            .description("Hide Player In The Nametags Module.")
            .defaultValue(true)
            .build()
    );

    public final Setting<Boolean> hidePlayerLoginLogoutMessages = sgGeneral.add(new BoolSetting.Builder()
            .name("hide-player-login-logout-messages")
            .description("Hide Player Login And Logout Messages.")
            .defaultValue(true)
            .build()
    );

    public MineplayRemoveOfflineRobloxPlayers() {
        super(BkMeteorAddon.CATEGORY, "remove-offline-roblox-players", "Removes offline roblox players (for mineplay).");
    }

    @Override
    public void onActivate() {
        if (!MineplayUtils.isOnMineplay()) {
            info("This module is only designed for mineplay (mc.mineplay.nl).");
            return;
        }
    }
}
