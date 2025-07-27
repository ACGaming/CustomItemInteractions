package mod.acgaming.cii;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import mod.acgaming.cii.config.CIIConfig;
import mod.acgaming.cii.util.CIICommandSender;

@Mod.EventBusSubscriber(modid = CustomItemInteractions.MOD_ID)
@Mod(modid = CustomItemInteractions.MOD_ID, name = CustomItemInteractions.NAME, version = CustomItemInteractions.VERSION, acceptedMinecraftVersions = CustomItemInteractions.ACCEPTED_VERSIONS)
public class CustomItemInteractions
{
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String NAME = Tags.NAME;
    public static final String VERSION = Tags.VERSION;
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    private static final Map<String, CIIConfig.FluidInteractionConfig> fluidConfigs = new HashMap<>();
    private static final Map<String, CIIConfig.ItemUseInteractionConfig> useConfigs = new HashMap<>();

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        if (event.getWorld().isRemote || event.getWorld().getMinecraftServer() == null) return;

        // Get held item
        ItemStack heldItem = event.getItemStack();
        if (heldItem.isEmpty()) return;

        // Perform ray trace to check for fluid
        RayTraceResult rayTrace = rayTraceFluid(event.getEntityPlayer(), event.getWorld());
        if (rayTrace == null || rayTrace.typeOfHit != RayTraceResult.Type.BLOCK) return;

        // Check for fluid at the block position
        Fluid fluid = getFluidFromBlock(event.getWorld(), rayTrace.getBlockPos());
        if (fluid == null) return;

        // Check for applicable fluid interaction config
        String key = fluid.getName() + "_" + heldItem.getItem().getRegistryName();
        CIIConfig.FluidInteractionConfig config = fluidConfigs.get(key);

        if (config != null)
        {
            // Get output item
            EntityPlayer player = event.getEntityPlayer();
            Item outputItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(config.outputItem));
            if (outputItem != null)
            {
                ItemStack outputStack = new ItemStack(outputItem, 1);

                // Replace multiple items
                if (heldItem.getCount() > 1)
                {
                    heldItem.shrink(1);
                    if (!player.inventory.addItemStackToInventory(outputStack))
                    {
                        player.dropItem(outputStack, false);
                    }
                }
                // Replace single item
                else
                {
                    player.setHeldItem(event.getHand(), outputStack);
                }
            }

            // Execute commands
            ICommandSender commandSender = new CIICommandSender(player, rayTrace.getBlockPos());
            for (String command : config.commands)
            {
                if (!command.isEmpty())
                {
                    event.getWorld().getMinecraftServer().getCommandManager().executeCommand(commandSender, command);
                }
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onUseItem(LivingEntityUseItemEvent.Finish event)
    {
        if (event.getEntity().getEntityWorld().isRemote || event.getEntity().getServer() == null || !(event.getEntity() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntity();
        ItemStack consumedItem = event.getItem();
        if (consumedItem.isEmpty()) return;

        // Check for applicable item use interaction config
        String key = consumedItem.getItem().getRegistryName().toString();
        CIIConfig.ItemUseInteractionConfig config = useConfigs.get(key);

        if (config != null)
        {
            // Get replacement item
            Item replacementItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(config.itemReplacing));
            if (replacementItem != null)
            {
                ItemStack replacementStack = new ItemStack(replacementItem, 1);

                // Replace multiple items
                if (consumedItem.getCount() > 1)
                {
                    consumedItem.shrink(1);
                    event.setResultStack(consumedItem);

                    if (!player.inventory.addItemStackToInventory(replacementStack))
                    {
                        player.dropItem(replacementStack, false);
                    }
                }
                // Replace single item
                else
                {
                    event.setResultStack(replacementStack);
                }
            }

            // Execute commands
            ICommandSender commandSender = new CIICommandSender(player, player.getPosition());
            for (String command : config.commands)
            {
                if (!command.isEmpty())
                {
                    player.getServer().getCommandManager().executeCommand(commandSender, command);
                }
            }
        }
    }

    public static void loadConfigs()
    {
        fluidConfigs.clear();
        useConfigs.clear();

        // Load fluid interactions
        for (String key : CIIConfig.fluidInteractions)
        {
            String[] parts = key.split(",");
            if (parts.length >= 3)
            {
                String fluidName = parts[0].trim();
                String inputItem = parts[1].trim();
                String outputItem = parts[2].trim();
                String[] commands = parts.length > 3 ? parts[3].split(";") : new String[0];

                Fluid fluid = FluidRegistry.getFluid(fluidName);
                if (fluid != null)
                {
                    fluidConfigs.put(fluidName + "_" + inputItem, new CIIConfig.FluidInteractionConfig(fluid, inputItem, outputItem, commands));
                }
                else
                {
                    LOGGER.warn("Invalid fluid name: {}", fluidName);
                }
            }
        }
        LOGGER.info("Loaded {} fluid interaction(s)", fluidConfigs.size());

        // Load item use interactions
        for (String key : CIIConfig.itemUseInteractions)
        {
            String[] parts = key.split(",");
            if (parts.length >= 2)
            {
                String itemConsumed = parts[0].trim();
                String itemReplacing = parts[1].trim();
                String[] commands = parts.length > 2 ? parts[2].split(";") : new String[0];

                Item consumed = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemConsumed));
                if (consumed != null)
                {
                    useConfigs.put(itemConsumed, new CIIConfig.ItemUseInteractionConfig(itemConsumed, itemReplacing, commands));
                }
                else
                {
                    LOGGER.warn("Invalid consumed item: {}", itemConsumed);
                }
            }
        }
        LOGGER.info("Loaded {} item use interaction(s)", useConfigs.size());
    }

    private static Fluid getFluidFromBlock(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Material material = state.getMaterial();

        // Shortcut check for vanilla fluids
        if (material == Material.WATER)
        {
            return FluidRegistry.WATER;
        }
        else if (material == Material.LAVA)
        {
            return FluidRegistry.LAVA;
        }

        // Check for modded fluids
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values())
        {
            if (fluid.getBlock() != null && block == fluid.getBlock())
            {
                return fluid;
            }
        }

        return null;
    }

    private static RayTraceResult rayTraceFluid(EntityPlayer player, World world)
    {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        Vec3d start = player.getPositionEyes(1.0F);
        float cosYaw = (float) Math.cos(-yaw * 0.017453292F - (float) Math.PI);
        float sinYaw = (float) Math.sin(-yaw * 0.017453292F - (float) Math.PI);
        float cosPitch = (float) -Math.cos(-pitch * 0.017453292F);
        float sinPitch = (float) Math.sin(-pitch * 0.017453292F);
        double range = CIIConfig.interactionDistance;
        Vec3d end = start.add(sinYaw * cosPitch * range, sinPitch * range, cosYaw * cosPitch * range);

        return world.rayTraceBlocks(start, end, true, false, true);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        loadConfigs();
    }
}
