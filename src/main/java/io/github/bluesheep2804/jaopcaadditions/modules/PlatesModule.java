package io.github.bluesheep2804.jaopcaadditions.modules;

import com.mojang.logging.LogUtils;
import io.github.bluesheep2804.jaopcaadditions.recipes.ShapedRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
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

import java.util.*;

@JAOPCAModule
public class PlatesModule implements IModule {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final JAOPCAApi api = JAOPCAApi.instance();
    private final IForm plateForm = api.newForm(this, "plates", api.itemFormType()).setMaterialTypes(MaterialType.NON_DUSTS);

    @Override
    public String getName() {
        return "additions_plates";
    }

    @Override
    public Set<MaterialType> getMaterialTypes() {
        return EnumSet.allOf(MaterialType.class);
    }

    @Override
    public List<IFormRequest> getFormRequests() {
        return Collections.singletonList(this.api.newFormRequest(this, this.plateForm));
    }

    @Override
    public void onCommonSetup(IModuleData moduleData, FMLCommonSetupEvent event) {
        IMiscHelper miscHelper = api.miscHelper();
        for (IMaterial material : plateForm.getMaterials()) {
            ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), material.getName());
            IItemInfo plateInfo = api.itemFormType().getMaterialFormInfo(plateForm, material);
            api.registerRecipe(
                    new ResourceLocation("jaopcaadditions", "plates.from_material." + material.getName()),
                    new ShapedRecipeSerializer(
                            new String[]{
                                    "M ",
                                    " M"
                            },
                            Map.of(
                                    "M", materialLocation
                            )
                            ,plateInfo,1
                    )
            );
        }
    }
}
