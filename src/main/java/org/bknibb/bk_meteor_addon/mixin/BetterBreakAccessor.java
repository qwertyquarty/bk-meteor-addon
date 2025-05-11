package org.bknibb.bk_meteor_addon.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public interface BetterBreakAccessor {
    @Accessor("blockBreakingCooldown")
    void setCooldown(int cooldown);

    @Accessor("blockBreakingCooldown")
    int getCooldown();
}
