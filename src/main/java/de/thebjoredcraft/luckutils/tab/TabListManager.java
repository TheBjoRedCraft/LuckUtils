package de.thebjoredcraft.luckutils.tab;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class TabListManager {
    public static Boolean first;
    private static final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public static void setupTablist() {
        createGroupTeams();
        first = true;

    }

    public static void updateTablist() {
        assignPlayersToTeams();

        for (Player player : Bukkit.getOnlinePlayers()) {

            String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();

//            boolean isregistered = player.hasPermission(LuckUtils.getInstance().getConfig().getString("RegPermisson", ""));
//            String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegFormatOff", "");
//
//
//            boolean animated = LuckUtils.getInstance().getConfig().getBoolean("AnimatedTablist");
//
//            if (prefix != null) {
//                if (animated) {
//                    if (first) {
//                        String HeaderWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistHeader", "").replace("%player%", player.getName());
//                        String HeaderWithPrefix = HeaderWithName.replace("%prefix%", prefix);
//                        String HeaderWithRegistered = HeaderWithPrefix.replace("%registered%", registered);
//
//                        player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(HeaderWithRegistered));
//
//                        String PlayerListNameWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistPlayerName", "").replace("%player%", player.getName());
//                        String PlayerListNameWithPrefix = PlayerListNameWithName.replace("%prefix%", prefix);
//                        String PlayerListNameWithRegistered = PlayerListNameWithPrefix.replace("%registered%", registered);
//
//                        player.playerListName(MiniMessage.miniMessage().deserialize(PlayerListNameWithRegistered));
//
//                        String FooterWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistFooter", "").replace("%player%", player.getName());
//                        String FooterWithPrefix = FooterWithName.replace("%prefix%", prefix);
//                        String FooterWithRegistered = FooterWithPrefix.replace("%registered%", registered);
//
//                        player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(FooterWithRegistered));
//                    } else {
//                        String HeaderWithName = LuckUtils.getInstance().getConfig().getString("SecondTablistHeader", "").replace("%player%", player.getName());
//                        String HeaderWithPrefix = HeaderWithName.replace("%prefix%", prefix);
//                        String HeaderWithRegistered = HeaderWithPrefix.replace("%registered%", registered);
//
//                        player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(HeaderWithRegistered));
//
//                        String PlayerListNameWithName = LuckUtils.getInstance().getConfig().getString("SecondTablistPlayerName", "").replace("%player%", player.getName());
//                        String PlayerListNameWithPrefix = PlayerListNameWithName.replace("%prefix%", prefix);
//                        String PlayerListNameWithRegistered = PlayerListNameWithPrefix.replace("%registered%", registered);
//
//                        player.playerListName(MiniMessage.miniMessage().deserialize(PlayerListNameWithRegistered));
//
//                        String FooterWithName = LuckUtils.getInstance().getConfig().getString("SecondTablistFooter", "").replace("%player%", player.getName());
//                        String FooterWithPrefix = FooterWithName.replace("%prefix%", prefix);
//                        String FooterWithRegistered = FooterWithPrefix.replace("%registered%", registered);
//
//                        player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(FooterWithRegistered));
//                    }
//                } else {
//                    String HeaderWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistHeader", "").replace("%player%", player.getName());
//                    String HeaderWithPrefix = HeaderWithName.replace("%prefix%", prefix);
//                    String HeaderWithRegistered = HeaderWithPrefix.replace("%registered%", registered);
//
//                    player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(HeaderWithRegistered));
//
//                    String PlayerListNameWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistPlayerName", "").replace("%player%", player.getName());
//                    String PlayerListNameWithPrefix = PlayerListNameWithName.replace("%prefix%", prefix);
//                    String PlayerListNameWithRegistered = PlayerListNameWithPrefix.replace("%registered%", registered);
//
//                    player.playerListName(MiniMessage.miniMessage().deserialize(PlayerListNameWithRegistered));
//
//                    String FooterWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistFooter", "").replace("%player%", player.getName());
//                    String FooterWithPrefix = FooterWithName.replace("%prefix%", prefix);
//                    String FooterWithRegistered = FooterWithPrefix.replace("%registered%", registered);
//
//                    player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(FooterWithRegistered));
//                }
//            } else {
//                Bukkit.getConsoleSender().sendMessage("[LuckUtils] Der Prefix der LuckPerms - Gruppe von " + player.getName() + " exestiert nicht, bitte erstelle eine LuckPerms-Gruppe mit Prefix um LuckUtils benutzten zu können! Disabling...");
//                LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
//            }
        }
    }

    private static BukkitRunnable runnable;

    public static void startTabupdate() {
        try {

            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    updateTablist();
                }
            };
            BukkitTask bukkitTask = runnable.runTaskTimer(LuckUtils.getInstance(), 0, LuckUtils.getInstance().getConfig().getInt("Repeat"));

            BukkitRunnable runnableOther = new BukkitRunnable() {
                @Override
                public void run() {
                    first = !first;
                }
            };
            BukkitTask bukkitTaskOther = runnableOther.runTaskTimer(LuckUtils.getInstance(), 0, LuckUtils.getInstance().getConfig().getInt("SwitchTabList"));
        } catch (NumberFormatException exception) {
            Bukkit.getConsoleSender().sendMessage(exception.getMessage());
        }
    }

    public static void stopTabListUpdate() {
        try {
            if (!runnable.isCancelled()) {
                runnable.cancel();
            }
        } catch (Exception exception) {
            Bukkit.getConsoleSender().sendMessage("ERROR in stopTabUpdate");
        }
    }

    private static void createGroupTeams() {
        LuckPerms luckPerms = LuckPermsProvider.get();
        if (luckPerms != null) {
            for (Group group : luckPerms.getGroupManager().getLoadedGroups()) {
                String groupName = group.getDisplayName();
                if (group.getDisplayName() != null) {
                    Team team = scoreboard.getTeam(groupName);
                    if (team == null) {
                        team = scoreboard.registerNewTeam(groupName);
                        team.setDisplayName(groupName);
                    }
                }else{
                    Bukkit.getConsoleSender().sendMessage("[LuckUtils] Der Displayname aller LuckPerms - Gruppen muss exestieren!");
                }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("[LuckUtils] LuckPerms not found, unable to create group teams.");
        }
    }
    private static void createGroupTeam(String groupName) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        if (luckPerms != null) {
            Group group = luckPerms.getGroupManager().getGroup(groupName);
            if (group != null) {
                Team team = scoreboard.getTeam(groupName);
                if (team == null) {
                    team = scoreboard.registerNewTeam(groupName);
                    team.setDisplayName(groupName);
                }
            } else {
                Bukkit.getConsoleSender().sendMessage("[LuckUtils] LuckPerms group not found: " + groupName);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("[LuckUtils] LuckPerms not found, unable to create group team. Disabling...");
            LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
        }
    }

    private static void assignPlayersToTeams() {
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
                    createGroupTeam(group.getDisplayName());

                }
            }else{
                Bukkit.getConsoleSender().sendMessage("[LuckUtils] Der Displayname der LuckPerms - Gruppe von " + player.getName() + " exestiert nicht, bitte erstelle eine LuckPerms-Gruppe mit Displayname um LuckUtils benutzten zu können! Disabling....");
                LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
            }
        }
    }
}
