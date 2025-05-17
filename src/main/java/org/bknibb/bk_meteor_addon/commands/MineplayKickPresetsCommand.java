package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringIdentifiable;

public class MineplayKickPresetsCommand extends Command {
    public MineplayKickPresetsCommand() {
        super("mp-kick", "Will kick a player using mineplay admin kick presets (requires /kick).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerArgumentType.create()).then(argument("text", StringArgumentType.greedyString()).suggests((context, suggestionsBuilder) -> {
            for (KickPreset preset : KickPreset.values()) {
                suggestionsBuilder.suggest(preset.asString());
            }
            return suggestionsBuilder.buildFuture();
        }).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            String text = StringArgumentType.getString(context, "text");
            String command = "kick " + player.getName().getString() + " " + "Stop " + text.replace(" -s", "") + ", if you continue, you will be banned - Kicked Warn";
            if (text.endsWith(" -s")) {
                command += " -s";
            }
            mc.getNetworkHandler().sendChatCommand(command);
            return SINGLE_SUCCESS;
        })));
    }

    private enum KickPreset implements StringIdentifiable {
        Griefing,
        InappropriateBuilds,
        ActingInappropriately,
        BeingRacist,
        BeingHomophobic,
        Spamming;

        @Override
        public String asString() {
            if (this == Griefing) {
                return "Griefing";
            } else if (this == InappropriateBuilds) {
                return "Inappropriate Builds";
            } else if (this == ActingInappropriately) {
                return "Acting Inappropriately";
            } else if (this == BeingRacist) {
                return "Being Racist";
            } else if (this == BeingHomophobic) {
                return "Being Homophobic";
            } else if (this == Spamming) {
                return "Spamming";
            }
            return null;
        }
    }
}
