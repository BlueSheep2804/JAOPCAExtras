package dev.bluesheep.jaopcaextras;

import net.minecraft.resources.ResourceLocation;

public class ResourceLocationWrapper {
    public static ResourceLocation fromNamespaceAndPath(String namespace, String path) {
        //? if >= 1.21 {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
        //?} else {
        /*return new ResourceLocation(namespace, path);
        *///?}
    }

    public static ResourceLocation withDefaultNamespace(String path) {
        //? if >= 1.21 {
        return ResourceLocation.withDefaultNamespace(path);
        //?} else {
        /*return new ResourceLocation(path);
        *///?}
    }
}
