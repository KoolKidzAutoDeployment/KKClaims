package com.koolkidzmc.kkclaims.commands;


import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import org.bukkit.Chunk;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CommandForceClaim implements CommandExecutor {

    KKClaims plugin;
    ClaimManager claims;
    public CommandForceClaim(KKClaims plugin, ClaimManager claims) {
        this.plugin = plugin;
        this.claims = claims;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            Chunk chunk = player.getLocation().getChunk();

            if (claims.isClaimed(chunk)) {
                player.sendMessage("This chunk is already claimed");
            } else {
                claims.createClaim(chunk, player.getUniqueId());
            }
        } else if (sender instanceof ConsoleCommandSender console) {
            console.sendMessage("This command must be run by a player!");
        }
        return true;
    }


}
