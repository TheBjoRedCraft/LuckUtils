package de.thebjoredcraft.luckutils.utils;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
    }
}
