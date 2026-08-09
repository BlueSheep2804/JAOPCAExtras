//? if <1.21 {
/*package dev.bluesheep.jaopcaextras.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.level.material.Fluids;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipeBuilder;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

import java.util.List;

public record AdvancedAEReactionRecipeSerializer(
        Object output,
        int outputCount,
        List<Object> inputs,
        int inputCount
) implements IRecipeSerializer {
    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();
        JsonObject json = new JsonObject();
        json.addProperty("type", ReactionChamberRecipe.TYPE_ID.toString());

        var builder = ReactionChamberRecipeBuilder.react(miscHelper.getItemStack(output, outputCount), 20000);
        builder.fluid(Fluids.WATER, 100);
        inputs.forEach(it -> builder.input(miscHelper.getItemStack(it, inputCount).getItem(), inputCount));

        builder.save(finishedRecipe -> finishedRecipe.serializeRecipeData(json), "temp");
        return json;
    }
}
*///?}
