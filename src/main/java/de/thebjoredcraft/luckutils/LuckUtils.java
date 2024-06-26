package de.thebjoredcraft.luckutils;

import de.thebjoredcraft.luckutils.chat.ChatManager;
import de.thebjoredcraft.luckutils.tab.TabListManager;
import de.thebjoredcraft.luckutils.utils.EventManager;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class LuckUtils extends JavaPlugin {
    private static final int RESOURCE_ID = 112818;
    private static TabListManager tabListManager;
    public static String currentVersion;
    private static LuckUtils instance;


    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        currentVersion = getDescription().getVersion();
        tabListManager = new TabListManager();

        getServer().getPluginManager().registerEvents(new ChatManager(), this);
        getServer().getPluginManager().registerEvents(new EventManager(), this);
        saveDefaultConfig();


        if(getInstance().getConfig().getBoolean("LuckUtilsTablistEnabled")) {
            LuckUtils.getTabListManager().start();
            LuckUtils.getTabListManager().createGroupTeams();
        }

        if (isUpdateAvailable()) {
            getLogger().warning("There is an new Version available!");
        } else {
            getLogger().info("The Plugin is up-to-date");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "(LU) LuckUtils wird gestoppt!");
        // Plugin shutdown logic
    }

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
    public static String replacePlaceHolder(String string, Player player){
        boolean isregistered = player.hasPermission(LuckUtils.getInstance().getConfig().getString("RegisteredPermisson", ""));

        String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();
        String suffix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getSuffix();
        String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegisteredFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegisteredFormatOff", "");
        String serverName = LuckUtils.getInstance().getConfig().getString("ServerName", "");

        return string.replace("%player%", player.getName())
                .replace("%prefix%", prefix)
                .replace("%suffix%", suffix)
                .replace("%ping%", String.valueOf(player.getPing()))
                .replace("%registered%", registered)
                .replace("%servername%", serverName)
                .replace("%ip%", Bukkit.getIp())
                .replace("%online_players%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%max_players%", String.valueOf(Bukkit.getMaxPlayers()));
    }

    public static TabListManager getTabListManager() {
        return tabListManager;
    }
    public static LuckUtils getInstance() {
        return instance;
    }
}
