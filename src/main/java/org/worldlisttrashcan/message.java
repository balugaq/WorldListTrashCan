package org.worldlisttrashcan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.worldlisttrashcan.WorldListTrashCan.*;

public class message {

    private static File messageFile;

    private static ConfigurationSection configurationSection;
    private static FileConfiguration Message;

    public static FileConfiguration getConfig() {
        return Message;
    }


    static public String chanceMessage;
    public static void reloadMessage() {
        //new File(main.getDataFolder(), "data" + File.separator + "data.yml");
        messageFile = new File(main.getDataFolder(), "message"+ File.separator +chanceMessage);
        if (!messageFile.exists())
            main.saveResource("message"+ File.separator +chanceMessage, false);
        Message = YamlConfiguration.loadConfiguration(messageFile);
        ConfigStringReplace(message.getConfig(),"&","§");
        ConfigStringReplace(message.getConfig(),"%PluginTitle%",message.find("PluginTitle"));
//        message.saveData();

    }



    public static void AllMessageLoad(){
        List<String> LangList = new ArrayList<>();
        LangList.add("message_zh.yml");LangList.add("message_en.yml");LangList.add("message_zh_TW.yml");
        for (String TheMessage : LangList) {
            File LangFile = new File(main.getDataFolder(), "message"+ File.separator + TheMessage);
            if (!LangFile.exists())
                main.saveResource("message"+ File.separator +TheMessage, false);
        }









    }

    public static String find(String path){
        if(message.getConfig().getString(path)!=null&&!message.getConfig().getString(path).isEmpty()){
            return message.getConfig().getString(path);
        }else {
            main.getLogger().info(message.find("NotFindMessageSlave").replace("%path%",path));
            return " ";
        }
    }

    public static void saveData() {
        try {
            Message.save(messageFile);
        } catch (IOException var2) {
            Bukkit.getLogger().info(message.find("NotFindMessage"));
        }
    }
}
