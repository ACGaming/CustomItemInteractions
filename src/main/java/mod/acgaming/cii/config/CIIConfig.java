package mod.acgaming.cii.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.acgaming.cii.CustomItemInteractions;

@Config(modid = CustomItemInteractions.MOD_ID, name = "CustomItemInteractions")
public class CIIConfig
{
    @Config.Name("Fluid Interactions")
    @Config.Comment
        ({
            "Syntax: fluid,input_item,output_item[,command1;command2;command3...]",
            "Example: water,minecraft:book,minecraft:paper,playsound block.cloth.break player @p"
        })
    public static String[] fluidInteractions = new String[] {};

    @Config.Name("Interaction Distance")
    @Config.Comment("The maximum distance in blocks a fluid can be interacted with")
    public static double interactionDistance = 5.0D;

    @Config.Name("Item Use Interactions")
    @Config.Comment
        ({
            "Syntax: input_item,output_item[,command1;command2;command3...]",
            "Example: minecraft:cooked_chicken,minecraft:bone,playsound entity.skeleton.step player @p"
        })
    public static String[] itemUseInteractions = new String[] {};

    @Mod.EventBusSubscriber(modid = CustomItemInteractions.MOD_ID)
    public static class EventHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(CustomItemInteractions.MOD_ID))
            {
                ConfigManager.sync(CustomItemInteractions.MOD_ID, Config.Type.INSTANCE);
                CustomItemInteractions.loadConfigs();
            }
        }
    }

    public static class FluidInteractionConfig
    {
        public Fluid fluid;
        public String inputItem;
        public String outputItem;
        public String[] commands;

        public FluidInteractionConfig(Fluid fluid, String inputItem, String outputItem, String[] commands)
        {
            this.fluid = fluid;
            this.inputItem = inputItem;
            this.outputItem = outputItem;
            this.commands = commands;
        }
    }

    public static class ItemUseInteractionConfig
    {
        public String itemConsumed;
        public String itemReplacing;
        public String[] commands;

        public ItemUseInteractionConfig(String itemConsumed, String itemReplacing, String[] commands)
        {
            this.itemConsumed = itemConsumed;
            this.itemReplacing = itemReplacing;
            this.commands = commands;
        }
    }
}
