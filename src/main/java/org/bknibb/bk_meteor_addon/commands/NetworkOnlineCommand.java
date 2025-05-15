package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.bknibb.bk_meteor_addon.modules.NetworkLoginLogoutNotifier;

public class NetworkOnlineCommand extends Command {
    public NetworkOnlineCommand() {
        super("network-online", "Shows online players on the network, gets settings from and requires NetworkLoginLogoutNotifier (for mineplay, also may work on other server networks).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            for (String name : Modules.get().get(NetworkLoginLogoutNotifier.class).onlinePlayers) {
                showOnlineNotification(name);
            }
            return SINGLE_SUCCESS;
        });
    }

    private void showOnlineNotification(String name) {
        if (Modules.get().get(NetworkLoginLogoutNotifier.class).simpleNotifications.get()) {
            mc.player.sendMessage(Text.literal(
                Formatting.GRAY + "["
                    + Formatting.LIGHT_PURPLE + "Network"
                    + Formatting.GRAY + "] "
                    + Formatting.GRAY + "["
                    + Formatting.GREEN + "Online"
                    + Formatting.GRAY + "] "
                    + Formatting.RESET + name
            ), false);
        } else {
            ChatUtils.sendMsg(Text.literal(
                name
                    + " is "
                    + Formatting.GREEN + "online"
                    + Formatting.RESET + " on the network."
            ));
        }
    }
}
