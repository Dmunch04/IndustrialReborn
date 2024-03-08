package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.MultiblockWriter;
import reborncore.common.fluid.FluidValue;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;

import java.util.List;

public class MobSlaughterBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
    public int slaughterTime = 0;
    public int totalSlaughterTime = IndustrialRebornConfig.mobSlaughterTicksPerSlaughter;

    public final int slaughterRadius = IndustrialRebornConfig.mobSlaughterRadius;

    public final Tank experienceTank;

    private BlockPos centerPos;

    public MobSlaughterBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.MOB_SLAUGHTER, pos, state, "MobSlaughter", IndustrialRebornConfig.mobSlaughterMaxInput, IndustrialRebornConfig.mobSlaughterMaxEnergy, IRContent.Machine.MOB_SLAUGHTER.block, 6);
        this.inventory = new RebornInventory<>(7, "MobSlaughterBlockEntity", 64, this);

        this.experienceTank = new Tank("MobSlaughterBlockEntity", FluidValue.BUCKET.multiply(16), this);
        experienceTank.setFluid(IRFluids.LIQUID_EXPERIENCE.getFluid());

        experienceTank.setFluidAmount(FluidValue.BUCKET.multiply(5));
    }

    @Override
    @NotNull
    public Tank getTank() {
        return experienceTank;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (centerPos == null) {
            centerPos = pos.offset(getFacing().getOpposite(), slaughterRadius + 1);
        }

        ServerWorld serverWorld = (ServerWorld) world;
        List<MobEntity> nearbyEntities = serverWorld.getEntitiesByClass(MobEntity.class, new Box(
                centerPos.getX() - slaughterRadius,
                centerPos.getY(),
                centerPos.getZ() - slaughterRadius,
                centerPos.getX() + slaughterRadius,
                centerPos.getY(),
                centerPos.getZ() + slaughterRadius
        ), entity -> true);

        if (!nearbyEntities.isEmpty()) {
            MobEntity entity = nearbyEntities.get(0);
            float currentHealth = entity.getHealth();
            entity.remove(Entity.RemovalReason.KILLED);
            if (!entity.isAlive()) {
                
            }
        }
    }

    @Override
    public ItemStack getToolDrop(PlayerEntity p0) {
        return IRContent.Machine.MOB_SLAUGHTER.getStack();
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("mob_slaughter").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .energySlot(6, 8, 72)
                .outputSlot(0, 70, 22).outputSlot(1, 88, 22)
                .outputSlot(2, 70, 40).outputSlot(3, 88, 40)
                .outputSlot(4, 70, 58).outputSlot(5, 88, 58)
                .syncEnergyValue()
                .sync(this::getExperienceAmount, this::setExperienceAmount)
                .sync(experienceTank)
                .addInventory().create(this, syncID);
    }

    @Override
    public void writeMultiblock(MultiblockWriter writer) {
        // TODO: multiblock doesn't draw for some reason
        final BlockState head = Blocks.PLAYER_HEAD.getDefaultState();

        final int diameter = slaughterRadius * 2 + 1;
        for (int i = 1; i <= diameter; i++) {
            for (int j = -slaughterRadius; j <= slaughterRadius; j++) {
                writer.add(i, 0, j, (world, pos) -> true, head);
            }
        }
    }

    public FluidValue getExperienceAmount() {
        return experienceTank.getFluidAmount();
    }

    public void setExperienceAmount(FluidValue amount) {
        experienceTank.setFluidAmount(amount);
    }

    private static class BoundingBox {
        private final int minX;
        private final int maxX;
        private final int y;
        private final int minZ;
        private final int maxZ;

        public BoundingBox(BlockPos center, int radius) {
            final int diameter = radius * 2 + 1;
            BlockPos corner1 = center.add(-radius, 0, -radius);
            BlockPos corner2 = center.add(radius, 0, radius);

            minX = corner1.getX();
            maxX = corner2.getX();
            y = center.getY();
            minZ = corner1.getZ();
            maxZ = corner2.getZ();
        }

        public boolean collides(BlockPos pos) {
            return pos.getX() >= minX
                    && pos.getX() <= maxX
                    && pos.getY() == y
                    && pos.getZ() >= minZ
                    && pos.getZ() <= maxZ;
        }
    }
}
