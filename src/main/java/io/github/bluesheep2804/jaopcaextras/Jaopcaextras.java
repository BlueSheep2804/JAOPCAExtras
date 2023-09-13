package io.github.bluesheep2804.jaopcaextras;

import io.github.bluesheep2804.jaopcaextras.registries.JaopcaextrasItems;
import io.github.bluesheep2804.jaopcaextras.registries.JaopcaextrasCreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Jaopcaextras.MOD_ID)
public class Jaopcaextras {
    public static final String MOD_ID = "jaopcaextras";
    
    public Jaopcaextras() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        JaopcaextrasItems.ITEMS.register(modEventBus);
        JaopcaextrasCreativeTabs.CREATIVE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
