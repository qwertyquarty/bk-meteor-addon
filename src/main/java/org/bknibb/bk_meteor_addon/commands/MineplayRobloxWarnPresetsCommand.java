package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringIdentifiable;

public class MineplayRobloxWarnPresetsCommand extends Command {
    public MineplayRobloxWarnPresetsCommand() {
        super("mp-rwarn", "Will warn a roblox player using mineplay admin warn presets.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerArgumentType.create()).then(argument("text", StringArgumentType.greedyString()).suggests((context, suggestionsBuilder) -> {
            for (RWarnPreset preset : RWarnPreset.values()) {
                suggestionsBuilder.suggest(preset.asString());
            }
            return suggestionsBuilder.buildFuture();
        }).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            String text = StringArgumentType.getString(context, "text");
            mc.getNetworkHandler().sendChatMessage(player.getName().getString() + " " + "Stop " + text + ", if you continue, you will be banned");
            return SINGLE_SUCCESS;
        })));
    }

    private enum RWarnPreset implements StringIdentifiable {
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
