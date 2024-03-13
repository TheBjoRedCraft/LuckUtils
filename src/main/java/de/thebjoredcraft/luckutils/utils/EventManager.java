package de.thebjoredcraft.luckutils.utils;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
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

        if (user != null) {
            String primaryGroup = user.getPrimaryGroup();
            if (primaryGroup != null) {
                String permission = "group." + LuckUtils.getInstance().getConfig().getString("default-group");

                Node node = Node.builder(permission).build();
                user.data().add(node);
                luckPerms.getUserManager().saveUser(user);
            }
        }
        event.joinMessage(MiniMessage.miniMessage().deserialize(LuckUtils.getInstance().getConfig().getString("JoinMessage", "")));
    }
    @EventHandler
    public void onLeft(PlayerQuitEvent event){
        event.quitMessage(MiniMessage.miniMessage().deserialize(LuckUtils.getInstance().getConfig().getString("QuitMessage", "")));
    }
}
