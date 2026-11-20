package dev.bucketcrystaloptimizer.mixin;

import dev.bucketcrystaloptimizer.BucketCrystalOptimizer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void bucketCrystalOptimizer$countClientTick(CallbackInfo ci) {
        Minecraft client = (Minecraft) (Object) this;
        if (client.level != null && !client.isPaused()) {
            BucketCrystalOptimizer.get().keptCrystals().ticked();
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void bucketCrystalOptimizer$aimPastHiddenCrystal(CallbackInfoReturnable<Boolean> cir) {
        BucketCrystalOptimizer.get().pastKeptCrystal().attackStarted((Minecraft) (Object) this);
    }

    @Inject(method = "startAttack", at = @At("RETURN"))
    private void bucketCrystalOptimizer$restoreAttackTarget(CallbackInfoReturnable<Boolean> cir) {
        BucketCrystalOptimizer.get().pastKeptCrystal().attackEnded((Minecraft) (Object) this);
    }

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void bucketCrystalOptimizer$aimPlacementPastHiddenCrystal(CallbackInfo ci) {
        BucketCrystalOptimizer.get().pastKeptCrystal().useStarted((Minecraft) (Object) this);
    }

    @ModifyVariable(method = "startUseItem", at = @At("STORE"), ordinal = 0)
    private ItemStack bucketCrystalOptimizer$selectCrystalHand(ItemStack stack) {
        BucketCrystalOptimizer.get().pastKeptCrystal().handTried((Minecraft) (Object) this, stack);
        return stack;
    }

    @Inject(method = "startUseItem", at = @At("RETURN"))
    private void bucketCrystalOptimizer$restorePlacementTarget(CallbackInfo ci) {
        BucketCrystalOptimizer.get().pastKeptCrystal().useEnded((Minecraft) (Object) this);
    }
}
