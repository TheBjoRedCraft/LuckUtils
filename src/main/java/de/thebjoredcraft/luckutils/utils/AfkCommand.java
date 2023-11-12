package de.thebjoredcraft.luckutils.utils;

import de.thebjoredcraft.luckutils.LuckUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AfkCommand implements CommandExecutor, TabCompleter {
    private final String[] subcommands = new String[]{"on","off"};

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player){

            Player player = (Player) commandSender;
            String prefix = LuckPermsProvider.get().getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrefix();

            boolean isregistered = player.hasPermission(LuckUtils.getInstance().getConfig().getString("Permisson", ""));
            Component registered = isregistered ? MiniMessage.miniMessage().deserialize(LuckUtils.getInstance().getConfig().getString("RegFormatOn")) : MiniMessage.miniMessage().deserialize(LuckUtils.getInstance().getConfig().getString("RegFormatOff"));

            if(args[0].equalsIgnoreCase("on")){

                String ChatMessageOnWithPlayerName = LuckUtils.getInstance().getConfig().getString("Chatmessageon", "").replace("%playername%", player.getName());
                String ChatMessageOnWithPlayerPrefix = ChatMessageOnWithPlayerName.replace("%luckperms_prefix%", prefix);

                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(ChatMessageOnWithPlayerPrefix));
                LuckUtils.setPermission(player, "lu.afk", true);

            }else if(args[0].equalsIgnoreCase("off")){

                String ChatMessageOffWithPlayerName = LuckUtils.getInstance().getConfig().getString("Chatmessageoff", "").replace("%playername%", player.getName());
                String ChatMessageOffWithPlayerPrefix = ChatMessageOffWithPlayerName.replace("%luckperms_prefix%", prefix);

                Bukkit.broadcast(MiniMessage.miniMessage().deserialize(ChatMessageOffWithPlayerPrefix));
                LuckUtils.setPermission(player, "lu.afk", false);

            }else{
                player.sendMessage("Usage: /afk <on|off>");

            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length <= 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(subcommands), completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
