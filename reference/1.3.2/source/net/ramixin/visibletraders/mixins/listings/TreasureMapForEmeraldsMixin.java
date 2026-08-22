package net.ramixin.visibletraders.mixins.listings;

import com.mojang.serialization.MapCodec;
import net.minecraft.class_1297;
import net.minecraft.class_1914;
import net.minecraft.class_3195;
import net.minecraft.class_3218;
import net.minecraft.class_3853;
import net.minecraft.class_5819;
import net.minecraft.class_6862;
import net.minecraft.class_6880;
import net.minecraft.class_9428;
import net.ramixin.visibletraders.ListingCodecs;
import net.ramixin.visibletraders.ducks.TreasureMapForEmeraldsDuck;
import net.ramixin.visibletraders.threading.SerializableListing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_3853.class_1654.class)
public abstract class TreasureMapForEmeraldsMixin implements TreasureMapForEmeraldsDuck, SerializableListing {

    @Shadow @Final private int emeraldCost;
    @Shadow @Final private class_6862<class_3195> destination;
    @Shadow @Final private String displayName;
    @Shadow @Final private class_6880<class_9428> destinationType;
    @Shadow @Final private int maxUses;
    @Shadow @Final private int villagerXp;

    @Shadow
    public abstract class_1914 getOffer(class_1297 entity, class_5819 randomSource);

    @Override
    public int visibleTrades$getEmeraldCost() {
        return emeraldCost;
    }

    @Override
    public class_6862<class_3195> visibleTrades$getDestination() {
        return destination;
    }

    @Override
    public String visibleTrades$getDisplayName() {
        return displayName;
    }

    @Override
    public class_6880<class_9428> visibleTrades$getDestinationType() {
        return destinationType;
    }

    @Override
    public int visibleTrades$getMaxUses() {
        return maxUses;
    }

    @Override
    public int visibleTrades$getVillagerXp() {
        return villagerXp;
    }

    @Override
    public MapCodec<? extends SerializableListing> visibleTrades$getCodec() {
        return ListingCodecs.NORMALIZED_TREASURE_MAP_FOR_EMERALDS_CODEC;
    }

    @Override
    public class_1914 visibleTrades$buildOffer(class_3218 serverLevel, class_1297 entity, class_5819 randomSource) {
        return getOffer(entity, randomSource);
    }
}
