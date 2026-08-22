package net.ramixin.visibletraders.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_1299;
import net.minecraft.class_1641;
import net.minecraft.class_1642;
import net.minecraft.class_1646;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_3218;
import net.ramixin.visibletraders.LockedTradeData;
import net.ramixin.visibletraders.ducks.VillagerDuck;
import net.ramixin.visibletraders.ducks.ZombieVillagerDuck;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(class_1641.class)
public abstract class ZombieVillagerMixin extends class_1642 implements ZombieVillagerDuck {

    @Unique
    private final Mutable<LockedTradeData> lockedTradeData = new MutableObject<>();

    public ZombieVillagerMixin(class_1299<? extends class_1642> entityType, class_1937 level) {
        super(entityType, level);
    }

    @Unique
    private void ifPresent(Consumer<LockedTradeData> consumer) {
        LockedTradeData val = lockedTradeData.getValue();
        if(val == null) return;
        consumer.accept(val);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void saveLockedTradeData(class_2487 compoundTag, CallbackInfo ci) {
        ifPresent(data -> data.write(compoundTag, this.method_56673()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readLockedTradeData(class_2487 compoundTag, CallbackInfo ci) {
        lockedTradeData.setValue(LockedTradeData.constructOrNull(compoundTag, this, this.method_56673()));
    }

    @Inject(method = "finishConversion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;setVillagerXp(I)V"))
    private void transferTradesToVillager(class_3218 serverLevel, CallbackInfo ci, @Local class_1646 villager) {
        VillagerDuck.of(villager).visibleTraders$setLockedTradeData(lockedTradeData.getValue());
    }

    @Override
    public void visibleTraders$setLockedTradeData(LockedTradeData data) {
        this.lockedTradeData.setValue(data);
    }
}
