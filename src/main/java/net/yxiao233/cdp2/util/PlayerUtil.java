package net.yxiao233.cdp2.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.common.registry.CDPAttachmentTypes;

public class PlayerUtil {
    public static void teleportTo(Player player, ResourceKey<Level> dimension){
        CompoundTag data = player.getData(CDPAttachmentTypes.PLAYER_EXTRA_DATA);
        if(data.contains("can_go_to_other_dimensions") && !data.getBoolean("can_go_to_other_dimensions")){
            if(player instanceof ServerPlayer serverPlayer){
                if(!serverPlayer.level().dimension().equals(dimension)){
                    ServerLevel targetLevel = serverPlayer.server.getLevel(dimension);
                    if(targetLevel != null && !player.level().equals(targetLevel)){
                        double x,y,z;
                        if(data.contains("unknown_dimension_respawnpoint")){
                            int[] respawnpoints = data.getIntArray("unknown_dimension_respawnpoint");
                            x = respawnpoints[0];
                            y = respawnpoints[1];
                            z = respawnpoints[2];
                        }else{
                            x = 0;
                            y = 58;
                            z = 0;
                        }
                        serverPlayer.server.execute(() -> {
                            serverPlayer.teleportTo(
                                    targetLevel,
                                    (int) x, (int) y, (int) z,
                                    player.getYRot(), player.getXRot()
                            );
                        });
                    }
                }
            }
        }
    }
}
