package dev.bluesheep.jaopcaextras.recipes;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import com.google.gson.JsonElement;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

//? if >= 1.21.1 {
import net.minecraft.world.item.crafting.Ingredient;
//?} else {
/*import com.google.gson.JsonObject;
import appeng.recipes.handlers.InscriberRecipeBuilder;
import dev.bluesheep.jaopcaextras.JAOPCAExtras;
*///?}
//? if >= 26.1.2
//import java.util.Optional;

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
                getOptionalIngredient(inputTop),
                getOptionalIngredient(inputBottom),
                mode
        ));
        //?} else {
        /*JsonObject json = new JsonObject();
        json.addProperty("type", InscriberRecipe.TYPE_ID.toString());
        var builder = InscriberRecipeBuilder.inscribe(
                miscHelper.getIngredient(this.inputMiddle),
                miscHelper.getItemStack(this.result, 1).getItem(),
                1
        ).setMode(this.mode);
        builder.setTop(miscHelper.getIngredient(this.inputTop));
        if (this.mode == InscriberProcessType.PRESS) {
            builder.setBottom(miscHelper.getIngredient(this.inputBottom));
        }
        builder.save(finishedRecipe -> finishedRecipe.serializeRecipeData(json), JAOPCAExtras.rl("temp"));

        return json;
        *///?}
    }

    //? if >= 1.21.1 {
    private
    //~ if < 26.1 'Optional<Ingredient>' -> 'Ingredient'
    Ingredient
    getOptionalIngredient(Object obj) {
        if (obj == null) {
            //? if < 26.1 {
            return Ingredient.EMPTY;
            //?} else {
            /*return Optional.empty();
            *///?}
        }
        Ingredient ingredient = JAOPCAApi.instance().miscHelper().getIngredient(obj);
        //? if < 26.1 {
        return ingredient;
        //?} else {
        /*return Optional.of(ingredient);
        *///?}
    }
    //?}
}