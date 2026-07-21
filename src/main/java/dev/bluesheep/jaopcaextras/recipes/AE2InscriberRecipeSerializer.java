package dev.bluesheep.jaopcaextras.recipes;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import com.google.gson.JsonElement;
import net.minecraft.world.item.crafting.Ingredient;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

//?if < 1.21.1 {
/*import java.util.Locale;
import com.google.gson.JsonObject;
*///?}

public record AE2InscriberRecipeSerializer(
        InscriberProcessType mode,
        Object inputMiddle,
        Object inputTop,
        Object inputBottom,
        Object result
) implements IRecipeSerializer {
    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();

        //? if >= 1.21.1 {
        return miscHelper.serializeRecipe(new InscriberRecipe(
                miscHelper.getIngredient(inputMiddle),
                miscHelper.getItemStack(result, 1),
                miscHelper.getIngredient(inputTop),
                inputBottom == null ? Ingredient.EMPTY : miscHelper.getIngredient(inputBottom),
                mode
        ));
        //?} else {
        /*JsonObject json = new JsonObject();
        json.addProperty("type", "ae2:inscriber");
        json.addProperty("mode", this.mode.name().toLowerCase(Locale.ROOT));

        JsonObject ingredientsJson = new JsonObject();
        ingredientsJson.add("middle", miscHelper.getIngredient(this.inputMiddle).toJson());
        ingredientsJson.add("top", miscHelper.getIngredient(this.inputTop).toJson());
        if (this.mode == InscriberProcessType.PRESS) {
            ingredientsJson.add("bottom", miscHelper.getIngredient(this.inputBottom).toJson());
        }
        json.add("ingredients", ingredientsJson);

        json.add("result", miscHelper.getIngredient(this.result).toJson());

        return json;
        *///?}
    }
}