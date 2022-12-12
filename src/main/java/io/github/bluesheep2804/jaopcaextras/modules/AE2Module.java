package io.github.bluesheep2804.jaopcaextras.modules;

import com.mojang.logging.LogUtils;
import io.github.bluesheep2804.jaopcaextras.recipes.AE2InscriberRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

import static io.github.bluesheep2804.jaopcaextras.init.ItemInit.EXTRA_PRESS;

@JAOPCAModule(modDependencies = "ae2")
public class AE2Module implements IModule {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final JAOPCAApi api = JAOPCAApi.instance();
    private static final Set<String> BLACKLIST = new TreeSet<>(Arrays.asList(
            "brick", "nether_brick",
            "prismarine", "sulfur",
            "diamond", "certus_quartz", "gold", "resonating"
    ));
    private final IForm processorForm = api.newForm(this, "processors", api.itemFormType()).setMaterialTypes(MaterialType.NON_DUSTS).setDefaultMaterialBlacklist(BLACKLIST);
    private final IForm circuitForm = api.newForm(this, "circuits", api.itemFormType()).setMaterialTypes(MaterialType.NON_DUSTS).setDefaultMaterialBlacklist(BLACKLIST);

    @Override
    public String getName() {
        return "extras_ae2";
    }

    @Override
    public Set<MaterialType> getMaterialTypes() {
        return EnumSet.allOf(MaterialType.class);
    }

    @Override
    public List<IFormRequest> getFormRequests() {
        return Collections.singletonList(this.api.newFormRequest(this, this.circuitForm, this.processorForm));
    }

    @Override
    public void onCommonSetup(IModuleData moduleData, FMLCommonSetupEvent event) {
        IMiscHelper miscHelper = api.miscHelper();
        Item press = EXTRA_PRESS.get();

        for (IMaterial material : circuitForm.getMaterials()) {
            String name = material.getName();
            if (!BLACKLIST.contains(name)) {
                ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), name);
                IItemInfo circuitInfo = api.itemFormType().getMaterialFormInfo(circuitForm, material);
                api.registerRecipe(
                        new ResourceLocation("jaopcaextras", "inscriber.circuit" + material.getName()),
                        new AE2InscriberRecipeSerializer(
                                "inscribe",
                                materialLocation,
                                press,
                                circuitInfo
                        )
                );
            }
        }

        for (IMaterial material : processorForm.getMaterials()) {
            String name = material.getName();
            if (!BLACKLIST.contains(name)) {
                ResourceLocation circuitLocation = miscHelper.getTagLocation("circuits", material.getName());
                IItemInfo processorInfo = api.itemFormType().getMaterialFormInfo(processorForm, material);
                Item redstone = Items.REDSTONE;
                api.registerRecipe(
                        new ResourceLocation("jaopcaextras", "inscriber.processor." + material.getName()),
                        new AE2InscriberRecipeSerializer(
                                "press",
                                redstone,
                                circuitLocation,
                                processorInfo
                        )
                );
            }
        }
    }
}
