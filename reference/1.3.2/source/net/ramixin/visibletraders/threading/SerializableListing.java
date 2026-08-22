package net.ramixin.visibletraders.threading;

import com.mojang.serialization.MapCodec;
import net.minecraft.class_1297;
import net.minecraft.class_1914;
import net.minecraft.class_3218;
import net.minecraft.class_5819;

public interface SerializableListing {

    MapCodec<? extends SerializableListing> visibleTrades$getCodec();

    class_1914 visibleTrades$buildOffer(class_3218 level, class_1297 entity, class_5819 randomSource);
}
