package dev.bucketcrystaloptimizer.crystal;

import com.google.common.collect.Iterables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Tracks crystals that the local client predicts have been broken.
 *
 * Kept entities are hidden from rendering immediately, but are retained in
 * client world lookups unless a placement/targeting operation explicitly
 * needs to look past them. The server remains authoritative.
 */
public final class KeptCrystals {
    // Fallback for rejected/cancelled hits when no block-state acknowledgement arrives.
    private static final long HIDDEN_TICKS = 30;

    private final List<EndCrystal> kept = new ArrayList<>();
    private long lastKeptAt;
    private volatile long ticks;
    private boolean seeingPast;

    private static boolean isHidden(Entity entity, long keptSince) {
        return entity instanceof KeptCrystal crystal
                && crystal.bucketCrystalOptimizer$isKept()
                && crystal.bucketCrystalOptimizer$keptAt() - keptSince > 0;
    }

    public boolean isHidden(Entity entity) {
        return isHidden(entity, ticks - HIDDEN_TICKS);
    }

    public void keep(EndCrystal crystal, int sequence) {
        long now = ticks;
        forgetSettled(now - HIDDEN_TICKS);
        if (isHidden(crystal, now - HIDDEN_TICKS)) {
            return;
        }

        ((KeptCrystal) crystal).bucketCrystalOptimizer$keep(now, sequence);
        kept.add(crystal);
        lastKeptAt = now;
    }

    public void ticked() {
        ticks++;
    }

    /**
     * A block-state acknowledgement is ordered after earlier client actions.
     * Once it advances past the sequence recorded at the hit, the prediction
     * can be released; the server has processed the interaction before it.
     */
    public void acknowledged(int sequence) {
        forgetSettled(ticks - HIDDEN_TICKS);
        kept.removeIf(crystal -> {
            KeptCrystal tracked = (KeptCrystal) crystal;
            if (tracked.bucketCrystalOptimizer$sequence() >= sequence) {
                return false;
            }
            tracked.bucketCrystalOptimizer$release();
            return true;
        });
    }

    public <T> T seePast(Supplier<T> lookup) {
        seeingPast = true;
        try {
            return lookup.get();
        } finally {
            seeingPast = false;
        }
    }

    public Predicate<? super Entity> hide(Predicate<? super Entity> predicate) {
        long keptSince = ticks - HIDDEN_TICKS;
        if (!seeingPast || nothingKept(keptSince)) {
            return predicate;
        }
        return entity -> !isHidden(entity, keptSince) && predicate.test(entity);
    }

    /**
     * Used only by vanilla's EndCrystalItem placement check. It changes the
     * local prediction result; it does not send or modify any network packet.
     */
    public List<Entity> withoutHidden(List<Entity> entities) {
        long keptSince = ticks - HIDDEN_TICKS;
        for (Entity entity : entities) {
            if (isHidden(entity, keptSince)) {
                List<Entity> visible = new ArrayList<>(entities);
                visible.removeIf(other -> isHidden(other, keptSince));
                return visible;
            }
        }
        return entities;
    }

    public Iterable<Entity> hide(Iterable<Entity> entities) {
        long keptSince = ticks - HIDDEN_TICKS;
        if (nothingKept(keptSince)) {
            return entities;
        }
        return Iterables.filter(entities, entity -> !isHidden(entity, keptSince));
    }

    private void forgetSettled(long keptSince) {
        kept.removeIf(crystal -> crystal.isRemoved() || !isHidden(crystal, keptSince));
    }

    private boolean nothingKept(long keptSince) {
        if (kept.isEmpty()) {
            return true;
        }
        if (lastKeptAt - keptSince <= 0) {
            kept.clear();
            return true;
        }
        return false;
    }
}
