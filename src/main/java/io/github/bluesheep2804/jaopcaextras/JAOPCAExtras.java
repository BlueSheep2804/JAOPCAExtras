package io.github.bluesheep2804.jaopcaextras;

import io.github.bluesheep2804.jaopcaextras.registries.JAOPCAExtrasCreativeTabs;
import io.github.bluesheep2804.jaopcaextras.registries.JAOPCAExtrasItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

@Mod(JAOPCAExtras.MOD_ID)
public class JAOPCAExtras {
    public static final String MOD_ID = "jaopcaextras";

    public JAOPCAExtras(IEventBus modEventBus) {
        JAOPCAExtrasItems.ITEMS.register(modEventBus);
        if (ModList.get().isLoaded("ae2")) {
            JAOPCAExtrasCreativeTabs.CREATIVE_TABS.register(modEventBus);
        }
    }
}
