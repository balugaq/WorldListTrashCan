package org.worldlisttrashcan.WorldLimitEntityCount;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.worldlisttrashcan.message;

import java.util.ArrayList;
import java.util.List;

import static org.worldlisttrashcan.WorldLimitEntityCount.LimitMain.*;
import static org.worldlisttrashcan.WorldLimitEntityCount.removeEntity.dealEntity;
import static org.worldlisttrashcan.WorldLimitEntityCount.removeEntity.removeLivingEntity;


public class PaperEntityMoveEvent implements Listener {
    @EventHandler
    public void EntityMoveEvent(EntityMoveEvent event){

        if(GatherLimitFlag){

            Entity entity = event.getEntity();

            //如果没血了就不处理
            if (((LivingEntity) entity).getHealth() <= 0) {
//                System.out.println("return 了1");
                return;
            }

//            System.out.println("000 "+entity.getName());
            if (GatherBanWorlds.contains(entity.getWorld().getName())) {
                return;
            }
//            EntityType entityType = entity.getType();
            String entityType = entity.getName();
//            System.out.println("entityType.name() "+entityType.name());

//            System.out.println("111 "+entity.getName());

            if (GatherLimits.containsKey(entityType.toUpperCase())) {
                dealEntity(entity);
            }
        }
    }

}