package de.thebjoredcraft.luckutils.tab;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;


@SuppressWarnings("UnreachableCode")
public class TabListManager {
    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    public static List<String> headers;
    public static List<String> footers;
    public static int headerIndex = 0;
    public static int footerIndex = 0;

    public void start(){
        headers = LuckUtils.getInstance().getConfig().getStringList("tablist.headers");
        footers = LuckUtils.getInstance().getConfig().getStringList("tablist.footers");

        if (headers.isEmpty() || footers.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>[LuckUtils]<bold>Warning! Header or/and Footer list is empty!"));
            return;
        }

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(count >= LuckUtils.getInstance().getConfig().getInt("SwitchTabList")){
                        count = 0;
                        updateTablist(player);

                        headerIndex = (headerIndex + 1) % headers.size();
                        footerIndex = (footerIndex + 1) % footers.size();
                    }
                    updateName(player);
                    assignPlayersToTeams();
                }
                count ++;
            }
        }.runTaskTimer(LuckUtils.getInstance(), 0, 5);
    }

    public void updateTablist(Player player) {
        boolean tablist = LuckUtils.getInstance().getConfig().getBoolean("LuckUtilsTablistEnabled");
        String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();

        if (prefix != null) {
            String headerTemplate = headers.get(headerIndex);
            String footerTemplate = footers.get(footerIndex);

            Component header = MiniMessage.miniMessage().deserialize(LuckUtils.replacePlaceHolder(headerTemplate, player));
            Component footer = MiniMessage.miniMessage().deserialize(LuckUtils.replacePlaceHolder(footerTemplate, player));

            if (tablist) {
                player.sendPlayerListHeader(header);
                player.sendPlayerListFooter(footer);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>[LuckUtils]<bold>Warning! The Prefix of the LuckPerms Group from " + player.getName() + " is null. Please add it to use LuckUtils! Disabling..."));
            Bukkit.getPluginManager().disablePlugin(LuckUtils.getInstance());
        }
    }
    public void updateName(Player player){
        boolean tablist = LuckUtils.getInstance().getConfig().getBoolean("LuckUtilsTablistEnabled");
        String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();

        if (prefix != null) {
            String nameTemplate = LuckUtils.getInstance().getConfig().getString("TablistName", "");

            Component name = MiniMessage.miniMessage().deserialize(LuckUtils.replacePlaceHolder(nameTemplate, player));

            if (tablist) {
                player.playerListName(name);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>[LuckUtils]<bold>Warning! The Prefix of the LuckPerms Group from " + player.getName() + " is null. Please add it to use LuckUtils! Disabling..."));
            Bukkit.getPluginManager().disablePlugin(LuckUtils.getInstance());
        }
    }

    public void createGroupTeams() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        if (luckPerms != null) {
            for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
                String groupName = group.getDisplayName();
                String prefix = group.getCachedData().getMetaData().getPrefix();
                if (group.getDisplayName() != null) {
                    Team team = scoreboard.getTeam(groupName);
                    if (team == null) {
                        team = scoreboard.registerNewTeam(groupName);
                        team.setDisplayName(groupName);
                        team.prefix(MiniMessage.miniMessage().deserialize(prefix + "<gray> | "));
                    }
                }else{
                    Bukkit.getConsoleSender().sendMessage("[LuckUtils] Der Displayname aller LuckPerms - Gruppen muss exestieren!");
                }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("[LuckUtils] LuckPerms not found, unable to create group teams.");
        }
    }
    public void createGroupTeam(String groupName) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        if (luckPerms != null) {
            Group group = luckPerms.getGroupManager().getGroup(groupName);
            String prefix = group.getCachedData().getMetaData().getPrefix();
            if (group != null) {
                Team team = scoreboard.getTeam(groupName);
                if (team == null) {
                    team = scoreboard.registerNewTeam(groupName);
                    team.setDisplayName(groupName);
                    team.prefix(MiniMessage.miniMessage().deserialize(prefix + "<gray> | "));
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("[LuckUtils] LuckPerms group not found: " + groupName);
                createGroupTeams();
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("[LuckUtils] LuckPerms not found, unable to create group team. Disabling...");
            LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
        }
    }

    public void assignPlayersToTeams() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Group group = LuckPermsProvider.get().getGroupManager().getGroup(LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getPrimaryGroup());

            if(group.getDisplayName() != null) {
                Team team = scoreboard.getTeam(group.getDisplayName());
                if (team != null) {
                    Team teamOld = scoreboard.getPlayerTeam(player);
                    if (teamOld != null) {
                        teamOld.removePlayer(player);
                    }
                    team.addPlayer(player);
                } else {
                    Bukkit.getConsoleSender().sendMessage("[LuckUtils] Team " + group.getDisplayName() + " does not exist, creating....");
                    createGroupTeams();

                }
            }else{
                Bukkit.getConsoleSender().sendMessage("[LuckUtils] Der Displayname der LuckPerms - Gruppe von " + player.getName() + " exestiert nicht, bitte erstelle eine LuckPerms-Gruppe mit Displayname um LuckUtils benutzten zu können! Disabling....");
                LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
            }
        }
    }
}
