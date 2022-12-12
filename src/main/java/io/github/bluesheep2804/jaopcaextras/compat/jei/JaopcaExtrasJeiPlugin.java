package io.github.bluesheep2804.jaopcaextras.compat.jei;

import io.github.bluesheep2804.jaopcaextras.Jaopcaextras;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.Collections;

import static io.github.bluesheep2804.jaopcaextras.init.ItemInit.EXTRA_PRESS;

@JeiPlugin
public class JaopcaExtrasJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Jaopcaextras.MOD_ID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!ModList.get().isLoaded("ae2")) {
            registration.getIngredientManager().removeIngredientsAtRuntime(
                    VanillaTypes.ITEM_STACK,
                    Collections.singletonList(EXTRA_PRESS.get().getDefaultInstance())
            );
        }
    }
}
