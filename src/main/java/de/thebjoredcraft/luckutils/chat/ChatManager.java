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
        event.setCancelled(true);
        Player player = event.getPlayer();

        User user = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player);
        String prefix = user.getCachedData().getMetaData().getPrefix();
        if(prefix != null) {

            boolean isregistered = player.hasPermission(LuckUtils.getInstance().getConfig().getString("Permisson", ""));
            String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegFormatOff", "");



            String ChatFormat = LuckUtils.getInstance().getConfig().getString("ChatFormat", "").replace("%playername%", player.getName());

            String ChatFormat2 = ChatFormat.replace("%luckperms_prefix%", prefix);


            String ChatFormat3 = ChatFormat2.replace("%message%", event.getMessage());

            String ChatFormat4 = ChatFormat3.replace("%registered%", registered);


            Bukkit.broadcast(MiniMessage.miniMessage().deserialize(ChatFormat4));
        }else{
            player.sendMessage("[LuckUtils] Der Prefix ist null, bitte erstelle eine LuckPerms-Gruppe mit Prefix um LuckUtils benutzten zu können!");
        }
    }
}
