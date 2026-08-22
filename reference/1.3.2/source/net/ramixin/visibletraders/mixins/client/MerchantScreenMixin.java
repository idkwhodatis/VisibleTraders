package net.ramixin.visibletraders.mixins.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.class_1661;
import net.minecraft.class_1728;
import net.minecraft.class_1802;
import net.minecraft.class_1914;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_465;
import net.minecraft.class_492;
import net.ramixin.visibletraders.ducks.MerchantMenuDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(class_492.class)
public abstract class MerchantScreenMixin extends class_465<class_1728> {

    @Shadow int scrollOff;

    public MerchantScreenMixin(class_1728 abstractContainerMenu, class_1661 inventory, class_2561 component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffers;size()I", ordinal = 1))
    private void disableOptionIfOutOfLevelRange(class_332 guiGraphics, int i, int j, float f, CallbackInfo ci, @Local class_492.class_493 tradeOfferButton) {
        tradeOfferButton.field_22763 = ((MerchantMenuDuck) this.field_2797).visibleTraders$shouldAllowTrade(tradeOfferButton.method_20228() + scrollOff);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen$TradeOfferButton;renderToolTip(Lnet/minecraft/client/gui/GuiGraphics;II)V"))
    private void ifStillGeneratingChangeTooltip(class_492.class_493 instance, class_332 guiGraphics, int i, int j, Operation<Void> original) {
        if(!instance.method_49606()) {
            original.call(instance, guiGraphics, i, j);
            return;
        }
        int indexer = instance.method_20228() + this.scrollOff;
        if(indexer >= this.field_2797.method_17438().size()) return;
        class_1914 offer = this.field_2797.method_17438().get(indexer);
        if(!offer.method_19272().method_7960()) original.call(instance, guiGraphics, i, j);
        else if(!offer.method_8250().method_31574(class_1802.field_8077)) original.call(instance, guiGraphics, i, j);
        else {
            guiGraphics.method_51438(this.field_22793, class_2561.method_43471("menu.trading.generating_full"), i, j);
        }
    }

    // BEHOLD: THE LEAST SKETCHY WAY TO RENDER TEXT INSTEAD OF ITEMS

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
    private <E> E decideIfTradeIsStillGenerating(Iterator<E> instance, Operation<E> original, @Share("visibletraders:isFuture") LocalBooleanRef isFuture) {
        E value = original.call(instance);
        if(!(value instanceof class_1914 offer)) return value;
        if(!offer.method_19272().method_7960()) return value;
        if(!offer.method_8250().method_31574(class_1802.field_8077)) return value;
        isFuture.set(true);
        return value;
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;canScroll(I)Z"))
    private boolean preventRenderingIfFuture(boolean original, @Share("visibletraders:isFuture") LocalBooleanRef isFuture) {
        if(isFuture.get()) return true;
        return original;
    }

    @Definition(id = "scrollOff", field = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;scrollOff:I")
    @Expression("? < 7 + this.scrollOff")
    @ModifyExpressionValue(method = "render", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean continuePreventingRenderingIfFutureAndRender(boolean original, @Share("visibletraders:isFuture") LocalBooleanRef isFuture, @Local(argsOnly = true) class_332 graphics, @Local(ordinal = 2) int k, @Local(ordinal = 4) LocalIntRef m) {
        if(!isFuture.get() || !original) return original;
        graphics.method_27534(this.field_22793, class_2561.method_43471("menu.trading.generating"), k + 50, m.get() + 7, 0xFF_FF_FF_FF);
        m.set(m.get() + 20);
        isFuture.set(false);
        return false;
    }
}
