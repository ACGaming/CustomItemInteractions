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
    private final BlockPos targetPos;

    public FICommandSender(EntityPlayer player, BlockPos targetPos)
    {
        this.player = player;
        this.targetPos = targetPos;
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
        return new Vec3d(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D);
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
