package dev.bucketcrystaloptimizer.mixin;

import dev.bucketcrystaloptimizer.BucketCrystalOptimizer;
import dev.bucketcrystaloptimizer.crystal.SequencedLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements SequencedLevel {
    @Shadow
    @Final
    private BlockStatePredictionHandler blockStatePredictionHandler;

    @Override
    public int bucketCrystalOptimizer$blockSequence() {
        return blockStatePredictionHandler.currentSequence();
    }

    @Inject(method = "entitiesForRendering", at = @At("RETURN"), cancellable = true)
    private void bucketCrystalOptimizer$hidePredictedBrokenCrystals(CallbackInfoReturnable<Iterable<Entity>> cir) {
        cir.setReturnValue(BucketCrystalOptimizer.get().keptCrystals().hide(cir.getReturnValue()));
    }

    @Inject(method = "handleBlockChangedAck", at = @At("HEAD"))
    private void bucketCrystalOptimizer$releaseAcknowledgedCrystals(int sequence, CallbackInfo ci) {
        BucketCrystalOptimizer.get().keptCrystals().acknowledged(sequence);
    }
}
