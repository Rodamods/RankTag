# RankTag for DonutSMP 3.1.0 (Fabric, Minecraft 1.21.11, client) - English version

Shows a role icon before player names (including your own in third person).
- MEDIA  -> pink camera (+ cyan plus)
- MEMBER -> blue diamond
- MOD / ADMIN -> green star

No setup needed: the mod reads the TAB list and detects roles by itself.
The server's own icons are removed from the nametag to avoid overlap.

## Config screen
Default key: Right Shift (change it in Options > Controls > RankTag).
Categories: Other players, My role, On / Off.
- Other players: show the blue diamond on members.
- My role: show your own icon, with separate options for the star (Mod/Admin), camera and plus (Media), diamond (Member), and your name in third person.
- On / Off: master switch.

Your icon only shows if the TAB counts you in that role.

## Commands (optional)
- /rolenametag toggle         enable / disable the mod
- /rolenametag dump           list the TAB with the server's icons as [U+XXXX]
- /rolenametag map E012 MEDIA force an icon to a role (MEDIA | STAFF | MEMBER)
- /rolenametag reset          forget learned icons

Config file: .minecraft/config/rolenametag.json

## Getting the .jar
Option A (nothing to install): create a GitHub repository, upload this folder (including .github/workflows/build.yml),
open the Actions tab -> "build" -> download the "rolenametag-jar" artifact.
Option B (local): JDK 21 + Gradle 9, then `gradle build`; the jar is in build/libs/rolenametag-en-3.1.0.jar.
Put it in .minecraft/mods with Fabric Loader 0.18+ and Fabric API for 1.21.11.

Note: this English build uses the same mod id as the French one. Keep only ONE of them in your mods folder.
