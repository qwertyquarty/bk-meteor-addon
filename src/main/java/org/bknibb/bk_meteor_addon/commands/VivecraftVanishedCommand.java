package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.command.CommandSource;
import org.bknibb.bk_meteor_addon.modules.VivecraftVanishDetect;

public class VivecraftVanishedCommand extends Command {
    public VivecraftVanishedCommand() {
        super("vivecraft-vanished", "Shows vanished players on the server that have vivecraft, gets settings from and requires VivecraftVanishDetect (the server and the player vanishing must have vivecraft).");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            VivecraftVanishDetect module = Modules.get().get(VivecraftVanishDetect.class);
            for (String name : module.vanishedPlayers) {
                module.showVanishedNotification(name);
            }
            return SINGLE_SUCCESS;
        });
    }
}
