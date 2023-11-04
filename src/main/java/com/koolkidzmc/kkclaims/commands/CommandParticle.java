package com.koolkidzmc.kkclaims.commands;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandParticle implements CommandExecutor, TabCompleter {

    KKClaims plugin;
    ClaimManager claims;
    public CommandParticle(KKClaims plugin, ClaimManager claims) {
        this.plugin = plugin;
        this.claims = claims;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) return false;

            Chunk chunk = player.getLocation().getChunk();
            Particle particle = Particle.valueOf(args[0]);
            if (!claims.getClaimOwner(chunk).equals(player.getUniqueId())) { player.sendMessage("You cannot change other players claim settings!");}
            else {
                claims.setClaimBorder(chunk, particle);
            }

        } else if (sender instanceof ConsoleCommandSender console) {
            console.sendMessage("This command must be run by a player!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        for (Particle particle : Particle.values()) {
            commands.add(particle.toString());
        }

        StringUtil.copyPartialMatches(args[0], commands, completions);
        Collections.sort(completions);
        return completions;
    }
}
