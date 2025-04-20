package org.worldlisttrashcan;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.worldlisttrashcan.TrashMain.RashCanInformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.worldlisttrashcan.WorldListTrashCan.WorldToLocation;
import static org.worldlisttrashcan.WorldListTrashCan.main;

public class log {

    public static boolean logFlag = false;


    //标准日志
    public static void logToFile(String playerName, String action, String itemName) {
        try {
            // 当前日期（用于文件名）和时间（用于日志行）
            LocalDateTime now = LocalDateTime.now();
            String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy_M_d"));
            String timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            // 创建 logs 文件夹路径
            File logFolder = new File(main.getDataFolder(), "logs");
            if (!logFolder.exists()) {
                logFolder.mkdirs(); // 如果 logs 文件夹不存在就创建
            }
            // 构造文件路径：插件目录下的日志文件，如 2025_4_20.txt
            File file = new File(logFolder, dateStr + ".txt");


            // 日志行格式
            String logLine = String.format("[%s|%s] [%s] [Item:%s] ", timeStr, playerName, action, itemName);

            // 追加写入文件
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(logLine);
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (IOException e) {
            main.getLogger().warning("写入日志文件失败: " + e.getMessage());
        }
    }

    //自定义日志
    public static void customLogToFile(String customStr) {
        try {
            // 当前日期（用于文件名）和时间（用于日志行）
            LocalDateTime now = LocalDateTime.now();
            String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy_M_d"));
            String timeStr = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            // 创建 logs 文件夹路径
            File logFolder = new File(main.getDataFolder(), "logs");
            if (!logFolder.exists()) {
                logFolder.mkdirs(); // 如果 logs 文件夹不存在就创建
            }
            // 构造文件路径：插件目录下的日志文件，如 2025_4_20.txt
            File file = new File(logFolder, dateStr + ".txt");


            // 日志行格式
            String logLine = String.format("[%s] %s", timeStr,customStr);

            // 追加写入文件
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(logLine);
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (IOException e) {
            main.getLogger().warning("写入日志文件失败: " + e.getMessage());
        }
    }


}
