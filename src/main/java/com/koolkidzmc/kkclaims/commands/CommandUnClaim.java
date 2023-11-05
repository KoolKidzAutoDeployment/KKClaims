package com.koolkidzmc.kkclaims.commands;


import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.SoundAPI;
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

            if (!claims.isClaimed(chunk) || !claims.getClaimOwner(chunk).equals(player.getUniqueId())) {
                player.sendMessage(ColorAPI.formatString("&cYou must be standing in your claim to run this!"));
                SoundAPI.fail(player);
            } else {
                claims.removeClaim(chunk);
                player.sendMessage(ColorAPI.formatString("&aUnclaimed chunk!"));
                SoundAPI.success(player);
            }
        } else if (sender instanceof ConsoleCommandSender console) {
            console.sendMessage("This command must be run by a player!");
        }
        return true;
    }
}
