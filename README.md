# Shimmer Support Layer

ShimmerSupportLayer is a mod that add Shimmer compatibility to multiple mods.</br>
And simplyfies the process for adding support for any given mod.

### Mods with added compatibylity

<sup>*(This mod needs to be installed alongside shimmer and other mods)*</sup>

| Name | Description | Platforms | Links | Authors |
| --- | :---: | :---: | :---: | :---: |
| [Vanilla Minecraft](https://www.minecraft.net) | The game we all love ;)</br>*(Some support is included in Shimmer, but not full)* | All | [web](https://www.minecraft.net) | Mojang |
| [Another Furniture](https://modrinth.com/mod/another-furniture) | Vanilla-styled furniture mod. | Fabric, Forge | [![Modrinth Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Modrinth.png)](https://modrinth.com/mod/another-furniture) [![Github Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Github.png)](https://github.com/starfish-studios/AnotherFurniture) | crispytwig, Synthestra |
| [BetterEnd](https://modrinth.com/mod/betterend) | End overhaul</br>*(Though technically with native support, it does not work)*</br>*(Be carefull! Performance intensive!)* | Fabric | [![Modrinth Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Modrinth.png)](https://modrinth.com/mod/betterend) [![Github Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Github.png)](https://github.com/quiqueck/BetterEnd) | quiqueck |
| [BetterNether](https://modrinth.com/mod/betternether) | Nether update 2.0 | Fabric | [![Modrinth Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Modrinth.png)](https://modrinth.com/mod/betternether) [![Github Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Github.png)](https://github.com/quiqueck/BetterNether) | quiqueck |

</br>

### How to quickly generate shimmer support for any mod using SSL?

<sup>*Before starting you need to download SSL as a mod, SSL (mod) source code and SSL Script (script branch)*</sup></br>
Generating support using SSL can be divided into 3 major steps:

1. Run Minecraft with SSL and all the mods you want to add support for. After entering a world run command *"/generateShimmerSupport"*.
2. Copy newly created *"light_block_plus_radius.csv"* file from path *"\<minecraft instance> -> config -> shimmer -> compatibility"* to the script folder, download and copy assets folders from all mods, and default vanilla assets folder, and put them in the script folder.
3. Now run the main script using *cmd* or via opening it in an editor and running it there, copy the java part of the output from the consol to *"SupportedMods"* class in (mod) source code. Delete any duplicates, apply any edits if you want, and compile the mod.

</br>

### I want to make support generation even more automatic skipping the script step

I also need to:

- add support for cauldrons,
- fix vanilla Minecraft support not working,
- improve item source, coloured lights,
- write better colour bias function,
- add config for additional customization and applying fixes,
- add bloom support?
