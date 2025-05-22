package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

public class MineplayIpCommand extends Command {
    public MineplayIpCommand() {
        super("mp-ip", "Will tell players the mineplay IPs.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            if (mc.getNetworkHandler() == null) return SINGLE_SUCCESS;
            mc.getNetworkHandler().sendChatMessage("Java IP: mc.mineplay.nl - Bedrock IP: pe.mineplay.nl");
            return SINGLE_SUCCESS;
        });
    }
}
