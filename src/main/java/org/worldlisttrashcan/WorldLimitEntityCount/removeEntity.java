package org.worldlisttrashcan.WorldLimitEntityCount;

import org.bukkit.entity.LivingEntity;

import javax.swing.text.html.parser.Entity;

public class removeEntity {
    public static boolean ItemDropFlag = false;
    public static void removeLivingEntity(LivingEntity livingEntity){
        if(ItemDropFlag){
            livingEntity.setHealth(0);
        }else {
            livingEntity.remove();
        }
    }
}
