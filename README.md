## Fluid Interactions

###### Solid functionality for liquid states!

A simple mod that applies custom functionality to right-click interactions with fluids, similar to filling a bucket with water.

- If a fluid X is right-clicked while holding item A, item A is replaced with item B
- Optional (series of) commands are executed upon the event, so sounds can be played, particles can be spawned, etc.
- Commands are executed in an OP player context, which supports every common command variable, such as @p, @a, @r, @s, etc.

Syntax: `fluid,input_item,output_item[,command1;command2;command3...]`

Example for soaking a book in water into paper and playing a sound: `water,minecraft:book,minecraft:paper,playsound minecraft:block.cloth.break player @p`

---

This mod was commissioned for Minecraft 1.12.2.