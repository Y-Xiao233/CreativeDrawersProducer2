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
    public static final String LOCATION_ASSETS = "assets";
    public static final String LOCATION_DATA = "data";
    public NbtFile(String location, String nameSpace, String path, String fileName){
        this.nbt = readFromPath(location,nameSpace,path,fileName);
    }

    public CompoundTag readFromPath(String location, String nameSpace, String path, String fileName){
        String classpath = "/" + location + "/" + nameSpace + "/" + path + "/" + fileName + ".nbt";
        try (InputStream is = getClass().getResourceAsStream(classpath)) {
            if (is != null) {
                return NbtIo.readCompressed(is, NbtAccounter.unlimitedHeap());
            }
        } catch (IOException ignored) {}

        Path kubejsPath = Path.of("kubejs/" + location + "/" + nameSpace + "/" + path + "/" + fileName + ".nbt");
        if (Files.exists(kubejsPath)) {
            try (InputStream is = Files.newInputStream(kubejsPath)) {
                return NbtIo.readCompressed(is, NbtAccounter.unlimitedHeap());
            } catch (IOException e) {
                LOGGER.error("Failed to read KubeJS NBT: {}", kubejsPath, e);
            }
        }
        System.out.println(kubejsPath);
        return null;
    }

    public CompoundTag getNbt() {
        return nbt;
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
