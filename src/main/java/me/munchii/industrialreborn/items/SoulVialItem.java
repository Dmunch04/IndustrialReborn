package me.munchii.industrialreborn.items;

import me.munchii.industrialreborn.IndustrialReborn;
import me.munchii.industrialreborn.core.store.item.StoreItem;
import me.munchii.industrialreborn.core.store.StoreProvider;
import me.munchii.industrialreborn.init.IRContent;
import me.munchii.industrialreborn.init.IRStores;
import me.munchii.industrialreborn.store.test.ITestStore;
import me.munchii.industrialreborn.store.test.TestStoreItemStack;
import me.munchii.industrialreborn.store.test2.Test2StoreItemStack;
import me.munchii.industrialreborn.utils.EntityCaptureUtils;
import me.munchii.industrialreborn.utils.EntityStorageNBTHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SoulVialItem extends StoreItem {

    public SoulVialItem(boolean filled) {
        super(new Item.Settings().maxCount(filled ? 1 : 16));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient) {
            return ActionResult.FAIL;
        }

        PlayerEntity player = context.getPlayer();

        if (player == null) {
            return ActionResult.FAIL;
        }

        ItemStack oStack = stack;
        this.stack = context.getStack();
        if (hasStoredEntity()) {
            IndustrialReborn.LOGGER.warn("BBBBBBBBB1");
            getStore(IRStores.TEST_STORE).ifPresent(store -> {
                IndustrialReborn.LOGGER.warn("BBBBBBBBB2");
                IndustrialReborn.LOGGER.info("AAAAAAAAAAAAAAAAAA " + store.getStoredString());
            });
        } else {
            IndustrialReborn.LOGGER.warn("BBBBBBBBB3");
            getStore(IRStores.TEST_STORE).ifPresent(store -> {
                IndustrialReborn.LOGGER.warn("BBBBBBBBB4");
                store.setStoredString("YEEEET");
            });
        }
        this.stack = oStack;

        return releaseEntity(context.getWorld(), context.getStack(), context.getBlockPos(), emptyVial -> player.setStackInHand(context.getHand(), emptyVial));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.getWorld().isClient) {
            return ActionResult.FAIL;
        }

        Optional<ItemStack> itemStack = catchEntity(stack, entity);
        if (itemStack.isPresent()) {
            ItemStack filledVial = itemStack.get();
            ItemStack handStack = user.getStackInHand(hand);
            if (handStack.isEmpty()) {
                handStack.setCount(1);
                user.setStackInHand(hand, filledVial);
            } else {
                if (!user.giveItemStack(filledVial)) {
                    user.dropItem(filledVial, false);
                }
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    private static Optional<ItemStack> catchEntity(ItemStack soulVial, LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            return Optional.empty();
        }

        //noinspection unchecked
        EntityCaptureUtils.CapturableStatus capturableStatus = EntityCaptureUtils.getCapturableStatus((EntityType<? extends LivingEntity>) entity.getType(), entity);
        if (capturableStatus != EntityCaptureUtils.CapturableStatus.CAPTURABLE) {
            return Optional.empty();
        }

        if (!entity.isAlive()) {
            return Optional.empty();
        }

        if (entity instanceof MobEntity mob && mob.isLeashed()) {
            mob.detachLeash(true, true);
        }

        soulVial.decrement(1);
        ItemStack filledVial = IRContent.FILLED_SOUL_VIAL.getDefaultStack();
        EntityStorageNBTHelper.saveEntityData(filledVial, entity);

        entity.discard();
        return Optional.of(filledVial);
    }

    private static ActionResult releaseEntity(World world, ItemStack filledVial, BlockPos pos, Consumer<ItemStack> emptyVialSetter) {
        if (EntityStorageNBTHelper.hasStoredEntity(filledVial)) {
            Optional<NbtCompound> entityTag = EntityStorageNBTHelper.getEntityDataCompound(filledVial);
            if (entityTag.isEmpty()) {
                return ActionResult.FAIL;
            }

            double spawnX = pos.getX();
            double spawnY = pos.getY() + 0.5;
            double spawnZ = pos.getZ();

            Optional<Entity> entity = EntityType.getEntityFromNbt(entityTag.get(), world);

            entity.ifPresent(ent -> {
                ent.setPos(spawnX, spawnY, spawnZ);
                ent.applyRotation(BlockRotation.random(world.getRandom()));
                world.spawnEntity(ent);
            });

            emptyVialSetter.accept(IRContent.EMPTY_SOUL_VIAL.getDefaultStack());
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.getItem() == IRContent.FILLED_SOUL_VIAL.asItem() && EntityStorageNBTHelper.hasStoredEntity(stack)) {
            Optional<NbtCompound> tag = EntityStorageNBTHelper.getEntityDataCompound(stack);
            tag.ifPresent(nbtCompound -> {
                Optional<Entity> entity = EntityType.getEntityFromNbt(nbtCompound, world);
                entity.ifPresent(ent -> tooltip.add(Text.translatable("item.industrialreborn.filled_soul_vial.tooltip", ent.getDisplayName().getString()).formatted(Formatting.GRAY)));
            });
        }
    }

    ItemStack stack;

    @Override
    public void initStores(StoreProvider provider) {
        // i dont think this will work because we get the default stack, instead of the current stack?
        // maybe we should "copy" the Forge capability way and pass around an ItemStack
        // unsure of where it would come from: public void initStores(ItemStack stack, StoreProvider provider)
        // where is the stack coming from. plus we cant do stack.getStore, we have to attach that function to this item class
        // so maybe make an item store instead?
        // - okay yes. it seems that using the default stack will not work. i tested it by having 2 vials. the first i right-clicked 2 times
        // to first store the string and then print. and the second i only right-clicked once. if it had worked, it should only
        // have printed "YEEEET" once, however it printed it 3 times (not sure why 3 times, should only be 2, but still indicates it doesn't work)
        //provider.addStore(IRStores.TEST_STORE, Optional.of(new TestStoreItemStack(getDefaultStack())));
        stack = new ItemStack(asItem());
        provider.addStore(IRStores.TEST_STORE, Optional.of(new TestStoreItemStack(stack)));
        provider.addStore(IRStores.TEST_2_STORE, Optional.of(new Test2StoreItemStack()));
        /*
        if (IRContent.FILLED_SOUL_VIAL != null) {
            provider.addStore(IRStores.TEST_STORE, Optional.of(new TestStoreItemStack(getDefaultStack())));
        }

         */
        /*
        if (this == IRContent.FILLED_SOUL_VIAL.asItem()) {
            provider.addStore(IRStores.TEST_STORE, Optional.of(new TestStoreItemStack(getDefaultStack())));
        }

         */
    }

    public boolean hasStoredEntity() {
        return getStore(IRStores.TEST_STORE).map(ITestStore::hasStoredString).orElse(false);
        // ??
        //return getStore(IRStores.TEST_2_STORE).map(ITest2Store::hasStoredString).orElse(false);
    }
}
