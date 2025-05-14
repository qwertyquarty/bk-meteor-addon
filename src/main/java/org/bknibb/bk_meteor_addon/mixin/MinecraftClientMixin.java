package org.bknibb.bk_meteor_addon.mixin;

import net.minecraft.client.MinecraftClient;
import org.bknibb.bk_meteor_addon.BkMeteorAddon;
import org.bknibb.bk_meteor_addon.ConfigModifier;
import org.bknibb.bk_meteor_addon.update_system.UpdateSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "onFinishedLoading", at = @At("RETURN"))
    private void onFinishedLoading(CallbackInfo info) {
        if (ConfigModifier.get().checkForUpdates.get()) {
            UpdateSystem.checkForUpdates(BkMeteorAddon.INSTNACE);
        }
    }
}
