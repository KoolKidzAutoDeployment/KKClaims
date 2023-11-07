package com.koolkidzmc.kkclaims.gui;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.ItemBuilder;
import org.bukkit.*;
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
            String confColor = plugin.getConfig().getString("borders." + particle.name() + ".color");
                FireworkEffect aa = FireworkEffect.builder()
                        .withColor(getConfColor(confColor)).build();
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

    public Color getConfColor(String color) {
        if (color.equalsIgnoreCase("lime")) return Color.LIME;
        if (color.equalsIgnoreCase("black")) return Color.BLACK;
        if (color.equalsIgnoreCase("aqua")) return Color.AQUA;
        if (color.equalsIgnoreCase("blue")) return Color.BLUE;
        if (color.equalsIgnoreCase("green")) return Color.GREEN;
        if (color.equalsIgnoreCase("gray")) return Color.GRAY;
        if (color.equalsIgnoreCase("purple")) return Color.PURPLE;
        if (color.equalsIgnoreCase("magenta")) return Color.FUCHSIA;
        if (color.equalsIgnoreCase("white")) return Color.WHITE;
        if (color.equalsIgnoreCase("dark blue")) return Color.NAVY;
        if (color.equalsIgnoreCase("yellow")) return Color.YELLOW;
        if (color.equalsIgnoreCase("red")) return Color.RED;
        if (color.equalsIgnoreCase("orange")) return Color.ORANGE;
        return Color.LIME;
    }
}
