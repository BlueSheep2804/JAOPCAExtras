package dev.bluesheep.jaopcaextras.modules;

import dev.bluesheep.jaopcaextras.JAOPCAExtras;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.forms.IForm;
import thelm.jaopca.api.forms.IFormRequest;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.items.IItemInfo;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.api.materials.MaterialType;
import thelm.jaopca.api.modules.IModule;
import thelm.jaopca.api.modules.IModuleData;
import thelm.jaopca.api.modules.JAOPCAModule;
import thelm.jaopca.recipes.ShapedRecipeSerializer;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@JAOPCAModule
public class RodsModule implements IModule {
    private final JAOPCAApi api = JAOPCAApi.instance();
    private final IForm rodForm = api.newForm(this, "rods", api.itemFormType())
            .setMaterialTypes(MaterialType.NON_DUSTS);

    @Override
    public String getName() {
        return "extras_rods";
    }

    @Override
    public Set<MaterialType> getMaterialTypes() {
        return EnumSet.allOf(MaterialType.class);
    }

    @Override
    public List<IFormRequest> getFormRequests() {
        return Collections.singletonList(this.api.newFormRequest(this, this.rodForm));
    }

    @Override
    public void onCommonSetup(IModuleData moduleData, FMLCommonSetupEvent event) {
        IMiscHelper miscHelper = api.miscHelper();
        for (IMaterial material : rodForm.getMaterials()) {
            ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), material.getName());
            IItemInfo gearInfo = api.itemFormType().getMaterialFormInfo(rodForm, material);
            ResourceLocation recipeLocation = JAOPCAExtras.rl("rods.from_material." + material.getName());

            api.registerRecipe(
                    recipeLocation,
                    new ShapedRecipeSerializer(
                            recipeLocation,
                            gearInfo,
                            4,
                            new String[] {
                                    "M",
                                    "M"
                            },
                            'M', materialLocation
                    )
            );
        }
    }
}
