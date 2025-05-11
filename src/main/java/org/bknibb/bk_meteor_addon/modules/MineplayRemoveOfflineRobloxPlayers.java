package org.bknibb.bk_meteor_addon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.entity.player.PlayerEntity;
import org.bknibb.bk_meteor_addon.BkMeteorAddon;
import org.bknibb.bk_meteor_addon.MineplayUtils;

public class MineplayRemoveOfflineRobloxPlayers extends Module {
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
