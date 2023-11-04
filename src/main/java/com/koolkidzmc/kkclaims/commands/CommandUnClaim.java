package com.koolkidzmc.kkclaims.commands;


import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import org.bukkit.Chunk;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CommandUnClaim implements CommandExecutor {

    KKClaims plugin;
    ClaimManager claims;
    public CommandUnClaim(KKClaims plugin, ClaimManager claims) {
        this.plugin = plugin;
        this.claims = claims;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            Chunk chunk = player.getLocation().getChunk();

            if (!claims.getClaimOwner(chunk).equals(player.getUniqueId())) {
                player.sendMessage("This chunk is not your claim!");
            } else {
                claims.removeClaim(chunk);
            }
        } else if (sender instanceof ConsoleCommandSender console) {
            console.sendMessage("This command must be run by a player!");
        }
        return true;
    }
}
