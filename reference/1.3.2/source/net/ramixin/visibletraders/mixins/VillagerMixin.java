package net.ramixin.visibletraders.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1299;
import net.minecraft.class_1646;
import net.minecraft.class_1914;
import net.minecraft.class_1916;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_3218;
import net.minecraft.class_3850;
import net.minecraft.class_3851;
import net.minecraft.class_3988;
import net.minecraft.class_4094;
import net.ramixin.visibletraders.LockedTradeData;
import net.ramixin.visibletraders.ducks.VillagerDuck;
import net.ramixin.visibletraders.threading.FutureMerchantOffer;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Mixin(class_1646.class)
public abstract class VillagerMixin extends class_3988 implements class_4094, class_3851, VillagerDuck {

    @Shadow public abstract @NotNull class_3850 method_7231();

    @Shadow
    protected abstract void method_7237();

    @Unique
    private final Mutable<LockedTradeData> lockedTradeData = new MutableObject<>();

    public VillagerMixin(class_1299<? extends class_3988> entityType, class_1937 level) {
        super(entityType, level);
    }

    @Unique
    private void ifPresent(Consumer<LockedTradeData> consumer) {
        LockedTradeData val = lockedTradeData.getValue();
        if(val == null) return;
        consumer.accept(val);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void saveLockedTradeData(class_2487 valueOutput, CallbackInfo ci) {
        ifPresent(data -> data.write(valueOutput, this.method_56673()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readLockedTradeData(class_2487 valueInput, CallbackInfo ci) {
        lockedTradeData.setValue(LockedTradeData.constructOrNull(valueInput, this, this.method_56673()));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void removeLockedTradeDataIfNoOffers(CallbackInfo ci) {
        if(this.field_17721 == null)
            this.lockedTradeData.setValue(null);
        ifPresent(data -> data.tick((class_1646) (Object) this, this::appendLockedOffer));
    }

    @Inject(method = "updateTrades", at = @At("HEAD"), cancellable = true)
    private void preventAdditionalTradesOnRankIncrease(CallbackInfo ci) {
        if(this.field_17721 == null || this.field_17721.isEmpty()) {
            this.lockedTradeData.setValue(null);
            return;
        }
        if(appendLockedOffer()) ci.cancel();
    }

    @Unique
    private boolean appendLockedOffer() {
        if(this.field_17721 == null) return false;
        AtomicBoolean result = new AtomicBoolean(false);
        ifPresent(data -> {
            class_1916 dismissedTrades = data.popTradeSet();
            if(dismissedTrades != null) {
                this.field_17721.addAll(dismissedTrades);
                result.set(true);
            }
        });
        return result.get();
    }

    @WrapOperation(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;isTrading()Z", ordinal = 0))
    private boolean preventUpgradeIfStillGeneratingTrades(class_1646 instance, Operation<Boolean> original) {
        boolean originalResult = original.call(instance);
        if(originalResult) return true;
        if(lockedTradeData.getValue() == null) return false;
        LockedTradeData data = lockedTradeData.getValue();
        Optional<class_1916> maybeSoonOffers = data.peekTradeSet();
        if(maybeSoonOffers.isEmpty()) return false;
        for(class_1914 offer : maybeSoonOffers.get()) {
            if(offer instanceof FutureMerchantOffer futureOffer) {
                if(!futureOffer.isFulfilled()) return true;
            }
        }
        return false;
    }


    @Override
    public void visibleTraders$setLockedTradeData(LockedTradeData data) {
        this.lockedTradeData.setValue(data);
    }

    @Override
    public Optional<LockedTradeData> visibleTraders$getLockedTradeData() {
        return Optional.ofNullable(lockedTradeData.getValue());
    }

    @Override
    public void visibleTrades$regenerateTrades() {
        this.lockedTradeData.setValue(new LockedTradeData((class_1646) (Object) this));
    }

    @Override
    public int visibleTraders$getShiftedLevel() {
        int level = method_7231().method_16925();
        if(this.field_17721 == null) return level;
        return level | (this.field_17721.size() << 8);
    }

    @Override
    public class_1916 visibleTraders$getCombinedOffers() {
        class_1916 offers = new class_1916();
        offers.addAll(this.field_17721);
        if(lockedTradeData.getValue() == null)
            visibleTrades$regenerateTrades();
        ifPresent(data -> offers.addAll(data.buildLockedOffers()));
        return offers;
    }

    @Override
    public void visibleTraders$updateTrades() {
        if(!(method_37908() instanceof class_3218))
            return;
        method_7237();
    }
}
