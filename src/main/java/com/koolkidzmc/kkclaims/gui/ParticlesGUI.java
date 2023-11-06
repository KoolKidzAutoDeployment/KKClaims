package com.koolkidzmc.kkclaims.gui;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

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
            if (index == 45) return;
            ItemStack item = new ItemStack(Material.FIREWORK_STAR, 1);
            FireworkMeta fm = (FireworkMeta) item.getItemMeta();
            fm.addEffect(FireworkEffect.builder()
                    .withColor(Color.LIME)
                    .build());
            item.setItemMeta(fm);
            setItem(index, new ItemBuilder(item).name(ColorAPI.formatString("&f" + particle.name())).build(),
                    e -> {
                        claims.setClaimBorder(player.getChunk(), particle);
                    });
            ++index;
        }
    }
}
