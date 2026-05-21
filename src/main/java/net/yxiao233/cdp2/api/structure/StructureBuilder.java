package net.yxiao233.cdp2.api.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StructureBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureBuilder.class);
    private static final Map<String, CompoundTag> CACHE = new ConcurrentHashMap<>();

    public CompoundTag nbtContext;

    public StructureBuilder(String path){
        this.nbtContext = CACHE.computeIfAbsent(path, this::readNbt);
    }

    public CompoundTag readNbt(String path){
        String classpath = "/assets/cdp2/structure/" + path + ".nbt";
        try (InputStream is = getClass().getResourceAsStream(classpath)) {
            if (is != null) {
                return NbtIo.readCompressed(is, NbtAccounter.unlimitedHeap());
            }
        } catch (IOException ignored) {}

        Path kubejsPath = Path.of("kubejs/assets/cdp2/structure/" + path + ".nbt");
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

    public StructureTemplate getStructureTemplate(ServerLevel level){
        if (nbtContext == null) {
            LOGGER.error("Cannot build structure: NBT context is null");
            return null;
        }
        StructureTemplate template = new StructureTemplate();
        template.load(level.holderLookup(Registries.BLOCK), nbtContext);
        return template;
    }

    public void buildStructure(ServerLevel level, BlockPos pos) {
        StructureTemplate template = getStructureTemplate(level);
        if(template == null){
            return;
        }

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .setRotation(Rotation.NONE);

        template.placeInWorld(level, pos, pos, settings, level.random, Block.UPDATE_ALL);
    }
}
