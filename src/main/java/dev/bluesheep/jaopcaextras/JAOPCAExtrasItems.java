package dev.bluesheep.jaopcaextras;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredRegister;

public class JAOPCAExtrasItems {
    public static DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, JAOPCAExtras.MODID);

    public static Lazy<Item> EXTRA_PRESS = createItem("extra_press");

    private static Lazy<Item> createItem(String id) {
        var item = REGISTRY.register(id, () -> new Item(new Item.Properties()));
        return Lazy.of(item);
    }
}
