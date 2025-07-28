## Custom Item Interactions

###### More interactive than ever!

A simple mod that applies custom functionality to right-click interactions with fluids, similar to filling a bucket with water, and using items, such as eating and drinking.

### 📖 About

Have you ever wanted to make a custom mug that fills with water for beer when you right click water? Or filling a bowl with lava? Now you can! With existing items (or creating new ones with ContentTweaker), you can not only set interactions with liquids, you can also set interactions with consuming food items!

- Want a mug of beer to leave behind an empty mug like the vanilla glass bottle? Done!
- Want a bandage-like food item created in ContentTweaker to leave behind threads upon use? Easy!
- Want to leave bones behind after eating a cooked chicken? See the config example!

The config can be updated in-game through the Forge Mod Options menu for real-time testing.

Both fluid and item use interactions allow for optional commands to be run post-interaction, allowing you to make particles, play sounds, grant advancements/quests, etc.

Mods that auto-sort inventories may have issues. In the case of something like Inventory Tweaks, set `enableAutoRefill=false`.

### 🫗 Fluid Interactions

- If a fluid X is right-clicked while holding item A, item A is replaced with item B
- Optional (series of) commands are executed upon the event, so sounds can be played, particles can be spawned, etc.
- Commands are executed in an OP player context, which supports every common command variable, such as @p, @a, @r, @s, etc.

Syntax: `fluid,input_item,output_item[,command1;command2;command3...]`

Example for soaking a book in water into paper and playing a sound: `water,minecraft:book,minecraft:paper,playsound block.cloth.break player @p`

### 🚮 Item Use Interactions

- If a consumable item A is used up, item A is replaced with item B
- Optional (series of) commands are executed upon the event, so sounds can be played, particles can be spawned, etc.
- Commands are executed in an OP player context, which supports every common command variable, such as @p, @a, @r, @s, etc.

Syntax: `input_item,output_item[,command1;command2;command3...]`

Example for returning bones when eating cooked chicken: `minecraft:cooked_chicken,minecraft:bone,playsound entity.skeleton.step player @p`

---

This mod was commissioned by Ski_ for Minecraft 1.12.2.