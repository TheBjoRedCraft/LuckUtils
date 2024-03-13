package de.thebjoredcraft.luckutils;

import net.kyori.adventure.text.minimessage.MiniMessage;
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

public class LuckUtilsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player player){
            if(args.length == 1 && args[0].equalsIgnoreCase("disable")){
                player.sendMessage(MiniMessage.miniMessage().deserialize("<color:#3b92d1>LuckUtils wird disabled... - LuckUtils will be disabled...."));
                player.sendMessage(MiniMessage.miniMessage().deserialize("<color:#3b92d1>disabling...."));

                LuckUtils.getInstance().getServer().getPluginManager().disablePlugin(LuckUtils.getInstance());
            }
        }
        return false;
    }
    private final String[] subcommands = new String[]{"update", "disable"};
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
