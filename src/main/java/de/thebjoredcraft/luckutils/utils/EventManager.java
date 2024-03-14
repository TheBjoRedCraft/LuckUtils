package de.thebjoredcraft.luckutils.utils;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        int ping = event.getPlayer().getPing();
        boolean isregistered = event.getPlayer().hasPermission(LuckUtils.getInstance().getConfig().getString("RegisteredPermisson", ""));

        String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegisteredFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegisteredFormatOff", "");
        String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(event.getPlayer()).getCachedData().getMetaData().getPrefix();

        if (user != null) {
            String primaryGroup = user.getPrimaryGroup();
            if (primaryGroup != null) {
                String permission = "group." + LuckUtils.getInstance().getConfig().getString("default-group");

                Node node = Node.builder(permission).build();
                user.data().add(node);
                luckPerms.getUserManager().saveUser(user);
            }
        }
        if(LuckUtils.getInstance().getConfig().getBoolean("JoinMessageEnabled")) {
            if (prefix != null) {
                event.joinMessage(MiniMessage.miniMessage().deserialize(LuckUtils.getInstance().getConfig().getString("QuitMessage", "")
                        .replace("%player%", event.getPlayer().getDisplayName()).replace("%prefix%", prefix).replace("%registered%", registered).replace("%ping%", String.valueOf(ping))));
            } else {
                Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>[LuckUtils]<bold> While setting the join message: The Prefix of the LuckPerms Group of player " + player.getName() + " is null, pls add it to use LuckUtils! Disabling..."));
                Bukkit.getPluginManager().disablePlugin(LuckUtils.getInstance());
            }
        }
    }
    @EventHandler
    public void onLeft(PlayerQuitEvent event){
        int ping = event.getPlayer().getPing();
        boolean isregistered = event.getPlayer().hasPermission(LuckUtils.getInstance().getConfig().getString("RegisteredPermisson", ""));

        String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegisteredFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegisteredFormatOff", "");
        String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(event.getPlayer()).getCachedData().getMetaData().getPrefix();

        if(LuckUtils.getInstance().getConfig().getBoolean("QuitMessageEnabled")) {
            if (prefix != null) {
                event.quitMessage(MiniMessage.miniMessage().deserialize(LuckUtils.getInstance().getConfig().getString("QuitMessage", "")
                        .replace("%player%", event.getPlayer().getDisplayName()).replace("%prefix%", prefix).replace("%registered%", registered).replace("%ping%", String.valueOf(ping))));
            } else {
                Bukkit.getConsoleSender().sendMessage(MiniMessage.miniMessage().deserialize("<red>[LuckUtils]<bold> While setting the quit message: The Prefix of the LuckPerms Group of player " + event.getPlayer().getName() + " is null, pls add it to use LuckUtils! Disabling..."));
                Bukkit.getPluginManager().disablePlugin(LuckUtils.getInstance());
            }
        }
    }
}
