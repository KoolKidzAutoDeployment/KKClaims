package com.koolkidzmc.kkclaims.listeners;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    KKClaims plugin;
    ClaimManager claims;
    public JoinListener(KKClaims plugin, ClaimManager claims) {
        this.plugin = plugin;
        this.claims = claims;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (claims.playerHasProfiles(event.getPlayer().getUniqueId())) return;
        claims.createGlobalProfile(event.getPlayer().getUniqueId());
    }
}
