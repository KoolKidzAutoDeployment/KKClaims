package com.koolkidzmc.kkclaims.listeners;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PreventionListener implements Listener
{
    KKClaims plugin;
    ClaimManager claims;
    public PreventionListener(KKClaims plugin, ClaimManager claims)
    {
        this.plugin = plugin;
        this.claims = claims;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null)
        {
            Chunk chunk = event.getClickedBlock().getChunk();

            if (claims.isClaimed(chunk))
            {
                Player player = event.getPlayer();

                if (!claims.getClaimOwner(chunk).equals(player.getUniqueId()))
                {
                    if (!player.isOp())
                    {
                        event.setCancelled(true);
                        player.sendMessage("You are not allowed to build here.");
                    }
                }
            }
        }
    }
}
