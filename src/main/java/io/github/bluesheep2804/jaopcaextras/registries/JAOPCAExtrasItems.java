package io.github.bluesheep2804.jaopcaextras.registries;

import io.github.bluesheep2804.jaopcaextras.JAOPCAExtras;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class JAOPCAExtrasItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(JAOPCAExtras.MOD_ID);
    public static final Supplier<Item> EXTRA_PRESS = ITEMS.registerSimpleItem("extra_press");
}
