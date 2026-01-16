package net.yxiao233.cdp2.api.capabilities;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.DataComponentUtil;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Optional;

@SuppressWarnings("unused")
public class BigItemStackHandler extends ItemStackHandler {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<ItemStack> CODEC = Codec.lazyInitialized(() -> {
        return RecordCodecBuilder.create((builder) -> {
            return builder.group(ItemStack.ITEM_NON_AIR_CODEC.fieldOf("id").forGetter(ItemStack::getItemHolder), ExtraCodecs.intRange(1, Integer.MAX_VALUE).fieldOf("count").orElse(1).forGetter(ItemStack::getCount), DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter((getter) -> {
                return ((PatchedDataComponentMap) getter.getComponents()).asPatch();
            })).apply(builder, ItemStack::new);
        });
    });

    private final int stackLimit;

    public BigItemStackHandler(int size, int stackLimit) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.stackLimit = stackLimit;
    }

    public BigItemStackHandler(int size){
        this(size,256);
    }


    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return this.stackLimit;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.stackLimit;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        ListTag nbtTagList = new ListTag();

        for(int i = 0; i < this.stacks.size(); ++i) {
            if (!this.stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                nbtTagList.add(save(this.stacks.get(i),provider,itemTag));
            }
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", this.stacks.size());
        return nbt;
    }
    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        this.setSize(nbt.contains("Size", 3) ? nbt.getInt("Size") : this.stacks.size());
        ListTag tagList = nbt.getList("Items", 10);

        for(int i = 0; i < tagList.size(); ++i) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            if (slot >= 0 && slot < this.stacks.size()) {
                parse(provider, itemTags).ifPresent((stack) -> {
                    this.stacks.set(slot, stack);
                });
            }
        }

        this.onLoad();
    }

    private static Optional<ItemStack> parse(HolderLookup.Provider lookupProvider, Tag tag) {
        return CODEC.parse(lookupProvider.createSerializationContext(NbtOps.INSTANCE), tag).resultOrPartial((p_330102_) -> {
            LOGGER.error("Tried to load invalid item: '{}'", p_330102_);
        });
    }

    private static Tag save(ItemStack stack, HolderLookup.Provider levelRegistryAccess, Tag outputTag) {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Cannot encode empty ItemStack");
        } else {
            return DataComponentUtil.wrapEncodingExceptions(stack, CODEC, levelRegistryAccess, outputTag);
        }
    }
}