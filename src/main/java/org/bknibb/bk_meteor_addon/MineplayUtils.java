package org.bknibb.bk_meteor_addon;

import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import org.bknibb.bk_meteor_addon.modules.MineplayRemoveOfflineRobloxPlayers;

public class MineplayUtils {
public static boolean isRobloxPlayer(PlayerListEntry entry) {
        if (entry == null || entry.getDisplayName() == null || entry.getDisplayName().getStyle().getColor() == null) {
            return false;
        }
        return entry.getDisplayName().getStyle().getColor().getRgb() == 0xFF999B;
    }
    public static boolean isDisconnectedPlayer(PlayerEntity player) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (entry == null) {
            return false;
        }
        if (!isRobloxPlayer(entry)) return false;
        return entry.getLatency() == 0;
    }
    public static boolean isDisconnectedPlayer(String player) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player);
        if (entry == null) {
            return false;
        }
        if (!isRobloxPlayer(entry)) return false;
        return entry.getLatency() == 0;
    }
    public static boolean isDisconnectedPlayer(PlayerListEntry entry) {
        if (!isRobloxPlayer(entry)) return false;
        return entry.getLatency() == 0;
    }
    public static boolean isOnMineplay() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getCurrentServerEntry() == null) {
            return false;
        }
        String serverName = client.getCurrentServerEntry().address;
        return serverName.contains("mineplay.nl");
    }
    public static boolean canHide() {
        return (isOnMineplay() && Modules.get().isActive(MineplayRemoveOfflineRobloxPlayers.class));
    }
    public static boolean hidePlayer(PlayerEntity player) {
        return (canHide() && player != MinecraftClient.getInstance().player && isDisconnectedPlayer(player));
    }
    public static boolean hidePlayer(PlayerListEntry entry) {
        return (canHide() && entry.getProfile().getId() != MinecraftClient.getInstance().player.getUuid() && isDisconnectedPlayer(entry));
    }
    public static boolean hidePlayer(String player) {
        return (canHide() && !player.equals(MinecraftClient.getInstance().player.getName().getString()) && isDisconnectedPlayer(player));
    }
}
