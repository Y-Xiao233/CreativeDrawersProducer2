package net.yxiao233.cdp2.api.structure;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NbtFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(NbtFile.class);
    private final CompoundTag nbt;
    public NbtFile(ResourceLocation location){
        this.nbt = readFromLocation(location);
    }

    public NbtFile(String nameSpace, String path){
        this.nbt = readFromPath(nameSpace, path);
    }

    public CompoundTag readFromLocation(ResourceLocation location){
        return readFromPath(location.getNamespace(), location.getPath());
    }

    public CompoundTag readFromPath(String nameSpace, String path){
        String classpath = "/assets/" + nameSpace + "/structure/" + path + ".nbt";
        try (InputStream is = getClass().getResourceAsStream(classpath)) {
            if (is != null) {
                return NbtIo.readCompressed(is, NbtAccounter.unlimitedHeap());
            }
        } catch (IOException ignored) {}

        Path kubejsPath = Path.of("kubejs/assets/" + nameSpace + "/structure/" + path + ".nbt");
        if (Files.exists(kubejsPath)) {
            try (InputStream is = Files.newInputStream(kubejsPath)) {
                return NbtIo.readCompressed(is, NbtAccounter.unlimitedHeap());
            } catch (IOException e) {
                LOGGER.error("Failed to read KubeJS NBT: {}", kubejsPath, e);
            }
        }

        LOGGER.error("NBT file not found: {} (tried classpath, kubejs/assets/cdp2/structure/, kubejs/assets/cdp2/structures/)", path);
        return null;
    }


    public List<Pair<BlockPos, BlockState>> getBlocks(){
        List<Pair<BlockPos, BlockState>> result = new ArrayList<>();
        if (nbt == null) return result;

        ListTag paletteList = nbt.getList("palette", Tag.TAG_COMPOUND);
        ListTag blocksList = nbt.getList("blocks", Tag.TAG_COMPOUND);

        BlockState[] palette = new BlockState[paletteList.size()];
        for (int i = 0; i < paletteList.size(); i++) {
            CompoundTag entry = paletteList.getCompound(i);
            String name = entry.getString("Name");
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(name));
            BlockState state = block.defaultBlockState();
            if (entry.contains("Properties")) {
                CompoundTag props = entry.getCompound("Properties");
                StateDefinition<Block, BlockState> definition = block.getStateDefinition();
                for (String key : props.getAllKeys()) {
                    Property<?> property = definition.getProperty(key);
                    if (property != null) {
                        state = setPropertyValue(state, property, props.getString(key));
                    }
                }
            }
            palette[i] = state;
        }

        for (int i = 0; i < blocksList.size(); i++) {
            CompoundTag entry = blocksList.getCompound(i);
            ListTag posList = entry.getList("pos", Tag.TAG_INT);
            BlockPos pos = new BlockPos(posList.getInt(0), posList.getInt(1), posList.getInt(2));
            int stateIndex = entry.getInt("state");
            result.add(Pair.of(pos, palette[stateIndex]));
        }

        return result;
    }

    private static <T extends Comparable<T>> BlockState setPropertyValue(BlockState state, Property<T> property, String value) {
        Optional<T> parsed = property.getValue(value);
        return parsed.map(v -> state.setValue(property, v)).orElse(state);
    }
}
