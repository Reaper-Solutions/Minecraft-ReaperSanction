package main.java.fr.farmeurimmo.reapersanction.cmd;

import main.java.fr.farmeurimmo.reapersanction.MessageManager;
import main.java.fr.farmeurimmo.reapersanction.ReaperSanction;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VanishCmd implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg3) {
        if (sender instanceof Player) {
            if (ReaperSanction.instance.getConfig().getBoolean("Vanish.Enabled")) {
                Player p = (Player) sender;
                if (ReaperSanction.vanished.contains(p)) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.showPlayer(p);
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        if (p.getGameMode() == GameMode.ADVENTURE) {
                            p.setAllowFlight(false);
                        }
                        if (p.getGameMode() == GameMode.SURVIVAL) {
                            p.setAllowFlight(false);
                        }
                        if (ReaperSanction.instance.getConfig().getBoolean("Vanish.ExitGamemode")) {
                            if (ReaperSanction.instance.getConfig().getInt("Vanish.ExGamemode") == 1) {
                                p.setGameMode(GameMode.CREATIVE);
                                p.setAllowFlight(true);
                                p.setFlying(true);
                            }
                            if (ReaperSanction.instance.getConfig().getInt("Vanish.ExGamemode") == 2) {
                                p.setGameMode(GameMode.ADVENTURE);
                            }
                            if (ReaperSanction.instance.getConfig().getInt("Vanish.ExGamemode") == 3) {
                                p.setGameMode(GameMode.SPECTATOR);
                                p.setAllowFlight(true);
                                p.setFlying(true);

                            }
                            if (ReaperSanction.instance.getConfig().getInt("Vanish.ExGamemode") == 0) {
                                p.setGameMode(GameMode.SURVIVAL);
                            }
                        }
                    }
                    ReaperSanction.vanished.remove(p);
                    p.sendMessage(MessageManager.instance.getMessage("Vanish-Isoff"));
                } else {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.hidePlayer(p);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 1));
                    }
                    ReaperSanction.vanished.add(p);
                    p.sendMessage(MessageManager.instance.getMessage("Vanish-Ison"));
                    if (ReaperSanction.instance.getConfig().getBoolean("Vanish.ChangeGamemode")) {
                        if (ReaperSanction.instance.getConfig().getInt("Vanish.Gamemode") == 1) {
                            p.setGameMode(GameMode.CREATIVE);
                        }
                        if (ReaperSanction.instance.getConfig().getInt("Vanish.Gamemode") == 2) {
                            p.setGameMode(GameMode.ADVENTURE);
                        }
                        if (ReaperSanction.instance.getConfig().getInt("Vanish.Gamemode") == 3) {
                            p.setGameMode(GameMode.SPECTATOR);
                        }
                        if (ReaperSanction.instance.getConfig().getInt("Vanish.Gamemode") == 0) {
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                    if (ReaperSanction.instance.getConfig().getBoolean("Vanish.Fly")) {
                        if (!p.getAllowFlight()) {
                            p.setAllowFlight(true);
                            p.setFlying(true);
                        }
                    }
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        ArrayList<String> subcmd = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("vanish")) {
            subcmd.add("");
            Collections.sort(subcmd);
        }
        return subcmd;
    }
}
