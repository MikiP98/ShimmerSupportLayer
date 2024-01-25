# Shimmer Support Layer

ShimmerSupportLayer is a mod that add Shimmer compatibility to multiple mods.</br>
And simplyfies the process for adding support for any given mod.

## As of version 0.9.0 of SSL auto support for all mods can be generated using command '/generateShimmerSupport'

*Run the command and restart the game*

## Mods with added compatibylity

<sup>*(This mod needs to be installed alongside shimmer and other mods)*</sup>

### As of version 0.9.0 support is divided into 3 categories:
- Hand-made 
<sup>(Every color has been hand-picked and tweaked to look as good as possible, every item and block has beed added, or some may have been removed)</sup>
- Semi-auto <sup>(Mods that have support generated automatically, but with some manual changes)</sup>
- automatic <sup>(Automatic basic support for most of the block and some items)</sup>

### Currently there is none hand-made support for anything :(

### List of mods with SemiAutomatic support:

| Name | Description | Platforms | Links | Authors |
| --- | :---: | :---: | :---: | :---: |
| [Vanilla Minecraft](https://www.minecraft.net) | The game we all love ;)</br>*(Some support is included in Shimmer, but not full)* | All | [web](https://www.minecraft.net) | Mojang |
| [Another Furniture](https://modrinth.com/mod/another-furniture) | Vanilla-styled furniture mod. | Fabric, Forge | [![Modrinth Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Modrinth.png)](https://modrinth.com/mod/another-furniture) [![Github Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Github.png)](https://github.com/starfish-studios/AnotherFurniture) | crispytwig, Synthestra |
| [BetterEnd](https://modrinth.com/mod/betterend) | End overhaul</br>*(Though technically with native support, it does not work)*</br>*(Be carefull! Performance intensive!)* | Fabric | [![Modrinth Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Modrinth.png)](https://modrinth.com/mod/betterend) [![Github Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Github.png)](https://github.com/quiqueck/BetterEnd) | quiqueck |
| [BetterNether](https://modrinth.com/mod/betternether) | Nether update 2.0 | Fabric | [![Modrinth Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Modrinth.png)](https://modrinth.com/mod/betternether) [![Github Logo](https://raw.githubusercontent.com/TheUsefulLists/assets/main/Images/Platform_Icons/Github.png)](https://github.com/quiqueck/BetterNether) | quiqueck |

</br>

## How to quickly generate shimmer support for any mod using SSL?

1. Run Minecraft with SSL and all the mods you want to add support for. After entering a world run command *"/generateShimmerSupport"*.
2. DONE! Now restart the game and support should be added!
3. If you want to improve the result and mayby help develop better support by sharing the support on the projects github you need:
    1. Download Source code of SSL.
    2. Copy the content of *"java_code.txt"* file <sup>(located in *'{game directory} > config > shimmer > compatitbility'*)</sup> to *"SemiAutomaticSupport"* JAVA class <sup>(located in *'src > client > java > io > github > mikip98 > automation > modSupport > SemiAutomaticSupport.java'*)</sup>
    3. Change any values you want.
    4. Now if you want to share your support, create a pull request or an issue on our github :)

</br>

## Roadmap:

- add support for cauldrons,
- fix vanilla Minecraft support not working <sup>*(it is probably some issue of 'Shimmer' itself because 1 in 200 restarts it works properly)*</sup>,
- improve item source, coloured lights,
- write better colour bias function,
- add config for additional customization and applying fixes,
- add bloom support?
