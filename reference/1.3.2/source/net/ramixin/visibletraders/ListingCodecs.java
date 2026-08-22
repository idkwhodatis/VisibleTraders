package net.ramixin.visibletraders;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.class_3853;
import net.minecraft.class_6862;
import net.minecraft.class_7924;
import net.minecraft.class_9428;
import net.ramixin.visibletraders.ducks.TreasureMapForEmeraldsDuck;
import net.ramixin.visibletraders.threading.SerializableListing;

public interface ListingCodecs {

    MapCodec<class_3853.class_1654> TREASURE_MAP_FOR_EMERALDS_CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Codec.INT.fieldOf("emeraldCost").forGetter(listing -> TreasureMapForEmeraldsDuck.get(listing).visibleTrades$getEmeraldCost()),
            class_6862.method_40090(class_7924.field_41246).fieldOf("destination").forGetter(listing -> TreasureMapForEmeraldsDuck.get(listing).visibleTrades$getDestination()),
            Codec.STRING.fieldOf("displayName").forGetter(listing -> TreasureMapForEmeraldsDuck.get(listing).visibleTrades$getDisplayName()),
            class_9428.field_50017.fieldOf("destinationType").forGetter(listing -> TreasureMapForEmeraldsDuck.get(listing).visibleTrades$getDestinationType()),
            Codec.INT.fieldOf("maxUses").forGetter(listing -> TreasureMapForEmeraldsDuck.get(listing).visibleTrades$getMaxUses()),
            Codec.INT.fieldOf("villagerXp").forGetter(listing -> TreasureMapForEmeraldsDuck.get(listing).visibleTrades$getVillagerXp())
    ).apply(instance, class_3853.class_1654::new));

    MapCodec<SerializableListing> NORMALIZED_TREASURE_MAP_FOR_EMERALDS_CODEC = normalize(TREASURE_MAP_FOR_EMERALDS_CODEC);


    static <T> MapCodec<SerializableListing> normalize(MapCodec<T> codec) {
        //noinspection unchecked
        return codec.xmap(t -> {
            if (t instanceof SerializableListing listing) return listing;
            else return null;
        }, serializableListing -> (T) serializableListing);
    }

}
