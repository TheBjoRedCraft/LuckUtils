package de.thebjoredcraft.luckutils.tab;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class TabListManager {
    public static Boolean first;
    public static void setupTablist(){
        first = true;
    }
    public static void updateTablist(){
        for (Player player : Bukkit.getOnlinePlayers()){
            String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();
            int ping = player.getPing();

            boolean isregistered = player.hasPermission(LuckUtils.getInstance().getConfig().getString("RegPermisson", ""));
            String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegFormatOff", "");


            boolean animated = LuckUtils.getInstance().getConfig().getBoolean("AnimatedTablist");

            if(prefix != null) {
                if(animated) {
                    if (first) {
                        String HeaderWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistHeader", "").replace("%playername%", player.getName());
                        String HeaderWithPrefix = HeaderWithName.replace("%prefix%", prefix);
                        String HeaderWithRegistered = HeaderWithPrefix.replace("%registered%", registered);

                        player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(HeaderWithRegistered));

                        String PlayerListNameWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistPlayerName", "").replace("%playername%", player.getName());
                        String PlayerListNameWithPrefix = PlayerListNameWithName.replace("%prefix%", prefix);
                        String PlayerListNameWithRegistered = PlayerListNameWithPrefix.replace("%registered%", registered);

                        player.playerListName(MiniMessage.miniMessage().deserialize(PlayerListNameWithRegistered));

                        String FooterWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistFooter", "").replace("%playername%", player.getName());
                        String FooterWithPrefix = FooterWithName.replace("%prefix%", prefix);
                        String FooterWithRegistered = FooterWithPrefix.replace("%registered%", registered);

                        player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(FooterWithRegistered));
                    }else{
                        String HeaderWithName = LuckUtils.getInstance().getConfig().getString("SecondTablistHeader", "").replace("%playername%", player.getName());
                        String HeaderWithPrefix = HeaderWithName.replace("%prefix%", prefix);
                        String HeaderWithRegistered = HeaderWithPrefix.replace("%registered%", registered);

                        player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(HeaderWithRegistered));

                        String PlayerListNameWithName = LuckUtils.getInstance().getConfig().getString("SecondTablistPlayerName", "").replace("%playername%", player.getName());
                        String PlayerListNameWithPrefix = PlayerListNameWithName.replace("%prefix%", prefix);
                        String PlayerListNameWithRegistered = PlayerListNameWithPrefix.replace("%registered%", registered);

                        player.playerListName(MiniMessage.miniMessage().deserialize(PlayerListNameWithRegistered));

                        String FooterWithName = LuckUtils.getInstance().getConfig().getString("SecondTablistFooter", "").replace("%playername%", player.getName());
                        String FooterWithPrefix = FooterWithName.replace("%prefix%", prefix);
                        String FooterWithRegistered = FooterWithPrefix.replace("%registered%", registered);

                        player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(FooterWithRegistered));
                    }
                }else{
                    String HeaderWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistHeader", "").replace("%playername%", player.getName());
                    String HeaderWithPrefix = HeaderWithName.replace("%prefix%", prefix);
                    String HeaderWithRegistered = HeaderWithPrefix.replace("%registered%", registered);

                    player.sendPlayerListHeader(MiniMessage.miniMessage().deserialize(HeaderWithRegistered));

                    String PlayerListNameWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistPlayerName", "").replace("%playername%", player.getName());
                    String PlayerListNameWithPrefix = PlayerListNameWithName.replace("%prefix%", prefix);
                    String PlayerListNameWithRegistered = PlayerListNameWithPrefix.replace("%registered%", registered);

                    player.playerListName(MiniMessage.miniMessage().deserialize(PlayerListNameWithRegistered));

                    String FooterWithName = LuckUtils.getInstance().getConfig().getString("FirstTablistFooter", "").replace("%playername%", player.getName());
                    String FooterWithPrefix = FooterWithName.replace("%prefix%", prefix);
                    String FooterWithRegistered = FooterWithPrefix.replace("%registered%", registered);

                    player.sendPlayerListFooter(MiniMessage.miniMessage().deserialize(FooterWithRegistered));
                }
            }else{
                Bukkit.getConsoleSender().sendMessage("[LuckUtils] Der Prefix der LuckPerms - Gruppe von "+ player.getName() +" exestiert nicht, bitte erstelle eine LuckPerms-Gruppe mit Prefix um LuckUtils benutzten zu können!");
            }
        }
    }
    private static BukkitRunnable runnable;
    private static BukkitRunnable runnableOther;

    public static void startTabupdate() {
        try {

            runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    updateTablist();
                }
            };
            BukkitTask bukkitTask = runnable.runTaskTimer(LuckUtils.getInstance(), 0, LuckUtils.getInstance().getConfig().getInt("Repeat"));

            runnableOther = new BukkitRunnable() {
                @Override
                public void run() {
                    first = !first;
                }
            };
            BukkitTask bukkitTaskOther = runnableOther.runTaskTimer(LuckUtils.getInstance(), 0, LuckUtils.getInstance().getConfig().getInt("SwitchTabList"));
        }catch (NumberFormatException exception){
            Bukkit.getConsoleSender().sendMessage(exception.getMessage());
        }
    }
    public static void stopTabListUpdate(){
        try {
            if(!runnable.isCancelled()) {
                runnable.cancel();
            }
        } catch(Exception exception) {
            Bukkit.getConsoleSender().sendMessage("ERROR in stopTabUpdate");
        }
    }
}
