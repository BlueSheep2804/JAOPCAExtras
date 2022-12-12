package io.github.bluesheep2804.jaopcaextras.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

import java.util.Map;

public class ShapedRecipeSerializer implements IRecipeSerializer {
    public final String[] pattern;
    public final Map<String, ResourceLocation> key;
    public final Object output;
    public final Integer count;

    public ShapedRecipeSerializer(String[] pattern, Map<String, ResourceLocation> key, Object output, Integer count) {
        this.pattern = pattern;
        this.key = key;
        this.output = output;
        this.count = count;
    }

    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");

        JsonArray patternJson = new JsonArray();
        for (String str : this.pattern) {
            patternJson.add(str);
        }
        json.add("pattern", patternJson);

        JsonObject keyJson = new JsonObject();
        for (Map.Entry<String, ResourceLocation> entry : this.key.entrySet()) {
            keyJson.add(String.valueOf(entry.getKey()), miscHelper.getIngredient(entry.getValue()).toJson());
        }
        json.add("key", keyJson);

        ItemStack stack = miscHelper.getItemStack(this.output, this.count);
        json.add("result", miscHelper.serializeItemStack(stack));

        return json;
    }
}
