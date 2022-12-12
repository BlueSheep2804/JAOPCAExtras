package io.github.bluesheep2804.jaopcaextras;

import io.github.bluesheep2804.jaopcaextras.init.ItemInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Jaopcaextras.MOD_ID)
public class Jaopcaextras {
    public static final String MOD_ID = "jaopcaextras";
    
    public Jaopcaextras() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        ItemInit.ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
