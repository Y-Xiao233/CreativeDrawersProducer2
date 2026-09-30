package net.yxiao233.cdp2.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerUtil {
    public static void teleportTo(Player player, ResourceKey<Level> dimension){
        if(player instanceof ServerPlayer serverPlayer){
            if(!serverPlayer.level().dimension().equals(dimension)){
                ServerLevel targetLevel = serverPlayer.server.getLevel(dimension);
                if(targetLevel != null && !player.level().equals(targetLevel)){
                    BlockPos respawnPosition = serverPlayer.getRespawnPosition();
                    double x,y,z;
                    if(respawnPosition != null){
                        x = respawnPosition.getX();
                        y = respawnPosition.getY();
                        z = respawnPosition.getZ();
                    }else if(player.getPersistentData().contains("unknown_dimension_respawnpoint")){
                        int[] respawnpoints = player.getPersistentData().getIntArray("unknown_dimension_respawnpoint");
                        x = respawnpoints[0];
                        y = respawnpoints[1];
                        z = respawnpoints[2];
                    }else{
                        x = 0;
                        y = 58;
                        z = 0;
                    }
                    double finalX = x;
                    double finalY = y;
                    double finalZ = z;
                    serverPlayer.server.execute(() -> {
                        serverPlayer.teleportTo(
                                targetLevel,
                                (int) finalX, (int) finalY, (int) finalZ,
                                player.getYRot(), player.getXRot()
                        );
                    });
                }
            }
        }
    }
}
