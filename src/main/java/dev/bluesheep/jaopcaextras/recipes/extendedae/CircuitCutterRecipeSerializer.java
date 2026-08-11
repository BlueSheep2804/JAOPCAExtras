package dev.bluesheep.jaopcaextras.recipes.extendedae;

import com.glodblock.github.extendedae.recipe.CircuitCutterRecipe;
//~ if >= 1.21 'extendedae.recipe.util.IngredientStack' -> 'glodium.recipe.stack.IngredientStack'
import com.glodblock.github.glodium.recipe.stack.IngredientStack;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

//? if < 1.21 {
/*import com.google.gson.JsonObject;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
*///?}

public record CircuitCutterRecipeSerializer(
        ResourceLocation resourceLocation,
        Object output, int outputCount,
        Object input
) implements IRecipeSerializer {
    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();

        //~ if >= 1.21 'this.' -> 'miscHelper.'
        return miscHelper.serializeRecipe(new CircuitCutterRecipe(
                //? if < 1.21
                //resourceLocation,
                miscHelper.getItemStack(output, outputCount),
                IngredientStack.of(miscHelper.getIngredient(input), 1)
                //? if < 1.21
                //, IngredientStack.of(new FluidStack(Fluids.WATER.getSource(), 100))
                //? if >= 26.1
                //, outputCount * 2000L
        ));
    }

    //? if < 1.21 {
    /*private JsonElement serializeRecipe(CircuitCutterRecipe recipe) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", CircuitCutterRecipe.TYPE_ID.toString());
        com.glodblock.github.extendedae.recipe.CircuitCutterRecipeSerializer.INSTANCE.toJson(obj, recipe);
        return obj;
    }
    *///?}
}
