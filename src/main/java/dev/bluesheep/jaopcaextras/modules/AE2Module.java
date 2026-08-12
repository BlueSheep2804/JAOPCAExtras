package dev.bluesheep.jaopcaextras.modules;

import appeng.recipes.handlers.InscriberProcessType;
import dev.bluesheep.jaopcaextras.JAOPCAExtras;
import dev.bluesheep.jaopcaextras.JAOPCAExtrasItems;
import dev.bluesheep.jaopcaextras.IdentifierWrapper;
import dev.bluesheep.jaopcaextras.recipes.AE2InscriberRecipeSerializer;
import dev.bluesheep.jaopcaextras.recipes.extendedae.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;
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

import java.util.*;

//? if forge {
/*import net.neoforged.neoforge.registries.ForgeRegistries;
 *///?} else {
import net.minecraft.core.registries.BuiltInRegistries;
//?}

//? if < 1.21
//import dev.bluesheep.jaopcaextras.recipes.AdvancedAEReactionRecipeSerializer;

@JAOPCAModule(modDependencies = "ae2")
public class AE2Module implements IModule {
    private final JAOPCAApi api = JAOPCAApi.instance();
    private static final Set<String> BLACKLIST = new TreeSet<>(Arrays.asList(
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
        Item press = JAOPCAExtrasItems.EXTRA_PRESS.get();

        for (IMaterial material : circuitForm.getMaterials()) {
            String name = material.getName();
            if (!BLACKLIST.contains(name)) {
                ResourceLocation materialLocation = miscHelper.getTagLocation(material.getType().getFormName(), name);
                IItemInfo circuitInfo = api.itemFormType().getMaterialFormInfo(circuitForm, material);
                api.registerRecipe(
                        JAOPCAExtras.rl("inscriber.circuit." + material.getName()),
                        new AE2InscriberRecipeSerializer(
                                InscriberProcessType.INSCRIBE,
                                materialLocation,
                                press,
                                null,
                                circuitInfo
                        )
                );

                if (ModList.get().isLoaded("extendedae")) {
                    ResourceLocation materialBlockLocation = miscHelper.getTagLocation("storage_blocks", name);
                    ResourceLocation circuitCutterRecipeLocation = JAOPCAExtras.rl("circuit_cutter.circuit." + material.getName());
                    api.registerRecipe(
                            circuitCutterRecipeLocation,
                            new CircuitCutterRecipeSerializer(
                                    circuitCutterRecipeLocation,
                                    circuitInfo,
                                    material.isSmallStorageBlock() ? 4 : 9,
                                    materialBlockLocation
                            )
                    );
                }
            }
        }

        ResourceLocation redstone = miscHelper.getTagLocation("dusts", "redstone");
        Item printedSilicon = getItem(IdentifierWrapper.fromNamespaceAndPath("ae2", "printed_silicon"));

        for (IMaterial material : processorForm.getMaterials()) {
            String name = material.getName();
            if (!BLACKLIST.contains(name)) {
                ResourceLocation circuitLocation = miscHelper.getTagLocation("circuits", material.getName());
                IItemInfo processorInfo = api.itemFormType().getMaterialFormInfo(processorForm, material);
                api.registerRecipe(
                        JAOPCAExtras.rl("inscriber.processor." + material.getName()),
                        new AE2InscriberRecipeSerializer(
                                InscriberProcessType.PRESS,
                                redstone,
                                circuitLocation,
                                printedSilicon,
                                processorInfo
                        )
                );

                //? if >= 1.21.1 {
                if (ModList.get().isLoaded("extendedae")) {
                    api.registerRecipe(
                            JAOPCAExtras.rl("crystal_assembler.processor." + material.getName()),
                            new CrystalAssemblerRecipeSerializer(
                                    processorInfo, 4,
                                    List.of(circuitLocation, printedSilicon, redstone), 4
                            )
                    );
                }
                //?} else {
                /*if (ModList.get().isLoaded("advancedae")) {
                    api.registerRecipe(
                            JAOPCAExtras.rl("reaction_chamber.processor." + material.getName()),
                            new AdvancedAEReactionRecipeSerializer(
                                    processorInfo, 4,
                                    List.of(circuitLocation, printedSilicon, redstone), 4
                            )
                    );
                }
                *///?}
            }
        }
    }

    private Item getItem(ResourceLocation resourceLocation) {
        //~ if neoforge 'ForgeRegistries.ITEMS.getValue' -> 'BuiltInRegistries.ITEM.get'
        var item = BuiltInRegistries.ITEM.get(resourceLocation);
        //? if < 26.1 {
        return item;
        //?} else {
        /*return item.get().value();
        *///?}
    }
}
