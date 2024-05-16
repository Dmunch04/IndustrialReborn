package me.munchii.industrialreborn.blockentity;

import me.munchii.industrialreborn.config.IndustrialRebornConfig;
import me.munchii.industrialreborn.init.IRBlockEntities;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRFluids;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import reborncore.common.blockentity.MachineBaseBlockEntity;
import reborncore.common.blockentity.RedstoneConfiguration;
import reborncore.common.blocks.BlockMachineBase;
import reborncore.common.fluid.FluidValue;
import reborncore.common.screen.BuiltScreenHandler;
import reborncore.common.screen.BuiltScreenHandlerProvider;
import reborncore.common.screen.builder.ScreenHandlerBuilder;
import reborncore.common.util.RebornInventory;
import reborncore.common.util.Tank;
import techreborn.blockentity.machine.GenericMachineBlockEntity;
import techreborn.config.TechRebornConfig;

public class AutoEnchanterBlockEntity extends GenericMachineBlockEntity implements BuiltScreenHandlerProvider {
    public int enchantTime = 0;
    public int totalEnchantTime = IndustrialRebornConfig.autoEnchanterTicksPerEnchant;

    public final Tank experienceTank;

    public AutoEnchanterBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntities.AUTO_ENCHANTER, pos, state, "AutoEnchanter", IndustrialRebornConfig.autoEnchanterMaxInput, IndustrialRebornConfig.autoEnchanterMaxEnergy, IRContent.Machine.AUTO_ENCHANTER.block, 4);
        this.inventory = new RebornInventory<>(5, "AnimalFeederBlockEntity", 64, this);

        this.experienceTank = new Tank("AutoEnchanterBlockEntity", FluidValue.BUCKET.multiply(32), this);
        experienceTank.setFluid(IRFluids.LIQUID_EXPERIENCE.getFluid());
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, MachineBaseBlockEntity blockEntity) {
        super.tick(world, pos, state, blockEntity);

        if (!(world instanceof ServerWorld) || !isActive(RedstoneConfiguration.POWER_IO)) {
            return;
        }

        updateState();

        if (getStored() > IndustrialRebornConfig.autoEnchanterEnergyPerEnchant && experienceTank.getFluidAmount().equalOrMoreThan(FluidValue.fromMillibuckets(IndustrialRebornConfig.autoEnchanterExperiencePerEnchant)) && inventory.getStack(0).isEnchantable()) {
            if (enchantTime >= totalEnchantTime) {
                boolean didEnchant = enchant();
                if (didEnchant) {
                    useEnergy(IndustrialRebornConfig.autoEnchanterEnergyPerEnchant);
                }
                enchantTime = 0;
            } else {
                enchantTime += (int) Math.round(getSpeedMultiplier() / TechRebornConfig.overclockerSpeed) + 1;
            }
        }
    }

    private boolean enchant() {
        if (!inventory.getStack(0).isEmpty() && inventory.getStack(1).isEmpty()) {
            ItemStack toEnchant = inventory.getStack(0);
            if (!toEnchant.isEnchantable()) {
                return false;
            }

            // TODO: Make tag to blacklist certain enchantments
            FluidValue experienceValue = FluidValue.fromMillibuckets(IndustrialRebornConfig.autoEnchanterExperiencePerEnchant);
            EnchantmentHelper.enchant(world.getRandom(), toEnchant, IndustrialRebornConfig.autoEnchanterExperiencePerEnchant / 1000, IndustrialRebornConfig.autoEnchanterAllowTreasureEnchantments);

            experienceTank.setFluidAmount(experienceTank.getFluidAmount().subtract(experienceValue));
            inventory.setStack(1, toEnchant);
            inventory.setStack(0, ItemStack.EMPTY);

            return true;
        }

        return false;
    }

    private void updateState() {
        assert world != null;

        final BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof final BlockMachineBase blockMachineBase) {
            boolean active = experienceTank.getFluidAmount().equalOrMoreThan(FluidValue.fromMillibuckets(IndustrialRebornConfig.autoEnchanterExperiencePerEnchant))
                    && getStored() > IndustrialRebornConfig.autoEnchanterEnergyPerEnchant
                    && inventory.getStack(0).isEnchantable();
            if (blockState.get(BlockMachineBase.ACTIVE) != active) {
                blockMachineBase.setActive(active, world, pos);
            }
        }
    }

    @Override
    public ItemStack getToolDrop(PlayerEntity p0) {
        return IRContent.Machine.AUTO_ENCHANTER.getStack();
    }

    @Override
    @NotNull
    public Tank getTank() {
        return experienceTank;
    }

    @Override
    public BuiltScreenHandler createScreenHandler(int syncID, PlayerEntity player) {
        return new ScreenHandlerBuilder("auto_enchanter").player(player.getInventory()).inventory().hotbar().addInventory().blockEntity(this)
                .energySlot(4, 8, 72)
                .fluidSlot(2, 34, 35)
                .outputSlot(3, 34, 55)
                .slot(0, 84, 43, ItemStack::isEnchantable)
                .outputSlot(1, 126, 43)
                .syncEnergyValue()
                .sync(this::getExperienceAmount, this::setExperienceAmount)
                .sync(this::getEnchantTime, this::setEnchantTime)
                .sync(this::getTotalEnchantTime, this::setTotalEnchantTime)
                .sync(experienceTank)
                .addInventory().create(this, syncID);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        getTank().read(tag);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        getTank().write(tag);
    }

    public FluidValue getExperienceAmount() {
        return experienceTank.getFluidAmount();
    }

    public void setExperienceAmount(FluidValue amount) {
        experienceTank.setFluidAmount(amount);
    }

    public int getEnchantTime() {
        return enchantTime;
    }

    public void setEnchantTime(int enchantTime) {
        this.enchantTime = enchantTime;
    }

    public int getTotalEnchantTime() {
        return totalEnchantTime;
    }

    public void setTotalEnchantTime(final int totalEnchantTime) {
        this.totalEnchantTime = totalEnchantTime;
    }
}
