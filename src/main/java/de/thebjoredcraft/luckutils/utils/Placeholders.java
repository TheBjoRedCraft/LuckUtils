package de.thebjoredcraft.luckutils.utils;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.entity.Player;

public class Placeholders {
    public static String formatPlayerPlaceHolder(String args, Player target){
        return args.replace("%playername%", target.getName());
    }
    public static String formatPlayerDisplayNamePlaceHolder(String args, Player target){
        return args.replace("%playerdisplayname%", target.getDisplayName());
    }
    public static String formatAfkPlayerDisplayNameRegisteredPrefix(String args, Player target){
        boolean isregistered = target.hasPermission(LuckUtils.getInstance().getConfig().getString("RegPermisson", ""));
        String registered = isregistered ? LuckUtils.getInstance().getConfig().getString("RegFormatOn", "") : LuckUtils.getInstance().getConfig().getString("RegFormatOff", "");

        boolean isAfk = target.hasPermission(LuckUtils.getInstance().getConfig().getString("AfkPermisson", ""));
        String afk = isAfk ? LuckUtils.getInstance().getConfig().getString("TabFormaton", "") : LuckUtils.getInstance().getConfig().getString("TabFormatoff", "");

        String withAfk = args.replace("%afk%", afk);
        String withDisplayName = withAfk.replace("%displayname%", target.getDisplayName());
        String withRegistered = withDisplayName.replace("%registered%", registered);

        return withRegistered.replace("%prefix%", LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(target).getCachedData().getMetaData().getPrefix());
    }
}
