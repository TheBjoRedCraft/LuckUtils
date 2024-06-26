package de.thebjoredcraft.luckutils.chat;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(LuckUtils.getInstance().getConfig().getBoolean("LuckUtilsChatEnabled")) {
            event.setCancelled(true);
            Player player = event.getPlayer();

            User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
            String prefix = user.getCachedData().getMetaData().getPrefix();
            String suffix = user.getCachedData().getMetaData().getSuffix();
            if (prefix != null && suffix != null) {

                boolean isregistered = player.hasPermission(LuckUtils.getInstance().getConfig().getString("RegisteredPermission", ""));
                String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegisteredFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegisteredFormatOff", "");
                String message = LuckUtils.getInstance().getConfig().getString("ChatFormat", "").replace("%player%", player.getName()).replace("%prefix%", prefix).replace("%message%", event.getMessage()).replace("%registered%", registered).replace("%suffix%", suffix);

                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message));
            } else {
                player.sendMessage("[LuckUtils] Der Prefix ODER Suffix ist null, bitte erstelle eine LuckPerms-Gruppe mit Prefix um LuckUtils benutzten zu können! Disabling...");
                LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
            }
        }
    }
}
