package org.bknibb.bk_meteor_addon.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import org.bknibb.bk_meteor_addon.MineplayUtils;
import org.bknibb.bk_meteor_addon.modules.MineplayBetterBreak;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(ClientPlayerInteractionManager.class)
public class BetterBreakMixin {
    @Inject(method = "attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (!Modules.get().isActive(MineplayBetterBreak.class)) return;
        if (!MineplayUtils.isOnMineplay()) return;
        if (pos.getY() < 0) return;

        mc.interactionManager.breakBlock(pos);
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction));

        ((BetterBreakAccessor) mc.interactionManager).setCooldown(5);

        cir.setReturnValue(true);
    }
}
