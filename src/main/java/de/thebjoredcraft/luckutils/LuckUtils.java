package de.thebjoredcraft.luckutils;

import de.thebjoredcraft.luckutils.chat.ChatManager;
import de.thebjoredcraft.luckutils.tab.TabListManager;
import de.thebjoredcraft.luckutils.utils.AfkCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class LuckUtils extends JavaPlugin {
    private static LuckUtils instance;

    @Override
    public void onLoad() {
        instance = this;
    }
    public static LuckUtils getInstance() {
        return instance;
    }
    private static final String RESOURCE_LINK = "https://api.spigotmc.org/legacy/update.php?resource=112818";
    private static final int RESOURCE_ID = 112818;
    public static String currentVersion;

    public static boolean isUpdateAvailable() {
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String apiVersion = reader.readLine();
            reader.close();

            return !currentVersion.equals(apiVersion);
        } catch (IOException e) {
            LuckUtils.getInstance().getLogger().warning("Fehler beim Überprüfen auf Updates: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void onEnable() {

        getLogger().info(ChatColor.GREEN + "(LU) LuckUtils wird geladen!");
        getCommand("afk").setExecutor(new AfkCommand());

        getServer().getPluginManager().registerEvents(new ChatManager(), this);

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            getLogger().info("(LU) LuckPerms Plugin gefunden!");
        } else {
            getLogger().warning("(LU) LuckPerms Plugin nicht gefunden! Plugin wird deaktiviert.");
            getServer().getPluginManager().disablePlugin(this);
        }
        TabListManager.setupTablist();
        // Plugin startup logic
        saveDefaultConfig();
        if(getInstance().getConfig().getBoolean("UpdateTabList")){
            TabListManager.startTabupdate();
        }else{
            getLogger().info("(LU) §eAutoTablistUpdate is not enabled!");
        }
        // Plugin startup logic
        currentVersion = getDescription().getVersion();

        if (isUpdateAvailable()) {
            getLogger().warning("There is an new Version available!");
        } else {
            getLogger().info("The Plugin is up-to-date");
        }

    }

    @Override
    public void onDisable() {
        if(getInstance().getConfig().getBoolean("UpdateTabList")){
            TabListManager.stopTabListUpdate();
        }
        getLogger().info(ChatColor.RED + "(LU) LuckUtils wird gestoppt!");
        getLogger().info(ChatColor.GREEN + "(LU) Plugin wird gesaved!");
        // Plugin shutdown logic
    }

    public static void setPermission(Player player, String permission, Boolean trueorfalse) {
        PermissionAttachment attachment = player.addAttachment(LuckUtils.getInstance());
        attachment.setPermission(permission, trueorfalse);

        player.recalculatePermissions();
    }
}
