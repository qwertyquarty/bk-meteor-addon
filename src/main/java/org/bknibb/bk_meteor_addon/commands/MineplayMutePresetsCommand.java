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
        builder.then(argument("player", PlayerArgumentType.create()).then(argument("text", StringArgumentType.greedyString()).suggests((context, suggestionsBuilder) -> {
            for (MutePreset preset : MutePreset.values()) {
                suggestionsBuilder.suggest(preset.asString());
            }
            return suggestionsBuilder.buildFuture();
        }).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            String text = StringArgumentType.getString(context, "text");
            mc.getNetworkHandler().sendChatCommand("mute " + player.getName().getString() + " " + text);
            return SINGLE_SUCCESS;
        })));
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
