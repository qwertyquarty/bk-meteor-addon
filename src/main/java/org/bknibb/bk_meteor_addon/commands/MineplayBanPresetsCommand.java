package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringIdentifiable;

public class MineplayBanPresetsCommand extends Command {
    public MineplayBanPresetsCommand() {
        super("mp-ban", "Will ban a player using mineplay admin ban presets (requires /ban).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        var argument = argument("player", PlayerArgumentType.create());
        for (BanPreset preset : BanPreset.values()) {
            argument = argument.then(literal(preset.name()).executes(context -> {
                PlayerEntity player = PlayerArgumentType.get(context);
                mc.getNetworkHandler().sendChatCommand("ban " + player.getName().getString() + " " + preset.asString());
                return SINGLE_SUCCESS;
            }).then(literal("-s").executes(context -> {
                PlayerEntity player = PlayerArgumentType.get(context);
                mc.getNetworkHandler().sendChatCommand("ban " + player.getName().getString() + " " + preset.asString() + " -s");
                return SINGLE_SUCCESS;
            })));
        }
        argument = argument.then(argument("text", StringArgumentType.greedyString()).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            String text = StringArgumentType.getString(context, "text");
            mc.getNetworkHandler().sendChatCommand("ban " + player.getName().getString() + " " + text);
            return SINGLE_SUCCESS;
        }));
        builder.then(argument);
    }

    private enum BanPreset implements StringIdentifiable {
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
                return "Hate Speech";
            }
            return null;
        }
    }
}
