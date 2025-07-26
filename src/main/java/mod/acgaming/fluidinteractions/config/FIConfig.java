package mod.acgaming.fluidinteractions.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.acgaming.fluidinteractions.FluidInteractions;

@Config(modid = FluidInteractions.MOD_ID)
public class FIConfig
{
    @Config.Name("Fluid Interactions")
    @Config.Comment
        ({
            "Syntax: fluid,input_item,output_item[,command1;command2;command3...]",
            "Example: water,minecraft:book,minecraft:paper,playsound minecraft:block.cloth.break player @p"
        })
    public static String[] fluidInteractions = new String[] {};

    @Config.Name("Interaction Distance")
    @Config.Comment("The maximum distance in blocks a fluid can be interacted with")
    public static double interactionDistance = 5.0D;

    @Mod.EventBusSubscriber(modid = FluidInteractions.MOD_ID)
    public static class EventHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(FluidInteractions.MOD_ID))
            {
                ConfigManager.sync(FluidInteractions.MOD_ID, Config.Type.INSTANCE);
                FluidInteractions.loadConfigs();
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
}
