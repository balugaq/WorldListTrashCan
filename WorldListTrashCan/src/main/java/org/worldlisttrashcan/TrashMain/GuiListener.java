package org.worldlisttrashcan.TrashMain;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.worldlisttrashcan.IsVersion.Is1_16_1_20Server;
import static org.worldlisttrashcan.WorldListTrashCan.GlobalTrashList;
import static org.worldlisttrashcan.WorldListTrashCan.main;
import static org.worldlisttrashcan.log.logFlag;
import static org.worldlisttrashcan.log.logToFile;


public class GuiListener implements Listener {

    List<Integer> int1_9 = Arrays.asList(45, 46, 47, 48, 49, 50, 51, 52, 53);




    //获取物品完整描述字符串
    public String getItemStackAllString(ItemStack itemStack){
        Material material = itemStack.getType();

        String item = "[material:"+material+" x "+itemStack.getAmount()+"]";

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {

            String customName = "";
            if(Is1_16_1_20Server){
                // 获取显示名（新版API）
                if (meta.hasDisplayName()) {
                    Component displayNameComponent = meta.displayName();
                    if (displayNameComponent != null) {
                        customName = "[name:"+PlainTextComponentSerializer.plainText().serialize(displayNameComponent)+"]";
                    }
                }
            }else {
                if(meta.hasDisplayName()){
                    customName = meta.getDisplayName();
                }
            }
            item += customName;

            List<String> loreList = new ArrayList<>();
            if(Is1_16_1_20Server){
                // 获取 lore（新版API 仍然是 List<Component>）
                if (meta.hasLore()) {
                    List<Component> lore = meta.lore();
                    if (lore != null) {
                        for (Component line : lore) {
                            loreList.add(PlainTextComponentSerializer.plainText().serialize(line));
                        }
                    }
                }
            }else {
                //旧版API
                if (meta.hasLore()) {
                    for (String lore : meta.getLore()) {
                        loreList.add(lore);
                    }
                }
            }
            if (!loreList.isEmpty()){
                item += "[lore:"+loreList.toString()+"]";
            }


            List<String> enchantList = new ArrayList<>();

            // 附魔
            Map<Enchantment, Integer> enchants = itemStack.getEnchantments();
            for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                enchantList.add(entry.getKey().getKey().getKey() + ":" + entry.getValue());
                // ↑ 获取 enchantment 的简短 ID，例如 "sharpness:5"
            }
            if (!enchantList.isEmpty()){
                item += "[enchant:"+enchantList.toString()+"]";
            }
        }
        return item;
    }



    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
//        System.out.println("0");
        // 检查事件是否发生在我们的菜单中

//        if (event.getView().getTitle().equals(mainGui.MainMenuName) || event.getView().getTitle().equals(mainGui.cauldronMenu.MenuName) ||
//                event.getView().getTitle().equals(mainGui.leatherChestplateMenu.MenuName) || event.getView().getTitle().equals(mainGui.netheriteChestplateMenu.MenuName) ||
//                event.getView().getTitle().equals(mainGui.oakSaplingMenu.MenuName) || event.getView().getTitle().equals(mainGui.tippedArrowMenu.MenuName)
//        ) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();


        String playerName = player.getName();



        int PageIndex = GlobalTrashList.indexOf(inventory);
        if(PageIndex!=-1){

            if(logFlag){

//                System.out.println("-----------");
//                System.out.println("event.getHotbarButton()"+event.getHotbarButton());
//                System.out.println("event.getSlot()"+event.getSlot());
//                System.out.println("event.getRawSlot()"+event.getRawSlot());
//                System.out.println("-----------");


//                event.getCursor();  玩家指针物品
//                event.getCurrentItem(); 玩家点击物品

                //玩家从鼠标上放入物品到箱子中
                //玩家点击的inventory就是公共垃圾桶的箱子
                if(event.getClickedInventory()!=null&&event.getClickedInventory().equals(inventory)) {
                    //玩家指针有物品
                    if (event.getCursor() != null && event.getCursor().getType()!=Material.AIR) {
//                        System.out.println("玩家从鼠标上放入物品到箱子中");
                        //事件发生后
//                        System.out.println("[+] "+getItemStackAllString(event.getCursor()));
                        String finalItem = getItemStackAllString(event.getCursor());
                        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                            logToFile(playerName, "+", finalItem);
                        });
                    }

                    //玩家的指针没有物品
                    if(event.getCursor() == null || event.getCursor().getType()==Material.AIR){
                        //没有按快捷键
                        if (event.getHotbarButton()==-1) {
                            //玩家点击的物品不为空
                            if(event.getCurrentItem()!=null&&event.getCurrentItem().getType()!=Material.AIR){
//                                System.out.println("玩家将箱子中的物品放到鼠标指针上");
//                                System.out.println("[-] "+getItemStackAllString(event.getCurrentItem()));
                                String finalItem = getItemStackAllString(event.getCurrentItem());
                                Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                                    logToFile(playerName, "-", finalItem);
                                });
                            }
                        }
                    }
                }
                //玩家使用快捷键，将箱子里的物品换到空的物品栏上
                //玩家点击的inventory就是公共垃圾桶的箱子
                if(event.getClickedInventory()!=null&&event.getClickedInventory().equals(inventory)){
                    //event.getHotbarButton是物品栏位置
                    //item1为箱子里的物品
                    ItemStack item1 = inventory.getItem(event.getSlot());

                    if (event.getHotbarButton()!=-1) {
                        //对应物品栏的物品为空
                        //item2为物品栏交换的物品
                        ItemStack item2 = player.getInventory().getItem(event.getHotbarButton());
                        if (item2==null||item2.getType()==Material.AIR) {
                            //对应箱子里的物品不为空
                            if (item1!=null&&item1.getType()!=Material.AIR) {
//                                System.out.println("玩家使用快捷键，将箱子里的物品换到空的物品栏上");
//                                System.out.println("[-] "+getItemStackAllString(item1));
                                String finalItem = getItemStackAllString(item1);
                                Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                                    logToFile(playerName, "-", finalItem);
                                });
                            }
                        }

                        //玩家使用快捷键，将箱子里的物品换到非空的物品栏上
                        if (item1!=null&&item1.getType()!=Material.AIR) {
                            if(item2!=null&&item2.getType()!=Material.AIR){
//                                System.out.println("玩家使用快捷键，将箱子里的物品换到不空的物品栏上");
//                                System.out.println("[-] "+getItemStackAllString(item1));
                                String finalItem = getItemStackAllString(item1);
                                Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                                    logToFile(playerName, "-", finalItem);
                                });
                            }
                        }

                        //如果对应的物品栏的物品不为空
                        if (item2!=null&&item2.getType()!=Material.AIR) {
//                            System.out.println("玩家使用快捷键，将物品栏的物品换到箱子中");
//                            System.out.println("[+] "+getItemStackAllString(item2));
                            String finalItem = getItemStackAllString(item2);
                            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                                logToFile(playerName, "+", finalItem);
                            });
                        }
                    }
                }
            }



            int count = event.getSlot();
            if(int1_9.contains(count)){
                event.setCancelled(true);
                //上一页
                if(count==46&&inventory.getItem(count).getType()==Material.ARROW){
                    player.openInventory(GlobalTrashList.get(PageIndex-1));
                //下一页
                }else if(count==52&&inventory.getItem(count).getType()==Material.ARROW){
                    player.openInventory(GlobalTrashList.get(PageIndex+1));
                }
            }
            // 取消玩家点击事件


//            Player player = (Player) event.getWhoClicked();
//            World world = player.getWorld();
//            Location location = player.getLocation();
//            // 检查点击的是哪个物品
//            ItemStack clickedItem = event.getCurrentItem();
//            if (clickedItem != null) {
//                if (event.getView().getTitle().equals(mainGui.getMenuName())) {
//                    if (clickedItem.getType() == Material.CAULDRON) {
//                        player.openInventory(mainGui.cauldronChangeMenu.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    } else if (clickedItem.getType() == Material.OAK_SAPLING) {
//                        player.openInventory(mainGui.oakSaplingMenu.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    } else if (clickedItem.getType() == Material.TIPPED_ARROW) {
//                        player.openInventory(mainGui.tippedArrowMenu.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    } else if (clickedItem.getType() == Material.LEATHER_CHESTPLATE) {
//                        player.openInventory(mainGui.leatherChestplateMenu.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    } else if (clickedItem.getType() == Material.NETHERITE_CHESTPLATE) {
//                        player.openInventory(mainGui.netheriteChestplateMenu.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//
//                }
//                if (event.getView().getTitle().equals(mainGui.armourMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getCauldronChangeMenu().getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                if (event.getView().getTitle().equals(mainGui.cauldronChangeMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }else if (clickedItem.getType() == Material.GOLDEN_HELMET) {
//                        player.openInventory(mainGui.getArmourMenu().getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }else if (clickedItem.getType() == Material.PAPER) {
//                        player.openInventory(mainGui.getCauldronMenu().getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                if (event.getView().getTitle().equals(mainGui.cauldronMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getCauldronChangeMenu().getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                if (event.getView().getTitle().equals(mainGui.leatherChestplateMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                if (event.getView().getTitle().equals(mainGui.netheriteChestplateMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                if (event.getView().getTitle().equals(mainGui.oakSaplingMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                if (event.getView().getTitle().equals(mainGui.tippedArrowMenu.getMenuName())) {
//                    if (clickedItem.getType() == Material.BARRIER) {
//                        player.openInventory(mainGui.getInventory());
//                        world.playSound(location, Sound.ENTITY_SPLASH_POTION_THROW, 1.0f, 1.0f);
//                        return;
//                    }
//                }
//                world.playSound(location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
//            }


        }

    }
}
