package com.koolkidzmc.kkclaims.gui;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.ItemBuilder;
import com.koolkidzmc.kkclaims.utils.SoundAPI;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticlesGUI extends FastInv {
    private boolean preventClose = false;
    ClaimManager claims;
    KKClaims plugin;
    public ParticlesGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&a&lClaims &7>> &8Profile Border"));
        this.plugin = plugin;
        this.claims = claims;


        for (int i = 0; i < 9; i++) {
            setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
            setItem(i + 36, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
        }
        for (int i = 1; i < 5; i++) {
            setItem(i * 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
            setItem(i * 9 + 8, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
        }
        Map<Integer, Integer> slotMap = new HashMap<>();
        for (int i = 0; i < 21; i++) {
            slotMap.put(i, i + 10);
        }


        for (String key : plugin.getConfig().getConfigurationSection("borders").getKeys(false)) {
            Particle particle = Particle.valueOf(key);
            String name = plugin.getConfig().getString("borders." + key + ".name");
            Material material = Material.valueOf(plugin.getConfig().getString("borders." + key + ".item"));
            List<String> lore = plugin.getConfig().getStringList("borders." + key + ".description");
            int slot = plugin.getConfig().getInt("borders." + key + ".slot");
            ItemStack item = new ItemStack(material, 1);
            if (material == Material.FIREWORK_STAR) {
                ItemMeta im = item.getItemMeta();
                FireworkEffectMeta metaFw = (FireworkEffectMeta) im;
                String confColor = plugin.getConfig().getString("borders." + key + ".color");
                    FireworkEffect aa = FireworkEffect.builder()
                            .withColor(getConfColor(confColor)).build();
                metaFw.setEffect(aa);
                item.setItemMeta(metaFw);
                item.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            }
            if (claims.getClaimBorder(player.getChunk()) == particle)
                name = plugin.getConfig().getString("border-selected") + name;
            item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            setItem(slotMap.get(slot), new ItemBuilder(item).name(ColorAPI.formatString(name)).lore(ColorAPI.formatStringList(lore)).build(),
                    e -> {
                        claims.setClaimBorder(player.getChunk(), particle);
                        SoundAPI.success(player);
                        new ParticlesGUI(plugin, claims, player).open(player);
                    });
        }
        setItem(45, new ItemBuilder(Material.ARROW).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .name(ColorAPI.formatString("&c\u02C2\u02C2 Go Back"))
                        .addLore(ColorAPI.formatString("&7\u279C Click to go back"))
                        .build(),
                e -> {
                    SoundAPI.click((Player) e.getWhoClicked());
                    new ProfilesGUI(plugin, claims, player).open((Player) e.getWhoClicked());
                });
        setItem(49, new ItemBuilder(Material.BARRIER).flags(ItemFlag.HIDE_ATTRIBUTES)
                        .name(ColorAPI.formatString("&c&lClose"))
                        .addLore(ColorAPI.formatString("&7\u279C Click to close"))
                        .build(),
                e -> {
                    SoundAPI.fail((Player) e.getWhoClicked());
                    e.getClickedInventory().close();
                });
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
