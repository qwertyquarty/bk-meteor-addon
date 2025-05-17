package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringIdentifiable;

public class MineplayRobloxBanPresetsCommand extends Command {
    public MineplayRobloxBanPresetsCommand() {
        super("mp-rban", "Will rban a roblox player using mineplay admin rban presets (requires /rban).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerArgumentType.create()).then(argument("text", StringArgumentType.word()).suggests((context, suggestionsBuilder) -> {
            for (RBanPreset preset : RBanPreset.values()) {
                suggestionsBuilder.suggest(preset.asString());
            }
            return suggestionsBuilder.buildFuture();
        }).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            String text = StringArgumentType.getString(context, "text");
            mc.getNetworkHandler().sendChatCommand("rban " + player.getName().getString() + " " + text);
            return SINGLE_SUCCESS;
        })));
    }

    private enum RBanPreset implements StringIdentifiable {
        Griefing,
        InappropriateBehaviour,
        InappropriateBuilds,
        Racism,
        Homophobia,
        HateSpeech;

        @Override
        public String asString() {
            if (this == Griefing) {
                return "Griefing";
            } else if (this == InappropriateBuilds) {
                return "InappropriateBuilds";
            } else if (this == InappropriateBehaviour) {
                return "InappropriateBehaviour";
            } else if (this == Racism) {
                return "Racism";
            } else if (this == Homophobia) {
                return "Homophobia";
            } else if (this == HateSpeech) {
                return "HateSpeech";
            }
            return null;
        }
    }
}
