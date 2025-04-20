package org.worldlisttrashcan.Method;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.worldlisttrashcan.message;

import java.util.ArrayList;
import java.util.List;

public class Method {











    private static List<String> monsterTypes;

//    烈焰人 (BLAZE)
//    沼骸 (GUARDIAN)
//    旋风人 (VEX)
//    苦力怕 (CREEPER)
//    远古守卫者 (ELDER_GUARDIAN)
//    末影螨 (ENDERMITES)
//    唤魔者 (EVOKER)
//    恶魂 (WITHER_SKELETON)
//    守卫者 (GUARDIAN)
//    疣猪兽 (HOGLIN)
//    尸壳 (ZOMBIE_VILLAGER)
//    岩浆怪 (MAGMA_CUBE)
//    幻翼 (PHANTOM)
//    猪灵蛮兵 (PIGLIN_BRUTE)
//    掠夺者 (RAVAGER)
//    劫掠兽 (PIGLIN)
//    潜影贝 (SHULKER)
//    蠹虫 (SILVERFISH)
//    骷髅 (SKELETON)
//    史莱姆 (SLIME)
//    流浪者 (WANDERING_TRADER)
//    恼鬼 (VEX)
//    卫道士 (VINDICATOR)
//    监守者 (WITHER)
//    女巫 (WITCH)
//    凋灵骷髅 (WITHER_SKELETON)
//    僵尸疣猪兽 (ZOMBIFIED_PIGLIN)
//    僵尸 (ZOMBIE)
//    僵尸村民 (ZOMBIE_VILLAGER)
//    嘲讽者 (GOAT)（假设“嘎枝”是指嘲讽者）
//    溺尸 (DROWNED)
//    末影人 (ENDERMAN)
//    蜘蛛 (SPIDER)
//    洞穴蜘蛛 (CAVE_SPIDER)
//    僵尸猪灵 (ZOMBIFIED_PIGLIN)

    static {
        monsterTypes = new ArrayList<>();
        monsterTypes.add("BLAZE");
        monsterTypes.add("GUARDIAN");
        monsterTypes.add("VEX");
        monsterTypes.add("CREEPER");
        monsterTypes.add("ELDER_GUARDIAN");
        monsterTypes.add("ENDERMITE");
        monsterTypes.add("EVOKER");
        monsterTypes.add("WITHER_SKELETON");
        monsterTypes.add("GUARDIAN");
        monsterTypes.add("HOGLIN");
        monsterTypes.add("ZOMBIE_VILLAGER");
        monsterTypes.add("MAGMA_CUBE");
        monsterTypes.add("PHANTOM");
        monsterTypes.add("PIGLIN_BRUTE");
        monsterTypes.add("RAVAGER");
        monsterTypes.add("PIGLIN");
        monsterTypes.add("SHULKER");
        monsterTypes.add("SILVERFISH");
        monsterTypes.add("SKELETON");
        monsterTypes.add("SLIME");
        monsterTypes.add("WANDERING_TRADER");
        monsterTypes.add("VEX");
        monsterTypes.add("VINDICATOR");
        monsterTypes.add("WITHER");
        monsterTypes.add("WITCH");
        monsterTypes.add("WITHER_SKELETON");
        monsterTypes.add("ZOMBIFIED_PIGLIN");
        monsterTypes.add("ZOMBIE");
        monsterTypes.add("ZOMBIE_VILLAGER");
        monsterTypes.add("GOAT");
        monsterTypes.add("DROWNED");
        monsterTypes.add("ENDERMAN");
        monsterTypes.add("SPIDER");
        monsterTypes.add("CAVE_SPIDER");
        monsterTypes.add("ZOMBIFIED_PIGLIN");
    }

    public static Boolean hasEnemyClass = false;

    //    //判断实体是否是怪物
    public static boolean isMonster(Entity entity) {
        String entityType = entity.getType().toString();
        if (monsterTypes.contains(entityType.toLowerCase()))
            return true;
        else if (entity instanceof Monster) {
            return true;
        }
        if (hasEnemyClass) {
//            System.out.println("hasEnemyClass hasEnemyClass hasEnemyClass");
            return entity instanceof Enemy;
        }

        return false;
    }






    /**
     * 检查类是否存在
     *
     * @param className 要检查的类的全限定名
     * @return 如果类存在，则返回 true，否则返回 false
     */
    public static boolean isClassPresent(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    public static boolean AutoCheckFoliaServer(){

        // 插件启动逻辑
        if (isClassPresent("io.papermc.paper.threadedregions.scheduler.FoliaRegionScheduler")) {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"检测到服务器拥有Folia的API，正在适用目前最佳的方法");
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"检测到服务器拥有Folia的API，正在适用目前最佳的方法");
            message.consoleSay("&a检测到服务器拥有Folia的API，正在适用目前最佳的方法");
            return true;

        } else {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"插件正在自动适应");

        }

        return false;
    }
    public static boolean AutoCheckPaperServer(){

        // 插件启动逻辑
//        if (isClassPresent("io.papermc.paper.event.entity.EntityMoveEvent")) {
        if (isClassPresent("io.papermc.paper.threadedregions.scheduler.ScheduledTask")) {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"检测到服务器拥有Paper的API，正在适用目前最佳的方法");
            message.consoleSay("&a检测到服务器拥有Paper的API，正在适用目前最佳的方法");
            return true;

        } else {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"插件正在自动适应");
        }

        return false;
    }
    public static boolean AutoCheckEntityMoveEventServer(){

        // 插件启动逻辑
        if (isClassPresent("io.papermc.paper.event.entity.EntityMoveEvent")) {
//        if (isClassPresent("io.papermc.paper.threadedregions.scheduler.ScheduledTask")) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"检测到服务器拥有Paper的EntityMoveEvent，正在适用目前最佳的方法");
//            message.consoleSay("&a检测到服务器拥有Paper的API，正在适用目前最佳的方法");
            return true;

        } else {
//            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"插件正在自动适应");
        }

        return false;
    }




}
