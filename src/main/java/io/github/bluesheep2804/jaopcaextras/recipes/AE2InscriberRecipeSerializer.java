package io.github.bluesheep2804.jaopcaextras.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

public class AE2InscriberRecipeSerializer implements IRecipeSerializer {
    public final String mode;
    public final Object inputMiddle;
    public final Object inputTop;
    public final Object result;

    public AE2InscriberRecipeSerializer(String mode, Object inputMiddle, Object inputTop, Object result) {
        this.mode = mode;
        this.inputMiddle = inputMiddle;
        this.inputTop = inputTop;
        this.result = result;
    }

    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();
        JsonObject json = new JsonObject();
        json.addProperty("type", "ae2:inscriber");
        json.addProperty("mode", this.mode);

        JsonObject ingredientsJson = new JsonObject();
        ingredientsJson.add("middle", miscHelper.getIngredient(this.inputMiddle).toJson());
        ingredientsJson.add("top", miscHelper.getIngredient(this.inputTop).toJson());
        if (this.mode == "press") {
            Item printedSilicon = ForgeRegistries.ITEMS.getValue(new ResourceLocation("ae2:printed_silicon"));
            ingredientsJson.add("bottom", miscHelper.getIngredient(printedSilicon).toJson());
        }
        json.add("ingredients", ingredientsJson);

        json.add("result", miscHelper.getIngredient(this.result).toJson());

        return json;
    }
}
