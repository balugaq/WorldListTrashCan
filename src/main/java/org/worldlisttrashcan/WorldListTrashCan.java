package org.worldlisttrashcan;

import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.worldlisttrashcan.DropSystem.DropLimitListener;
import org.worldlisttrashcan.SimpleChange.NotPickArrowListener;
import org.worldlisttrashcan.SpeakSystem.QuickSpeakListener;
import org.worldlisttrashcan.SpeakSystem.QuickUseCommandListener;
import org.worldlisttrashcan.SimpleChange.TreadingFarmLandListener;
import org.worldlisttrashcan.TrashMain.*;
import org.worldlisttrashcan.WorldLimitEntityCount.LimitMain;
import static org.worldlisttrashcan.IsVersion.IsFoliaSever;

import java.util.*;
import java.util.function.Consumer;

import static org.worldlisttrashcan.DropSystem.DropLimitListener.PlayerDropList;
import static org.worldlisttrashcan.TrashMain.TrashListener.GlobalItemSetString;
import static org.worldlisttrashcan.WorldLimitEntityCount.LimitMain.*;
import static org.worldlisttrashcan.message.AllMessageLoad;
import static org.worldlisttrashcan.message.chanceMessage;

public final class WorldListTrashCan extends JavaPlugin {

    public static Plugin main;
    ClearItemsTask clearItemsTask;
    FoliaClearItemsTask foliaClearItemsTask;
    public static GlobalTrashGui globalTrashGui;

    public static List<Inventory> GlobalTrashList = new ArrayList<>();

    static public Map<World, RashCanInformation> WorldToLocation = new HashMap<>();
    static public List<Player> UseEntityBarPlayerList = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        main = this;
        Bukkit.getPluginManager().registerEvents(new TrashListener(), this);
        Bukkit.getPluginManager().registerEvents(new GuiListener(), this);
        IsFoliaSever = Bukkit.getServer().getVersion().contains("Folia");


        reload();


    }






    public static Map<Player,World> PlayerToWorld = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        System.out.println(command.toString()+"："+args.toString());
        // 检查是否是管理员 (OP) 使用命令
        if (command.getName().equalsIgnoreCase("WorldListTrashCan")||command.getName().equalsIgnoreCase("WTC")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "WorldListTrashCan " + ChatColor.BLUE + "作者QQ：2831508831");
                sender.sendMessage(ChatColor.YELLOW + "/WorldListTrashCan help" + ChatColor.BLUE + "帮助");
            } else if (args[0].equalsIgnoreCase("reload")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));

                if (sender.isOp()) {
                    reload();
                    sender.sendMessage(message.find("ReloadInformation"));
                }
            } else if (args[0].equalsIgnoreCase("help")) {
//                sender.sendMessage("§0——————§7【§bWorldListTrashCan§7】§0——————");
//                sender.sendMessage("§6/WorldListTrashCan help"+"§d查看帮助");
//                sender.sendMessage("§6/WorldListTrashCan ban"+"§d打开本世界垃圾黑名单Gui");
//                sender.sendMessage("§6/WorldListTrashCan GlobalBan"+"§d打开全局世界垃圾黑名单Gui");
//                sender.sendMessage("§6/WorldListTrashCan add [世界名] <数量>"+"§d设置世界名垃圾桶最大数量(不填则为脚下世界)");
                if(sender.hasPermission("WorldListTrashCan.help")||sender.isOp()){
                    for (String string : message.getConfig().getStringList("HelpTitle")) {
                        sender.sendMessage(string.replace("&","§"));
                    }
                }else {
                    sender.sendMessage(message.find("NotHavaPermission").replace("%permission%","WorldListTrashCan.help"));
                }

//                sender.sendMessage("§6/WorldListTrashCan "+"§d");
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));

//                if (sender instanceof Player) {
//                    if (sender.isOp()) {
//                        Player player = ((Player) sender).getPlayer();
//                        ItemStack itemStack = player.getInventory().getItemInMainHand();
//                        sender.sendMessage(ChatColor.GREEN + "你手上拿的是：" + itemStack.getType().toString());
//                    }
//                } else {
//                    sender.sendMessage(ChatColor.RED + "只有玩家能够使用这个命令");
//                }
            } else if (args[0].equalsIgnoreCase("Ban")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));

                if (sender instanceof Player) {
                    if (sender.hasPermission("WorldListTrashCan.BanGui")||sender.isOp()) {
                        Player player = ((Player) sender).getPlayer();

                        PlayerToWorld.put(player,player.getWorld());
                        sender.sendMessage(message.find("SecondCountdown"));
                        if(IsFoliaSever){
                            ScheduledTask task = player.getScheduler().runDelayed(main, new Consumer<ScheduledTask>() {
//                                int count = 0;

                                @Override
                                public void accept(ScheduledTask scheduledTask) {
//                                    if(count>3){
                                        if(PlayerToWorld.get(player)!=null){
                                            PlayerToWorld.remove(player);
                                            sender.sendMessage(message.find("SecondCountdownEnd"));
                                        }else {

                                        }
                                        //stop
//                                    }

//                                    count++;
                                }
                            }, () -> main.getLogger().info("Error,Player is null"),60L);
                        }else {
                            new BukkitRunnable(){

                                int count = 0;
                                @Override
                                public void run() {
                                    if(count>3){
                                        if(PlayerToWorld.get(player)!=null){
                                            PlayerToWorld.remove(player);
                                            sender.sendMessage(message.find("SecondCountdownEnd"));
                                        }else {

                                        }
                                        cancel();
                                    }

                                    count++;
                                }
                            }.runTaskTimer(this,0L,60L);
                        }
//                        Inventory inventory = (new BanGui()).getInventory(player);
//                        player.openInventory(inventory);
//                        for (ItemStack itemStack : inventory) {
//                            if(itemStack!=null){
//                                player.sendMessage(ChatColor.BLUE+"物品："+itemStack.getType());
//                            }
//                        }
                    }else {
                        sender.sendMessage(message.find("NotHavePermission").replace("%permission%","WorldListTrashCan.BanGui"));
                    }
                } else {
                    sender.sendMessage(message.find("NotIsPlayer"));
                }
            } else if (args[0].equalsIgnoreCase("GlobalBan")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));

                if (sender instanceof Player) {
                    if (sender.hasPermission("WorldListTrashCan.GlobalBan")||sender.isOp()) {
                        Player player = ((Player) sender).getPlayer();
                        Inventory inventory = (new BanGui()).getGlobalInventory(player);
                        player.openInventory(inventory);
//                        for (ItemStack itemStack : inventory) {
//                            if(itemStack!=null){
//                                player.sendMessage(ChatColor.BLUE+"物品："+itemStack.getType());
//                            }
//                        }
                    }else {
                        sender.sendMessage(message.find("NotHavaPermission").replace("%permission%","WorldListTrashCan.GlobalBan"));
                    }
                } else {
                    sender.sendMessage(message.find("NotIsPlayer"));
                }
            }else if (args[0].equalsIgnoreCase("GlobalTrash")||args[0].equalsIgnoreCase("Trash")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));

                if (sender instanceof Player) {
                    if (sender.hasPermission("WorldListTrashCan.GlobalTrashOpen")||sender.isOp()) {
                        Player player = ((Player) sender).getPlayer();
                        Inventory inventory = globalTrashGui.getInventory();
                        player.openInventory(inventory);
                    }else {
                        sender.sendMessage(message.find("NotHavaPermission").replace("%permission%","WorldListTrashCan.GlobalTrashOpen"));
                    }
                } else {
                    sender.sendMessage(message.find("NotIsPlayer"));
                }
            }else if (args[0].equalsIgnoreCase("DropMode")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));
                if(!getConfig().getBoolean("DropItemCheck.Flag")){
                    return true;
                }

                if (sender instanceof Player) {
                    if (sender.hasPermission("WorldListTrashCan.GlobalTrashOpen")||sender.isOp()) {
                        Player player = ((Player) sender).getPlayer();
                        //处理玩家
                        if(PlayerDropList.contains(player)){
                            PlayerDropList.remove(player);
                            player.sendMessage(message.find("OffDropMode"));
                        }else {
                            PlayerDropList.add(player);
                            player.sendMessage(message.find("OpenDropMode"));
                        }

                    }else {
                        sender.sendMessage(message.find("NotHavaPermission").replace("%permission%","WorldListTrashCan.DropMode"));
                    }
                } else {
                    sender.sendMessage(message.find("NotIsPlayer"));
                }
            }else if (args[0].equalsIgnoreCase("look")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));

                if (sender instanceof Player) {
                    if (sender.hasPermission("WorldListTrashCan.Look")||sender.isOp()) {
                        Player player = ((Player) sender).getPlayer();
                        UseEntityBarPlayerList.add(player);
                        player.sendMessage(message.find("PleaseRightEntity"));
                    }else {
                        sender.sendMessage(message.find("NotHavaPermission").replace("%permission%","WorldListTrashCan.Look"));
                    }
                } else {
                    sender.sendMessage(message.find("NotIsPlayer"));
                }
            } else if (args[0].equalsIgnoreCase("add")) {
//                getLogger().info("发送指令者是: "+sender.getName());
//                getLogger().info("是否有权限: "+sender.hasPermission("WorldListTrashCan.reload"));
//                data.getConfig().getInt("WorldData." + world.getName() + ".RashMaxCount")


                if (args.length > 2) {
                    if (sender.isOp()) {
                        World world = Bukkit.getWorld(args[1]);
                        if (world != null) {
                            try {

                                int inputCount = Integer.parseInt(args[2]);

                                int count = data.RashMaxCountAdd(world, inputCount);
                                sender.sendMessage(message.find("AddRashMaxCountTrue")
                                        .replace("%world%", world.getName())
                                        .replace("%count%", count+""));

                            } catch (Exception e) {
                                //不是整数
                                sender.sendMessage(message.find("NotInt"));
                            }
                        } else {
                            sender.sendMessage(message.find("NotFindWorld"));

                        }
                    } else {
                        sender.sendMessage(message.find("NotIsOp"));
                    }
                } else if (args.length > 1) {
                    if (sender instanceof Player) {
                        Player player = ((Player) sender).getPlayer();
                        World world = player.getWorld();
                        if (world != null) {
                            try {
//                                System.out.println("1");
                                int inputCount = Integer.parseInt(args[1]);

                                int count = data.RashMaxCountAdd(world, inputCount);
                                sender.sendMessage(message.find("AddRashMaxCountTrue")
                                        .replace("%world%", world.getName())
                                        .replace("%count%", count+""));




                            } catch (Exception e) {
                                //不是整数
                                sender.sendMessage(message.find("NotInt"));
                            }
                        } else {
                            sender.sendMessage(message.find("NotFindWorld"));

                        }
                    } else {
//                        sender.sendMessage(ChatColor.RED + "只有玩家能够不写世界名字这个参数来使用这个命令");
                        sender.sendMessage(message.find("NotInputArgAdd"));
                        sender.sendMessage(message.find("WorldListTrashCanAdd"));
                    }
                } else {
                    sender.sendMessage(message.find("WorldListTrashCanAdd"));
                }


            } else {
                sender.sendMessage(ChatColor.GREEN + "WorldListTrashCan " + ChatColor.BLUE + "作者QQ：2831508831");
//                sender.sendMessage(ChatColor.YELLOW + "/WorldListTrashCan reload" + ChatColor.BLUE + "重载插件");
                sender.sendMessage(ChatColor.YELLOW + "/WorldListTrashCan help" + ChatColor.BLUE + "插件帮助");
            }
        }
        return true;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void ConfigStringReplace(ConfigurationSection config, String target, String replacement) {
        for (String key : config.getKeys(false)) {
            if (config.isConfigurationSection(key)) {
                // 递归处理子节
                ConfigStringReplace(config.getConfigurationSection(key), target, replacement);
            } else if (config.isString(key)) {
                // 替换字符串值
                String originalValue = config.getString(key);
                String modifiedValue = originalValue.replace(target, replacement);
                config.set(key, modifiedValue);
            }
        }
    }



    public static boolean GlobalTrashGuiFlag = false;
//    static public data data;

    public static List<String> NoClearContainerLore = new ArrayList<>();
    public void reload(){
//        reloadConfig();

        saveDefaultConfig();
        reloadConfig();
        ConfigStringReplace(getConfig(),"&","§");

        AllMessageLoad();
        chanceMessage = getConfig().getString("Set.Lang");
        message.reloadMessage();

        data.LoadData();
//        AllMessageLoad();
//        chanceMessage = getConfig().getString("Set.Lang");
//        message.reloadMessage();
        GlobalItemSetString = new HashSet<>(main.getConfig().getStringList("GlobalBanItem"));
        globalTrashGui = new GlobalTrashGui();


        if(IsFoliaSever){
            if(foliaClearItemsTask!=null){
                foliaClearItemsTask.Stop();
            }
            foliaClearItemsTask = new FoliaClearItemsTask();
            foliaClearItemsTask.Start();
        }else {
            if(clearItemsTask!=null){
                clearItemsTask.Stop();
            }
            clearItemsTask = new ClearItemsTask();
            clearItemsTask.Start();
        }



        GlobalTrashGuiFlag = getConfig().getBoolean("Set.GlobalTrash.Flag");




        if(getConfig().getBoolean("SimpleOptimize.NotPickArrow")){
            HandlerList.unregisterAll(notPickArrowListener);
            Bukkit.getPluginManager().registerEvents(notPickArrowListener, this);
        }else {
            HandlerList.unregisterAll(notPickArrowListener);
        }

        if(getConfig().getBoolean("SimpleOptimize.NotTreadingFarmLand")){
            HandlerList.unregisterAll(treadingFarmLandListener);
            Bukkit.getPluginManager().registerEvents(treadingFarmLandListener, this);
        }else {
            HandlerList.unregisterAll(treadingFarmLandListener);
        }




        if(getConfig().getBoolean("WorldEntityLimitCount.Flag")){
            HandlerList.unregisterAll(limitMain);
            Bukkit.getPluginManager().registerEvents(limitMain, this);
        }else {
            HandlerList.unregisterAll(limitMain);
        }

        if(getConfig().getBoolean("ChatSet.QuickSendMessage.Flag")){
            HandlerList.unregisterAll(quickSpeakListener);
            quickSpeakListener.Init();
            Bukkit.getPluginManager().registerEvents(quickSpeakListener, this);
        }else {
            HandlerList.unregisterAll(quickSpeakListener);
        }

        if(getConfig().getBoolean("ChatSet.QuickUseCommand.Flag")){
            HandlerList.unregisterAll(quickUseCommandListener);
            quickUseCommandListener.Init();
            Bukkit.getPluginManager().registerEvents(quickUseCommandListener, this);
        }else {
            HandlerList.unregisterAll(quickUseCommandListener);
        }


        if(getConfig().getBoolean("DropItemCheck.Flag")){
            HandlerList.unregisterAll(dropLimitListener);
            Bukkit.getPluginManager().registerEvents(dropLimitListener, this);
        }else {
            HandlerList.unregisterAll(dropLimitListener);
        }





//        TreadingFarmLandListener
        LoadWorldLimitEntityConfig();
        NoClearContainerLore = main.getConfig().getStringList("Set.NoClearContainerLore");
    }
    DropLimitListener dropLimitListener = new DropLimitListener();
    NotPickArrowListener notPickArrowListener = new NotPickArrowListener();
    TreadingFarmLandListener treadingFarmLandListener = new TreadingFarmLandListener();
    QuickSpeakListener quickSpeakListener = new QuickSpeakListener();
    QuickUseCommandListener quickUseCommandListener = new QuickUseCommandListener();
    LimitMain limitMain = new LimitMain();


    public void LoadWorldLimitEntityConfig(){
        if(!worldLimits.isEmpty()){
            worldLimits.clear();
        }
//        if(!globalLimits.isEmpty()){
//            globalLimits.clear();
//        }
        WorldLimitFlag = main.getConfig().getBoolean("WorldEntityLimitCount.Flag");
        GatherLimitFlag = main.getConfig().getBoolean("GatherEntityLimitCount.Flag");
        if(WorldLimitFlag){
            for(String EntityToCountStr : main.getConfig().getStringList("WorldEntityLimitCount.DefaultCount")){
                String[] strings = EntityToCountStr.split(";");
//            strings[0]
                EntityType entityType = getEntityType(strings[0]);
                if (entityType == null) {
                    //EntityCountSetOK: "%PluginTitle% 成功设置各世界该 %Entity% 实体默认为 %Count% 个"
                    //EntityCountSetError: "%PluginTitle% 实体类型错误！可选的实体类型包括：%EntityTypes%"
                    main.getLogger().info(message.find("EntityCountSetError").replace("%EntityName%",strings[0]).replace("%EntityTypes%",getEntityTypes()));
                    continue;
                }
                int limit = Integer.parseInt(strings[1]);
                worldLimits.put(entityType.name(), limit);
//            main.getLogger().info(ChatColor.GREEN + "成功设置 " + entityType.name() + " 的数量限制为 " + limit);
                main.getLogger().info(message.find("EntityCountSetOK").replace("%Count%",limit+"").replace("%Entity%",entityType.name()));
            }
            BanWorlds.addAll(main.getConfig().getStringList("WorldEntityLimitCount.BanWorldNameList"));
        }














        if(GatherLimitFlag){
            for(String EntityToCountStrAndLimitRange : main.getConfig().getStringList("GatherEntityLimitCount.DefaultCount")){
                String[] strings = EntityToCountStrAndLimitRange.split(";");
//            strings[0]
                EntityType entityType = getEntityType(strings[0]);
                if (entityType == null) {
                    //EntityCountSetOK: "%PluginTitle% 成功设置各世界该 %Entity% 实体默认为 %Count% 个"
                    //EntityCountSetError: "%PluginTitle% 实体类型错误！可选的实体类型包括：%EntityTypes%"
//                main.getLogger().info(message.find("EntityCountSetError").replace("%EntityName%",strings[0]).replace("%EntityTypes%",getEntityTypes()));
                    continue;
                }
                int limit = Integer.parseInt(strings[1]);
                int range = Integer.parseInt(strings[2]);
                int clearCount = Integer.parseInt(strings[3]);
                GatherLimits.put(entityType.name(), new int[]{limit, range, clearCount});
//            main.getLogger().info(ChatColor.GREEN + "成功设置 " + entityType.name() + " 的数量限制为 " + limit);
                main.getLogger().info(message.find("EntityCountSetOK").replace("%Count%",limit+"").replace("%Entity%",entityType.name()));
            }
            GatherBanWorlds.addAll(main.getConfig().getStringList("GatherEntityLimitCount.BanWorldNameList"));
        }










    }




}
