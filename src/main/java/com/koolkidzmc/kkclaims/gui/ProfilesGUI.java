package com.koolkidzmc.kkclaims.gui;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.ItemBuilder;
import com.koolkidzmc.kkclaims.utils.SoundAPI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;
import java.util.Map;

public class ProfilesGUI extends FastInv {
    private boolean preventClose = false;
    ClaimManager claims;
    KKClaims plugin;

    public ProfilesGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&a&lClaims &7>> &fProfiles"));
        this.plugin = plugin;
        this.claims = claims;

        for (int i = 0; i < 9; i++) {
            setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
            setItem(i + 45, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
        }
        for (int i = 1; i < 5; i++) {
            setItem(i * 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
            setItem(i * 9 + 8, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").lore(ColorAPI.formatString("&8www.koolkidzmc.com")).build());
        }

        Map<Integer, Integer> slotMap = new HashMap<>();
        for (int i = 0; i < 28; i++) {
            slotMap.put(i, i + 10);
        }

        //TODO: for loop to iterate through profiles and show all of the players profiles

        setItem(45, new ItemBuilder(Material.ARROW).flags(ItemFlag.HIDE_ATTRIBUTES)
                .name(ColorAPI.formatString("&c\u02C2\u02C2 Go Back"))
                .addLore(ColorAPI.formatString("&7\u279C Click to go back"))
                .build(),
                e -> {
                    SoundAPI.click((Player) e.getWhoClicked());
                    new ClaimGUI(plugin, claims, player).open((Player) e.getWhoClicked());
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
}
