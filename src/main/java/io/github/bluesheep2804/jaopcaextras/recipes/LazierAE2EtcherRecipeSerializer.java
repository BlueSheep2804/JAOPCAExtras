package io.github.bluesheep2804.jaopcaextras.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.recipes.IRecipeSerializer;

public class LazierAE2EtcherRecipeSerializer implements IRecipeSerializer {
    private static final Logger LOGGER = LogManager.getLogger();

    public final ResourceLocation[] input;
    public final Object output;
    public final int processTime;
    public final int energyCost;

    public LazierAE2EtcherRecipeSerializer(ResourceLocation[] input, Object output) {
        this.input = input;
        this.output = output;
        this.processTime = 120;
        this.energyCost = 1000;
    }

    public LazierAE2EtcherRecipeSerializer(ResourceLocation[] input, Object output, int processTime, int energyCost) {
        this.input = input;
        this.output = output;
        this.processTime = processTime;
        this.energyCost = energyCost;
    }

    @Override
    public JsonElement get() {
        JAOPCAApi api = JAOPCAApi.instance();
        IMiscHelper miscHelper = api.miscHelper();
        JsonObject json = new JsonObject();
        json.addProperty("type", "lazierae2:etcher");
        json.addProperty("process_time", this.processTime);
        json.addProperty("energy_cost", this.energyCost);

        JsonArray inputJson = new JsonArray();
        for (ResourceLocation item : this.input) {
            inputJson.add(miscHelper.getIngredient(item).toJson());
        }
        json.add("input", inputJson);

        json.add("output", miscHelper.getIngredient(this.output).toJson());

        LOGGER.info(json);
        return json;
    }
}
