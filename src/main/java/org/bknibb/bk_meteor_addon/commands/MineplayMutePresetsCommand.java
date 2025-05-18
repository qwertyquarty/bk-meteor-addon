package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.StringIdentifiable;

public class MineplayMutePresetsCommand extends Command {
    public MineplayMutePresetsCommand() {
        super("mp-mute", "Will mute a player using mineplay admin mute presets (requires /mute).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        var argument = argument("player", PlayerArgumentType.create());
        for (MutePreset preset : MutePreset.values()) {
            argument = argument.then(literal(preset.name()).executes(context -> {
                PlayerEntity player = PlayerArgumentType.get(context);
                mc.getNetworkHandler().sendChatCommand("mute " + player.getName().getString() + " " + preset.asString());
                return SINGLE_SUCCESS;
            }).then(literal("-s").executes(context -> {
                PlayerEntity player = PlayerArgumentType.get(context);
                mc.getNetworkHandler().sendChatCommand("mute " + player.getName().getString() + " " + preset.asString() + " -s");
                return SINGLE_SUCCESS;
            })));
        }
        argument = argument.then(argument("text", StringArgumentType.greedyString()).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            String text = StringArgumentType.getString(context, "text");
            mc.getNetworkHandler().sendChatCommand("mute  " + player.getName().getString() + " " + text);
            return SINGLE_SUCCESS;
        }));
        builder.then(argument);
    }

    private enum MutePreset implements StringIdentifiable {
        Spamming,
        HateSpeech;

        @Override
        public String asString() {
            if (this == Spamming) {
                return "Spamming";
            } else if (this == HateSpeech) {
                return "Hate Speech";
            }
            return null;
        }
    }
}
