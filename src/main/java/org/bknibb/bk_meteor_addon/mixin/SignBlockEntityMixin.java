package org.bknibb.bk_meteor_addon.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.bknibb.bk_meteor_addon.modules.BadWordFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntity {
    private SignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "parseLines", at = @At(value = "TAIL", target = "Lnet/minecraft/block/entity/SignBlockEntity;parseLines(Lnet/minecraft/block/entity/SignText;)Lnet/minecraft/block/entity/SignText;"))
    private void onFrontWordsReadParse(SignText signText, CallbackInfoReturnable<SignText> cir) {
        SignBlockEntity ts = (SignBlockEntity) (Object) this;

        if (Modules.get().get(BadWordFinder.class).isActive()) {
            BadWordFinder.BadWordCheck(ts.getFrontText().getMessages(false), ts.getPos(), false);
            BadWordFinder.BadWordCheck(ts.getBackText().getMessages(true), ts.getPos(), true);
        }
    }

    @Inject(method = "setFrontText", at = @At("RETURN"))
    private void onSetFrontText(SignText signText, CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(BadWordFinder.class).isActive()) {
            Text[] texts = signText.getMessages(false);
            //frontBadWords = BadWordFinder.badWordCheck(texts, instance.getPos());
            BadWordFinder.BadWordCheck(texts, getPos(), false);
        }
    }

    @Inject(method = "setBackText", at = @At("RETURN"))
    private void onSetBackText(SignText signText, CallbackInfoReturnable<Boolean> cir) {
        if (Modules.get().get(BadWordFinder.class).isActive()) {
            Text[] texts = signText.getMessages(false);
            //backBadWords = BadWordFinder.badWordCheck(texts, instance.getPos());
            BadWordFinder.BadWordCheck(texts, getPos(), true);
        }
    }


}
