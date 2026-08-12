# JAOPCA Extras
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/679287?style=for-the-badge&logo=curseforge&color=F16436)](https://curseforge.com/minecraft/mc-mods/jaopca-extras)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/lev3YGBh?style=for-the-badge&logo=modrinth&color=00AF5C)](https://modrinth.com/mod/jaopca-extras)

[![GitHub License](https://img.shields.io/github/license/BlueSheep2804/ToggleVisualize?style=for-the-badge)](https://github.com/BlueSheep2804/ToggleVisualize/blob/main/LICENSE)
![Latest version](https://img.shields.io/badge/dynamic/regex?url=https%3A%2F%2Fraw.githubusercontent.com%2FBlueSheep2804%2FJAOPCAExtras%2Frefs%2Fheads%2Fmain%2Fbuild.gradle.kts&search=%20%2Aconst%20val%20mod_version%20%3D%20%22(.*)%22&replace=%241&style=for-the-badge&label=Latest%20version)

This mod adds various modules to JAOPCA.  
JAOPCA adds modules (described below) corresponding to each material.

## Modules
The following modules are currently in place.
### Vanilla
- Plates
- Rods
- Gears

### AE2
- Processors
- Circuits

## Configs
It is generated in config/jaopca/modules/.
- `extras_gears.toml`
- `extras_plates.toml`
- `extras_rods.toml`
- `extras_ae2.toml`(If AE2 is loaded)

## ModPacks
**Feel free to use it.**  
If you want to change the name of each item added by JAOPCA, change lang/*.json as follows:
```json
{
    "item.jaopca.{module_name}.{material_name}": "Something",
    "item.jaopca.rods.netherite": "Magical Rod",
    "item.jaopca.processors.iron": "Basic Processor"
}
```
