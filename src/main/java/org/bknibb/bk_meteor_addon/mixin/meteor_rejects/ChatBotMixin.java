package org.bknibb.bk_meteor_addon.mixin.meteor_rejects;

import anticope.rejects.modules.ChatBot;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import org.bknibb.bk_meteor_addon.ConfigModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatBot.class)
public class ChatBotMixin {
    @Unique private static final ThreadLocal<String> currentCmd = new ThreadLocal<>();
    @Redirect(method = "onMessageRecieve", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/player/ChatUtils;sendPlayerMsg(Ljava/lang/String;)V"), remap = false)
    private void sendPlayerMsg(String message, ReceiveMessageEvent event) {
        if (ConfigModifier.get().chatBotSender.get() && message.contains("<sender>")) {
            Pattern pattern = Pattern.compile("^(?:(?:<[^>]+>|\\S+)\\s)*<?([a-zA-Z0-9_]+)>?:\\s|^(?:(?:<[^>]+>|\\S+)\\s)*<([a-zA-Z0-9_]+)>\\s");
            Matcher matcher = pattern.matcher(event.getMessage().getString());
            if (matcher.find()) {
                String name = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                message = message.replace("<sender>", name);
            }
        }
        if (ConfigModifier.get().chatBotArgs.get() && message.contains("<args>")) {
            int index = event.getMessage().getString().indexOf(currentCmd.get()) + currentCmd.get().length()+1;
            if (index < event.getMessage().getString().length()) {
                message = message.replace("<args>", event.getMessage().getString().substring(index));
            } else {
                //message = message.replace("<args>", "");
                ChatUtils.sendPlayerMsg("Please provide an argument for this command!");
                return;
            }
        }
        ChatUtils.sendPlayerMsg(message);
    }
    @Redirect(method = "onMessageRecieve", at = @At(value = "INVOKE", target = "Ljava/lang/String;endsWith(Ljava/lang/String;)Z"), remap = false)
    private boolean endsWith(String message, String command) {
        currentCmd.set(command);
        if (ConfigModifier.get().chatBotArgs.get()) {
            return message.endsWith(command) || message.contains(command + " ");
        }
        return message.endsWith(command);
    }
}
