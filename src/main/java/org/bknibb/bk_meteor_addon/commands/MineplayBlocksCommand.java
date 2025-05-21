package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

public class MineplayBlocksCommand extends Command {
    public MineplayBlocksCommand() {
        super("mp-blocks", "Will tell players how to get blocks.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            mc.getNetworkHandler().sendChatMessage("Use /blocks or /b to open the blocks menu!");
            return SINGLE_SUCCESS;
        });
    }
}
