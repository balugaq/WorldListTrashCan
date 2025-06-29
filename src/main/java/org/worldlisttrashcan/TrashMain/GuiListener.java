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

import java.util.*;

import static org.worldlisttrashcan.IsVersion.*;
import static org.worldlisttrashcan.Method.Method.getItemStackAllString;
import static org.worldlisttrashcan.WorldListTrashCan.GlobalTrashList;
import static org.worldlisttrashcan.WorldListTrashCan.main;
import static org.worldlisttrashcan.log.*;


public class GuiListener implements Listener {









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


//        System.out.println("-----------");
//        System.out.println("event.getHotbarButton()"+event.getHotbarButton());
//        System.out.println("event.getSlot()"+event.getSlot());
//        System.out.println("event.getRawSlot()"+event.getRawSlot());
//        System.out.println("-----------");

        String playerName = player.getName();



        int PageIndex = GlobalTrashList.indexOf(inventory);
        if(PageIndex!=-1){

            //无论如何都不允许玩家直接拖动，所以直接取消
            event.setCancelled(true);



            int count = event.getRawSlot();
            if(count>=45 && count<=53){
                //上一页
                if(count==46&&inventory.getItem(count).getType()==Material.ARROW){
                    player.openInventory(GlobalTrashList.get(PageIndex-1));
                //下一页
                }else if(count==52&&inventory.getItem(count).getType()==Material.ARROW){
                    player.openInventory(GlobalTrashList.get(PageIndex+1));
                }
            }else if (count>=0 && count<=44) {
                ItemStack itemStack = inventory.getItem(count);
                if (itemStack != null && itemStack.getType() != Material.AIR) {
                    //记录原数量
                    int amount = itemStack.getAmount();
                    //如果只加了一半，也返回false
                    HashMap<Integer, ItemStack> integerItemStackHashMap = player.getInventory().addItem(itemStack);
                    if (integerItemStackHashMap.isEmpty()) {
                        String finalItem = getItemStackAllString(itemStack,amount);
                        itemStack.setAmount(0);
                        if(logFlag) {
                            startLogToFileTask(playerName, "-", finalItem);
                        }
                    }else {
                        //没加进去的物品
                        ItemStack itemStack1 = integerItemStackHashMap.get(0);
                        //剩余物品数量
                        int shengyu = itemStack1.getAmount();
                        if (amount-shengyu>0){
                            String finalItem = getItemStackAllString(itemStack,amount-shengyu);

                            if(logFlag){
                                startLogToFileTask(playerName, "-", finalItem);
                            }
                        }
                    }
                }

            }
            // 取消玩家点击事件


        }

    }
}
