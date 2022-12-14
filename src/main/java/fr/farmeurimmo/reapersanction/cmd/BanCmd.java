package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.ConfigManager;
import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.sanctions.ApplySanction;
import main.java.fr.farmeurimmo.reapersanction.utils.TimeConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class BanCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Calendar calendar = Calendar.getInstance();
        if (args.length == 0) {
            sender.sendMessage(MessageManager.prefix +
                    MessageManager.instance.getMessage("ErrorBanArg"));
        } else if (args.length == 1) {
            Date Mydate = new Date(System.currentTimeMillis());
            Player p = Bukkit.getPlayer(args[0]);
            String reason = MessageManager.instance.getMessage("UnkownReasonSpecified");
            assert p != null;
            if (p.isOnline()) {
                p.kickPlayer(ConfigManager.instance.getFromConfigFormatted("Ban.lines")
                        .replace("%banner%", sender.getName())
                        .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                        .replace("%reason%", reason.trim()));
            }
            ApplySanction.instance.ApplyPermaBan(p, reason, sender.getName(),
                    TimeConverter.getFormatTimeWithTZ(Mydate));
        } else {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player p = Bukkit.getPlayer(args[0]);
                StringBuilder sb = new StringBuilder();
                for (String s : args) {
                    sb.append(s).append(' ');
                }
                String reason = sb.toString().replace(args[0] + " ", "").trim();
                assert p != null;
                if (p.isOnline()) {
                    p.kickPlayer(ConfigManager.instance.getFromConfigFormatted("Ban.lines")
                            .replace("%banner%", sender.getName())
                            .replace("%date%", TimeConverter.getFormatTimeWithTZ(calendar.getTime()))
                            .replace("%reason%", reason));
                }
                Date Mydate = new Date(System.currentTimeMillis());
                ApplySanction.instance.ApplyPermaBan(p, reason, sender.getName(),
                        TimeConverter.getFormatTimeWithTZ(Mydate));
            } else {
                sender.sendMessage(MessageManager.prefix +
                        MessageManager.instance.getMessage("InvalidPlayer"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (args.length == 1) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    subcmd.add(player.getName());
                }
            } else if (args.length >= 2) {
                subcmd.add("");
            }
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}
