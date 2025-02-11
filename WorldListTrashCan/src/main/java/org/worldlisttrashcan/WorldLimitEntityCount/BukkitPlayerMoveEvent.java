package org.worldlisttrashcan.WorldLimitEntityCount;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static org.worldlisttrashcan.WorldLimitEntityCount.LimitMain.*;
import static org.worldlisttrashcan.WorldLimitEntityCount.removeEntity.dealEntity;


public class BukkitPlayerMoveEvent implements Listener {
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event){


        Player player = event.getPlayer();
        if(GatherLimitFlag){


//            System.out.println("PlayerMoveEvent1");

            if (GatherBanWorlds.contains(player.getWorld().getName())) {
                return;
            }

//            for(Entity entity : player.getNearbyEntities(10,10,10)){
            for(Entity entity : player.getNearbyEntities(10,10,10)){


//                System.out.println("PlayerMoveEvent2");

                if (entity instanceof LivingEntity) {
                    //如果没血了就不处理
                    if (((LivingEntity) entity).getHealth() <= 0) {
//                        System.out.println("return 了2");
                        return;
                    }
                }


//                System.out.println(entity.getName());
//                EntityType entityType = entity.getType();
                String entityType = entity.getName();

//                System.out.println("entityType.toUpperCase() "+entityType.toUpperCase());
//                for (String string : GatherLimits.keySet()) {
//                    System.out.println("string "+string);
//                }
                if (GatherLimits.containsKey(entityType.toUpperCase())) {


                    dealEntity(entity);
                }
            }

        }
    }

}