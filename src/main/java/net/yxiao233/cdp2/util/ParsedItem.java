package net.yxiao233.cdp2.util;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;

public record ParsedItem(ExtraCodecs.TagOrElementLocation tagOrId, int count) {
    public ParsedItem(ExtraCodecs.TagOrElementLocation tagOrId, int count) {
        this.tagOrId = tagOrId;
        this.count = count;
    }

    public static ParsedItem read(StringReader input) throws CommandSyntaxException {
        return read(input, false);
    }

    public static ParsedItem read(StringReader input, boolean single) throws CommandSyntaxException {
        int count = 1;
        int cursor = input.getCursor();

        try {
            count = input.readInt();
            input.expect('x');
            input.expect(' ');
            Preconditions.checkArgument(!single, "Count not allowed here");
            Preconditions.checkArgument(count > 0, "Count must be positive");
        } catch (CommandSyntaxException var6) {
            input.setCursor(cursor);
        }

        boolean tag = false;
        if (input.peek() == '#') {
            input.skip();
            tag = true;
        }

        ResourceLocation id = ResourceLocation.read(input);
        if (!tag) {
            Preconditions.checkArgument(BuiltInRegistries.ITEM.containsKey(id), "Unknown item: %s", id);
        }

        return new ParsedItem(new ExtraCodecs.TagOrElementLocation(id, tag), count);
    }

    public @NotNull String toString() {
        return this.count == 1 ? this.tagOrId.toString() : "%dx %s".formatted(this.count, this.tagOrId);
    }

    private Ingredient rawIngredient() {
        return this.tagOrId.tag() ? Ingredient.of(TagKey.create(Registries.ITEM, this.tagOrId.id())) : Ingredient.of(new ItemLike[]{(ItemLike)BuiltInRegistries.ITEM.get(this.tagOrId.id())});
    }

    public Ingredient ingredient() {
        Preconditions.checkArgument(this.count == 1, "Ingredient must not have count");
        return this.rawIngredient();
    }

    public SizedIngredient sizedIngredient() {
        return new SizedIngredient(this.rawIngredient(), this.count);
    }

    public ItemStack itemStack() {
        Preconditions.checkArgument(!this.tagOrId.tag(), "ItemStack must not be a tag");
        return new ItemStack((ItemLike)BuiltInRegistries.ITEM.getOptional(this.tagOrId.id()).orElseThrow(), this.count);
    }

    public ExtraCodecs.TagOrElementLocation tagOrId() {
        return this.tagOrId;
    }

    public int count() {
        return this.count;
    }
}

