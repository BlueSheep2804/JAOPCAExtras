//? if >= 1.21.1 {
package dev.bluesheep.jaopcaextras.recipes.extendedae;

import com.glodblock.github.extendedae.recipe.CrystalAssemblerRecipe;
import com.glodblock.github.glodium.recipe.stack.IngredientStack;
import com.google.gson.JsonElement;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

import java.util.List;
import java.util.Optional;

public record CrystalAssemblerRecipeSerializer(
        Object output,
        int outputCount,
        List<Object> inputs,
        int inputCount
) implements IRecipeSerializer {
    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();

        return miscHelper.serializeRecipe(new CrystalAssemblerRecipe(
                miscHelper.getItemStack(output, outputCount),
                inputs.stream().map(it -> IngredientStack.of(miscHelper.getIngredient(it), inputCount)).toList(),
                Optional.empty()
        ));
    }
}
//?}