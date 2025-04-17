package io.github.bluesheep2804.jaopcaextras.modules;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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

import java.util.*;

@JAOPCAModule
public class GearsModule implements IModule {
    private final JAOPCAApi api = JAOPCAApi.instance();
    private final IForm gearForm = api.newForm(this, "gears", api.itemFormType()).setMaterialTypes(MaterialType.INGOTS);

    @Override
    public String getName() {
        return "extras_gears";
    }

    @Override
    public Set<MaterialType> getMaterialTypes() {
        return EnumSet.allOf(MaterialType.class);
    }

    @Override
    public List<IFormRequest> getFormRequests() {
        return Collections.singletonList(this.api.newFormRequest(this, this.gearForm));
    }

    @Override
    public void onCommonSetup(IModuleData moduleData, FMLCommonSetupEvent event) {
        IMiscHelper miscHelper = api.miscHelper();
        for (IMaterial material : gearForm.getMaterials()) {
            ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), material.getName());
            IItemInfo gearInfo = api.itemFormType().getMaterialFormInfo(gearForm, material);

            api.registerRecipe(
                    ResourceLocation.fromNamespaceAndPath("jaopcaextras", "gears.from_material." + material.getName()),
                    new ShapedRecipeSerializer(
                            ResourceLocation.fromNamespaceAndPath("jaopcaextras", "gears.from_material." + material.getName()),
                            gearInfo,
                            1,
                            new String[] {
                                    " M ",
                                    "M M",
                                    " M "
                            },
                            'M', materialLocation
                    )
            );
        }
    }
}
