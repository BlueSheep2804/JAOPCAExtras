package io.github.bluesheep2804.jaopcaextras.registries;

import io.github.bluesheep2804.jaopcaextras.Jaopcaextras;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

import static io.github.bluesheep2804.jaopcaextras.registries.JaopcaextrasItems.EXTRA_PRESS;

public class JaopcaextrasCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Jaopcaextras.MOD_ID);

    public static final RegistryObject<CreativeModeTab> X = CREATIVE_TABS.register("jaopcaextras", () -> CreativeModeTab.builder()
            .withTabsBefore(new ResourceLocation("jaopca:tab"))
            .title(Component.translatable("itemGroup." + Jaopcaextras.MOD_ID))
            .icon(Items.GUNPOWDER::getDefaultInstance)
            .displayItems((parameters, output) -> {
                output.acceptAll(Stream.of(
                        EXTRA_PRESS
                ).map(sup -> sup.get().getDefaultInstance()).toList());
            })
            .build()
    );
}
