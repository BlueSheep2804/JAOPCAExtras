package io.github.bluesheep2804.jaopcaextras.registries;

import io.github.bluesheep2804.jaopcaextras.JAOPCAExtras;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.github.bluesheep2804.jaopcaextras.registries.JAOPCAExtrasItems.EXTRA_PRESS;

public class JAOPCAExtrasCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, JAOPCAExtras.MOD_ID);

    public static final Supplier<CreativeModeTab> X = CREATIVE_TABS.register("jaopcaextras", () -> CreativeModeTab.builder()
            .withTabsBefore(ResourceLocation.fromNamespaceAndPath("jaopca", "tab"))
            .title(Component.translatable("itemGroup." + JAOPCAExtras.MOD_ID))
            .icon(Items.GUNPOWDER::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.acceptAll(Stream.of(
                        EXTRA_PRESS
                ).map(sup -> sup.get().getDefaultInstance()).toList());
            })
            .build()
    );
}
