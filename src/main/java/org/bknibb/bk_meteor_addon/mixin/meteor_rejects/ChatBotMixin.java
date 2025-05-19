package org.bknibb.bk_meteor_addon.mixin.meteor_rejects;

import anticope.rejects.modules.ChatBot;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatBot.class)
public class ChatBotMixin {
    @Redirect(method = "onMessageRecieve", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/utils/player/ChatUtils;sendPlayerMsg(Ljava/lang/String;)V"), remap = false)
    private void sendPlayerMsg(String message, ReceiveMessageEvent event) {
        Pattern pattern = Pattern.compile("^(?:(?:<[^>]+>|\\S+)\\s)*<?([a-zA-Z0-9_]+)>?:\\s|^(?:(?:<[^>]+>|\\S+)\\s)*<([a-zA-Z0-9_]+)>\\s");
        Matcher matcher = pattern.matcher(event.getMessage().getString());
        if (matcher.find()) {
            String name = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            message = message.replace("<sender>", name);
        }
        ChatUtils.sendPlayerMsg(message);
    }
}
