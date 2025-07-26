package mod.acgaming.fluidinteractions.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FICommandSender implements ICommandSender
{
    private final EntityPlayer player;

    public FICommandSender(EntityPlayer player)
    {
        this.player = player;
    }

    @Override
    public String getName()
    {
        return player.getName();
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true; // Bypass permission checks
    }

    @Override
    public BlockPos getPosition()
    {
        return player.getPosition();
    }

    @Override
    public Vec3d getPositionVector()
    {
        return player.getPositionVector();
    }

    @Override
    public World getEntityWorld()
    {
        return player.getEntityWorld();
    }

    @Override
    public Entity getCommandSenderEntity()
    {
        return player;
    }

    @Override
    public boolean sendCommandFeedback()
    {
        return false; // Suppress feedback
    }

    @Override
    public MinecraftServer getServer()
    {
        return player.getServer();
    }
}
