package com.koolkidzmc.kkclaims.gui;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ParticlesGUI extends FastInv {
    private boolean preventClose = false;
    ClaimManager claims;
    KKClaims plugin;
    public ParticlesGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&d&lKoolKidz &aClaiming"));
        this.plugin = plugin;
        this.claims = claims;
        int index = 0;
        for (Particle particle : Particle.values()) {
            Bukkit.broadcastMessage(particle.toString());
            if (particle == Particle.VILLAGER_HAPPY) {
            ItemStack item = new ItemStack(Material.FIREWORK_STAR, 1);
            ItemMeta im = item.getItemMeta();
            FireworkEffectMeta metaFw = (FireworkEffectMeta) im;
                FireworkEffect aa = FireworkEffect.builder().withColor(plugin.getConfig().getColor("borders." + particle.name() + ".color")).build();
            metaFw.setEffect(aa);
            item.setItemMeta(metaFw);
            Bukkit.broadcastMessage(particle.name());
            Bukkit.broadcastMessage(item.getItemMeta().getAsString());
            setItem(0, new ItemBuilder(item).name(ColorAPI.formatString(plugin.getConfig().getString("borders." + particle.name() + ".name"))).build(),
                    e -> {
                        claims.setClaimBorder(player.getChunk(), particle);
                    });
            ++index;
            }
        }
    }
}
