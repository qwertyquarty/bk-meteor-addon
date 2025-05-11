package org.bknibb.bk_meteor_addon.modules;

import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.ArrayListDeque;
import org.bknibb.bk_meteor_addon.BkMeteorAddon;

import java.util.List;
import java.util.UUID;

public class PlayerLoginLogoutNotifier extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgWhitelist = settings.createGroup("Whitelist");

    private final Setting<JoinLeaveModes> joinsLeavesMode = sgGeneral.add(new EnumSetting.Builder<JoinLeaveModes>()
        .name("player-joins-leaves")
        .description("How to handle player join/leave notifications.")
        .defaultValue(JoinLeaveModes.Both)
        .build()
    );

    private final Setting<Integer> notificationDelay = sgGeneral.add(new IntSetting.Builder()
        .name("notification-delay")
        .description("How long to wait in ticks before posting the next join/leave notification in your chat.")
        .range(0, 1000)
        .sliderRange(0, 100)
        .defaultValue(0)
        .build()
    );

    private final Setting<Boolean> simpleNotifications = sgGeneral.add(new BoolSetting.Builder()
        .name("simple-notifications")
        .description("Display join/leave notifications without a prefix, to reduce chat clutter.")
        .defaultValue(true)
        .build()
    );

    private final Setting<PlayerTracers.ListMode> listMode = sgWhitelist.add(new EnumSetting.Builder<PlayerTracers.ListMode>()
        .name("list-mode")
        .description("Selection mode.")
        .defaultValue(PlayerTracers.ListMode.Blacklist)
        .build()
    );

    private final Setting<List<String>> blacklist = sgWhitelist.add(new StringListSetting.Builder()
        .description("The players you don't want to see.")
        .visible(() -> listMode.get() == PlayerTracers.ListMode.Blacklist)
        .build()
    );

    private final Setting<List<String>> whitelist = sgWhitelist.add(new StringListSetting.Builder()
        .name("whitelist")
        .description("The players you want to see.")
        .visible(() -> listMode.get() == PlayerTracers.ListMode.Whitelist)
        .build()
    );

    private int timer;
    //private boolean loginPacket = true;
    private final ArrayListDeque<Text> messageQueue = new ArrayListDeque<>();

    public PlayerLoginLogoutNotifier() {
        super(BkMeteorAddon.CATEGORY, "player-login-logout-notifier", "Notifies you when a player logs in or out.");
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {
        timer = 0;
        messageQueue.clear();
    }

    @EventHandler
    private void onGameJoin(GameJoinedEvent event) {
        timer = 0;
        messageQueue.clear();
    }

    @EventHandler
    private void onGameLeave(GameLeftEvent event) {
        //loginPacket = true;
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        switch (event.packet) {
            case PlayerListS2CPacket packet when joinsLeavesMode.get().equals(JoinLeaveModes.Both) || joinsLeavesMode.get().equals(JoinLeaveModes.Joins) -> {
//                if (loginPacket) {
//                    loginPacket = false;
//                    return;
//                }

                if (packet.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER)) {
                    createJoinNotifications(packet);
                }
            }
            case PlayerRemoveS2CPacket packet when joinsLeavesMode.get().equals(JoinLeaveModes.Both) || joinsLeavesMode.get().equals(JoinLeaveModes.Leaves) ->
                createLeaveNotification(packet);
            default -> {}
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        timer++;
        while (timer >= notificationDelay.get() && !messageQueue.isEmpty()) {
            timer = 0;
            if (simpleNotifications.get()) {
                mc.player.sendMessage(messageQueue.removeFirst(), false);
            } else {
                ChatUtils.sendMsg(messageQueue.removeFirst());
            }
        }
    }

    private void createJoinNotifications(PlayerListS2CPacket packet) {
        for (PlayerListS2CPacket.Entry entry : packet.getPlayerAdditionEntries()) {
            if (entry.profile() == null) continue;
            if (listMode.get() == PlayerTracers.ListMode.Blacklist) {
                if (blacklist.get().contains(entry.profile().getName())) {
                    continue;
                }
            } else {
                if (!whitelist.get().contains(entry.profile().getName())) {
                    continue;
                }
            }

            if (simpleNotifications.get()) {
                messageQueue.addLast(Text.literal(
                    Formatting.GRAY + "["
                        + Formatting.GREEN + "+"
                        + Formatting.GRAY + "] "
                        + entry.profile().getName()
                ));
            } else {
                messageQueue.addLast(Text.literal(
                    Formatting.WHITE
                        + entry.profile().getName()
                        + Formatting.GRAY + " joined."
                ));
            }
        }
    }

    private void createLeaveNotification(PlayerRemoveS2CPacket packet) {
        if (mc.getNetworkHandler() == null) return;

        for (UUID id : packet.profileIds()) {
            PlayerListEntry toRemove = mc.getNetworkHandler().getPlayerListEntry(id);
            if (toRemove == null) continue;
            if (listMode.get() == PlayerTracers.ListMode.Blacklist) {
                if (blacklist.get().contains(toRemove.getProfile().getName())) {
                    continue;
                }
            } else {
                if (!whitelist.get().contains(toRemove.getProfile().getName())) {
                    continue;
                }
            }

            if (simpleNotifications.get()) {
                messageQueue.addLast(Text.literal(
                    Formatting.GRAY + "["
                        + Formatting.RED + "-"
                        + Formatting.GRAY + "] "
                        + toRemove.getProfile().getName()
                ));
            } else {
                messageQueue.addLast(Text.literal(
                    Formatting.WHITE
                        + toRemove.getProfile().getName()
                        + Formatting.GRAY + " left."
                ));
            }
        }
    }

    public enum JoinLeaveModes {
        Joins, Leaves, Both
    }

    public enum ListMode {
        Whitelist,
        Blacklist
    }
}
