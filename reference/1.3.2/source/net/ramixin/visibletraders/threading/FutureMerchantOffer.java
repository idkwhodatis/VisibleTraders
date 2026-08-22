package net.ramixin.visibletraders.threading;

import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1914;
import net.minecraft.class_9306;

public class FutureMerchantOffer extends class_1914 {

    private final Mutable<class_1914> offer = new MutableObject<>();
    private final SerializableListing listing;
    private final Supplier<class_1914> future;


    public FutureMerchantOffer(SerializableListing listing, Supplier<class_1914> future) {
        super(new class_9306(class_1802.field_8162), class_1802.field_8077.method_7854(), 0, 0, 0f);
        this.listing = listing;
        this.future = future;
    }

    public void fulfillFuture() {
        offer.setValue(future.get());
    }

    public SerializableListing getListing() {
        return listing;
    }

    public boolean isFulfilled() {
        return offer.getValue() != null;
    }

    public @Nullable class_1914 getFuture() {
        return offer.getValue();
    }

    @Override
    public @NotNull class_1799 method_8246() {
        return offer.getValue() != null ? offer.getValue().method_8246() : super.method_8246();
    }

    @Override
    public @NotNull class_1799 method_19272() {
        return offer.getValue() != null ? offer.getValue().method_19272() : super.method_19272();
    }

    @Override
    public @NotNull class_1799 method_8247() {
        return offer.getValue() != null ? offer.getValue().method_8247() : super.method_8247();
    }

    @Override
    public @NotNull class_9306 method_57556() {
        return offer.getValue() != null ? offer.getValue().method_57556() : super.method_57556();
    }

    @Override
    public @NotNull Optional<class_9306> method_57557() {
        return offer.getValue() != null ? offer.getValue().method_57557() : super.method_57557();
    }

    @Override
    public @NotNull class_1799 method_8250() {
        return offer.getValue() != null ? offer.getValue().method_8250() : super.method_8250();
    }

    @Override
    public void method_19274() {
        if (offer.getValue() != null) offer.getValue().method_19274();
        else super.method_19274();
    }

    @Override
    public @NotNull class_1799 method_18019() {
        return offer.getValue() != null ? offer.getValue().method_18019() : super.method_18019();
    }

    @Override
    public int method_8249() {
        return offer.getValue() != null ? offer.getValue().method_8249() : super.method_8249();
    }

    @Override
    public void method_19275() {
        if (offer.getValue() != null) offer.getValue().method_19275();
        else super.method_19275();
    }

    @Override
    public int method_8248() {
        return offer.getValue() != null ? offer.getValue().method_8248() : super.method_8248();
    }

    @Override
    public void method_8244() {
        if (offer.getValue() != null) offer.getValue().method_8244();
        else super.method_8244();
    }

    @Override
    public int method_21725() {
        return offer.getValue() != null ? offer.getValue().method_21725() : super.method_21725();
    }

    @Override
    public void method_8245(int i) {
        if (offer.getValue() != null) offer.getValue().method_8245(i);
        else super.method_8245(i);
    }

    @Override
    public void method_19276() {
        if (offer.getValue() != null) offer.getValue().method_19276();
        else super.method_19276();
    }

    @Override
    public int method_19277() {
        return offer.getValue() != null ? offer.getValue().method_19277() : super.method_19277();
    }

    @Override
    public void method_19273(int i) {
        if (offer.getValue() != null) offer.getValue().method_19273(i);
        else super.method_19273(i);
    }

    @Override
    public float method_19278() {
        return offer.getValue() != null ? offer.getValue().method_19278() : super.method_19278();
    }

    @Override
    public int method_19279() {
        return offer.getValue() != null ? offer.getValue().method_19279() : super.method_19279();
    }

    @Override
    public boolean method_8255() {
        return offer.getValue() != null ? offer.getValue().method_8255() : super.method_8255();
    }

    @Override
    public void method_8254() {
        if (offer.getValue() != null) offer.getValue().method_8254();
        else super.method_8254();
    }

    @Override
    public boolean method_21834() {
        return offer.getValue() != null ? offer.getValue().method_21834() : super.method_21834();
    }

    @Override
    public boolean method_8256() {
        return offer.getValue() != null ? offer.getValue().method_8256() : super.method_8256();
    }

    @Override
    public boolean method_16952(class_1799 a, class_1799 b) {
        return offer.getValue() != null ? offer.getValue().method_16952(a, b) : super.method_16952(a, b);
    }

    @Override
    public boolean method_16953(class_1799 a, class_1799 b) {
        return offer.getValue() != null ? offer.getValue().method_16953(a, b) : super.method_16953(a, b);
    }

    @Override
    public @NotNull class_1914 method_53881() {
        return offer.getValue() != null ? offer.getValue().method_53881() : super.method_53881();
    }
}
