package io.github.bluesheep2804.jaopcaextras.recipes;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import com.google.gson.JsonElement;
import net.minecraft.world.item.crafting.Ingredient;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

public class AE2InscriberRecipeSerializer implements IRecipeSerializer {
    public final InscriberProcessType mode;
    public final Object inputMiddle;
    public final Object inputTop;
    public final Object inputBottom;
    public final Object result;

    public AE2InscriberRecipeSerializer(InscriberProcessType mode, Object inputMiddle, Object inputTop, Object inputBottom, Object result) {
        this.mode = mode;
        this.inputMiddle = inputMiddle;
        this.inputTop = inputTop;
        this.inputBottom = inputBottom;
        this.result = result;
    }

    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();

        if (inputBottom instanceof Ingredient) {
            return miscHelper.serializeRecipe(new InscriberRecipe(
                    miscHelper.getIngredient(inputMiddle),
                    miscHelper.getItemStack(result, 1),
                    miscHelper.getIngredient(inputTop),
                    (Ingredient) inputBottom,
                    mode
            ));
        } else {
            return miscHelper.serializeRecipe(new InscriberRecipe(
                    miscHelper.getIngredient(inputMiddle),
                    miscHelper.getItemStack(result, 1),
                    miscHelper.getIngredient(inputTop),
                    miscHelper.getIngredient(inputBottom),
                    mode
            ));
        }
    }
}
