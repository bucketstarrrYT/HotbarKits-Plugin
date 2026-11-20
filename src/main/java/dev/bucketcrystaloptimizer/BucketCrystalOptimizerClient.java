package dev.bucketcrystaloptimizer;

import net.fabricmc.api.ClientModInitializer;

/**
 * Minimal, passive entrypoint.
 *
 * This mod intentionally does not intercept player input, alter item-use timing,
 * change reach, or send or modify interaction packets. Vanilla remains in full
 * control of End Crystal placement and all server-side validation.
 */
public final class BucketCrystalOptimizerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.getLogger("BucketCrystalOptimizer").log(
                System.Logger.Level.INFO,
                "Loaded. Vanilla End Crystal interaction handling is unchanged."
        );
    }
}
