package com.koolkidzmc.kkclaims.commands;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.gui.ClaimGUI;
import com.koolkidzmc.kkclaims.gui.ProfilesGUI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.SoundAPI;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandClaim implements CommandExecutor, TabCompleter {
    KKClaims plugin;
    ClaimManager claims;
    public CommandClaim(KKClaims plugin, ClaimManager claims) {
        this.plugin = plugin;
        this.claims = claims;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                SoundAPI.success(player);
                new ClaimGUI(plugin, claims, player).open(player);
                return true;
            } if (args[0].equalsIgnoreCase("gui")) {
                SoundAPI.success(player);
                new ClaimGUI(plugin, claims, player).open(player);
                return true;
            } if (args[0].equalsIgnoreCase("profiles")) {
                SoundAPI.success(player);
                new ProfilesGUI(plugin, claims, player).open(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        commands.add("gui");
        commands.add("profiles");


        StringUtil.copyPartialMatches(args[0], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}
