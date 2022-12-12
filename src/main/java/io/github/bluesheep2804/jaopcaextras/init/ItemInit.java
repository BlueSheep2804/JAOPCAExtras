package io.github.bluesheep2804.jaopcaextras.init;

import io.github.bluesheep2804.jaopcaextras.Jaopcaextras;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thelm.jaopca.api.JAOPCAApi;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Jaopcaextras.MOD_ID);
    public static RegistryObject<Item> EXTRA_PRESS = ITEMS.register("extra_press", () -> new Item(new Item.Properties().tab(JAOPCAApi.instance().creativeTab())));
}
