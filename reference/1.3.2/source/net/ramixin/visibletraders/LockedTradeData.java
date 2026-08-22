package net.ramixin.visibletraders;

import com.mojang.serialization.Codec;
import net.minecraft.class_1297;
import net.minecraft.class_1646;
import net.minecraft.class_1914;
import net.minecraft.class_1916;
import net.minecraft.class_2487;
import net.minecraft.class_2499;
import net.minecraft.class_2509;
import net.minecraft.class_2520;
import net.minecraft.class_3218;
import net.minecraft.class_3850;
import net.minecraft.class_5455;
import net.minecraft.class_6903;
import net.ramixin.visibletraders.ducks.VillagerDuck;
import net.ramixin.visibletraders.threading.FutureMerchantOffer;
import net.ramixin.visibletraders.threading.SerializableListing;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LockedTradeData {

    private List<class_1916> lockedOffers;

    public LockedTradeData(class_1646 villager) {
        this.lockedOffers = generateTrades(villager);
    }

    private LockedTradeData(List<class_1916> offers) {
        this.lockedOffers = new ArrayList<>(offers);
    }

    public static @Nullable LockedTradeData constructOrNull(class_2487 inputTag, class_1297 entity, class_5455 registryAccess) {
        if(!(entity.method_37908() instanceof class_3218 level))
            return null;

        class_2520 lockedOffers = inputTag.method_10580("LockedOffers");
        class_6903<class_2520> contextualizedNbtOps = registryAccess.method_57093(class_2509.field_11560);
        Optional<List<class_1916>> maybeOffers;
        if(lockedOffers == null) maybeOffers = Optional.empty();
        else maybeOffers = Optional.of(class_1916.field_48850.listOf().parse(contextualizedNbtOps, lockedOffers).getOrThrow());

        if(maybeOffers.isEmpty()) return null;
        List<class_1916> offers = maybeOffers.get();

        class_2520 futureOfferIndices = inputTag.method_10580("futureOfferIndices");
        Optional<List<Long>> maybeIndices;
        if(futureOfferIndices == null) maybeIndices = Optional.empty();
        else maybeIndices = Optional.of(Codec.LONG.listOf().parse(contextualizedNbtOps, futureOfferIndices).getOrThrow());

        if(maybeIndices.isPresent()) {
            List<int[]> indices = maybeIndices.get().stream().map(lung -> new int[]{(int) (lung >> 32), (int) (lung & 0xFF_FF_FF_FFL)}).toList();

            class_2520 futureOffers = inputTag.method_10580("futureOffers");
            Optional<List<SerializableListing>> maybeListings;
            if(futureOffers == null) maybeListings = Optional.empty();
            else maybeListings = Optional.of(VisibleTraders.CODEC.listOf().parse(contextualizedNbtOps, futureOffers).getOrThrow());

            if(maybeListings.isEmpty()) return null;
            List<SerializableListing> listings = maybeListings.get();
            if(listings.size() != indices.size()) return null;
            for(int i = 0; i < listings.size(); i++) {
                SerializableListing listing = listings.get(i);
                int[] index = indices.get(i);
                if(index[0] >= offers.size()) return null;
                class_1916 offerSet = offers.get(index[0]);
                FutureMerchantOffer futureOffer = new FutureMerchantOffer(listing, () -> listing.visibleTrades$buildOffer(level, entity, entity.method_59922()));
                VisibleTraders.TRADE_WORKER.addOrder(futureOffer);
                offerSet.add(index[1], futureOffer);
            }
        }
        return new LockedTradeData(offers);
    }

    private static ArrayList<class_1916> generateTrades(class_1646 villager) {
        class_1916 offers = villager.method_8264();
        class_3850 data = villager.method_7231();
        ArrayList<class_1916> lockedOffers = new ArrayList<>();
        int level = data.method_16925();
        while(level < 5) {
            villager.method_7195(data.method_16920(++level));
            int prev = offers.size();
            VillagerDuck.of(villager).visibleTraders$updateTrades();
            int dif = offers.size() - prev;
            class_1916 newOffers = new class_1916();
            for(int i = 0; i < dif; i++) newOffers.add(offers.removeLast());
            lockedOffers.add(newOffers);
        }
        villager.method_7195(data);
        return lockedOffers;
    }

    public void write(class_2487 outputTag, class_5455 registryAccess) {
        if(this.lockedOffers == null) return;
        List<class_1916> offersToWrite = new ArrayList<>();
        List<Long> futureOfferIndexes = new ArrayList<>();
        List<SerializableListing> futureOffers = new ArrayList<>();
        for (int i = 0; i < lockedOffers.size(); i++) {
            class_1916 offers = lockedOffers.get(i);
            class_1916 newOffers = new class_1916();
            for (int j = 0; j < offers.size(); j++) {
                class_1914 offer = offers.get(j);
                if (!(offer instanceof FutureMerchantOffer futureOffer)) newOffers.add(offer);
                else {
                    if (futureOffer.isFulfilled())
                        newOffers.add(Objects.requireNonNull(futureOffer.getFuture(), "Future offers was null although fulfilled"));
                    else {
                        futureOfferIndexes.add(((long)i) << 32 | j);
                        futureOffers.add(futureOffer.getListing());
                    }
                }
            }
            offersToWrite.add(newOffers);
        }
        class_6903<class_2520> contextualizedNbtOps = registryAccess.method_57093(class_2509.field_11560);
        class_2520 lockedOffersNbt = class_1916.field_48850.listOf().encode(offersToWrite, contextualizedNbtOps, new class_2499()).getOrThrow();
        class_2520 futureOfferIndicesNbt = Codec.LONG.listOf().encode(futureOfferIndexes, contextualizedNbtOps, new class_2499()).getOrThrow();
        class_2520 futureOffersNbt = VisibleTraders.CODEC.listOf().encode(futureOffers, contextualizedNbtOps, new class_2499()).getOrThrow();
        outputTag.method_10566("LockedOffers", lockedOffersNbt);
        outputTag.method_10566("futureOfferIndices", futureOfferIndicesNbt);
        outputTag.method_10566("futureOffers", futureOffersNbt);
    }

    public boolean hasNoOffers() {
        return this.lockedOffers.isEmpty();
    }

    public class_1916 popTradeSet() {
        if(this.lockedOffers == null || hasNoOffers()) return null;
        return this.lockedOffers.removeFirst();
    }

    public Optional<class_1916> peekTradeSet() {
        if(this.lockedOffers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.lockedOffers.getFirst());
    }

    public class_1916 buildLockedOffers() {
        class_1916 lockedOffers = new class_1916();
        if(this.lockedOffers == null) return lockedOffers;
        for(class_1916 listOffers : this.lockedOffers) for(class_1914 offer : listOffers) {
            if(offer.method_8250().method_7960() && !(offer instanceof FutureMerchantOffer)) {
                this.lockedOffers = new ArrayList<>();
                VisibleTraders.LOGGER.error("detected incomplete trade. Rebuilding locked offers");
                return new class_1916();
            }
            lockedOffers.add(offer);
        }
        return lockedOffers;
    }

    public void tick(class_1646 villager, Runnable popCallback) {
        if(this.lockedOffers == null) return;
        int requiredSets = 5 - villager.method_7231().method_16925();
        while(requiredSets < this.lockedOffers.size()) popCallback.run();
        if(requiredSets > this.lockedOffers.size()) {
            VisibleTraders.LOGGER.error("detected missing locked trade sets. Rebuilding locked offers");
            this.lockedOffers = generateTrades(villager);
        }
    }
}
