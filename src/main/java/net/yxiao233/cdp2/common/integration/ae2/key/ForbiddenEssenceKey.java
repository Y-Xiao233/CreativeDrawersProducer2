package net.yxiao233.cdp2.common.integration.ae2.key;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.mojang.serialization.MapCodec;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.List;

public class ForbiddenEssenceKey extends AEKey {
    public static final ResourceLocation TYPE_ID = CreativeDrawersProducer2.makeId("forbidden_essence");
    public static final ResourceLocation BLOOD_ID = CreativeDrawersProducer2.makeId("forbidden_blood_essence");
    public static final ResourceLocation SOULS_ID = CreativeDrawersProducer2.makeId("forbidden_souls_essence");
    public static final ResourceLocation EXPERIENCE_ID = CreativeDrawersProducer2.makeId("forbidden_experience_essence");
    public static final ResourceLocation AUREAL_ID = CreativeDrawersProducer2.makeId("forbidden_aureal_essence");
    public static final ForbiddenEssenceKey BLOOD = new ForbiddenEssenceKey(EssenceType.BLOOD);
    public static final ForbiddenEssenceKey SOULS = new ForbiddenEssenceKey(EssenceType.SOULS);
    public static final ForbiddenEssenceKey EXPERIENCE = new ForbiddenEssenceKey(EssenceType.EXPERIENCE);
    public static final ForbiddenEssenceKey AUREAL = new ForbiddenEssenceKey(EssenceType.AUREAL);
    public static final MapCodec<ForbiddenEssenceKey> MAP_CODEC = EssenceType.CODEC.optionalFieldOf("type", EssenceType.BLOOD).xmap(ForbiddenEssenceKey::of, ForbiddenEssenceKey::type);
    private final EssenceType type;

    private ForbiddenEssenceKey(EssenceType type) {
        this.type = type;
    }

    public static ForbiddenEssenceKey of(EssenceType type) {
        return switch (type) {
            case BLOOD -> BLOOD;
            case SOULS -> SOULS;
            case AUREAL -> AUREAL;
            case EXPERIENCE -> EXPERIENCE;
        };
    }

    public EssenceType type(){
        return this.type;
    }

    @Override
    public AEKeyType getType() {
        return ForbiddenEssenceKeyType.INSTANCE;
    }

    @Override
    public AEKey dropSecondary() {
        return this;
    }

    @Override
    public CompoundTag toTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", this.type.getSerializedName());
        return tag;
    }

    @Override
    public Object getPrimaryKey() {
        return type;
    }

    @Override
    public ResourceLocation getId() {
        return switch (type){
            case EXPERIENCE -> EXPERIENCE_ID;
            case AUREAL -> AUREAL_ID;
            case SOULS -> SOULS_ID;
            case BLOOD -> BLOOD_ID;
        };
    }

    @Override
    public void writeToPacket(RegistryFriendlyByteBuf data) {
        data.writeEnum(type);
    }

    @Override
    protected Component computeDisplayName() {
        return Component.translatable("key.cdp2.forbidden_" + type.getSerializedName() + "_essence");
    }

    @Override
    public void addDrops(long l, List<ItemStack> list, Level level, BlockPos blockPos) {

    }

    @Override
    public boolean hasComponents() {
        return false;
    }

    public boolean equals(Object obj) {
        boolean is;
        if (obj instanceof ForbiddenEssenceKey other) {
            if (this.type == other.type) {
                is = true;
                return is;
            }
        }

        is = false;
        return is;
    }

    public int hashCode() {
        return this.type.hashCode();
    }

    public String toString() {
        return this.getId().toString();
    }
}