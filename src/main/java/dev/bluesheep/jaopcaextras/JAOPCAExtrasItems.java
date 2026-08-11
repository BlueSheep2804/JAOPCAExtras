package dev.bluesheep.jaopcaextras;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

//? if neoforge {
import net.neoforged.neoforge.registries.DeferredItem;
//?} else {
/*import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.RegistryObject;
*///?}

public class JAOPCAExtrasItems {
    //? if neoforge {
    public static DeferredRegister.Items REGISTRY = DeferredRegister.createItems(JAOPCAExtras.MODID);
    public static DeferredItem<Item> EXTRA_PRESS = REGISTRY.registerSimpleItem("extra_press");
    //?} else {
    /*public static DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, JAOPCAExtras.MODID);
    public static RegistryObject<Item> EXTRA_PRESS = REGISTRY.register("extra_press", () -> new Item(new Item.Properties()));
    *///?}
}
