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
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilesGUI extends FastInv {
    private boolean preventClose = false;
    private ClaimManager claims;
    private KKClaims plugin;

    private Player player;

    public ProfilesGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&a&lClaims &7>> &8Profiles"));
        this.plugin = plugin;
        this.claims = claims;
        this.player = player;

        fillBackground();
        populateProfileSlots();
        addNavigationButtons(player);
    }

    private void fillBackground() {
        for (int i = 0; i < 9; i++) {
            setItem(i, createBackgroundItem().build());
            setItem(i + 36, createBackgroundItem().build());
        }
        for (int i = 1; i < 5; i++) {
            setItem(i * 9, createBackgroundItem().build());
            setItem(i * 9 + 8, createBackgroundItem().build());
        }
    }

    private ItemBuilder createBackgroundItem() {
        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .lore(ColorAPI.formatString("&8www.koolkidzmc.com"));
    }

    private void populateProfileSlots() {
        Map<Integer, Integer> slotMap = new HashMap<>();
        for (int i = 0; i < 21; i++) {
            slotMap.put(i, i + 10);
        }

        int i = 10;
        for (Object o : claims.getPlayerProfiles(player)) {
            JSONObject profile = (JSONObject) o;
            setItem(slotMap.get(i), createProfileItem(profile).build(), e -> {
                SoundAPI.click((Player) e.getWhoClicked());
                new ParticlesGUI(plugin, claims, player).open((Player) e.getWhoClicked());
            });
            ++i;
        }
    }

    private ItemBuilder createProfileItem(JSONObject profile) {
        return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                .name(ColorAPI.formatString("&a&l" + profile.get("name")));
    }

    private void addNavigationButtons(Player player) {
        setItem(45, createNavigationItem(Material.ARROW, "&c\u02C2\u02C2 Go Back", "&7\u279C Click to go back"), e -> {
            SoundAPI.click(player);
            new ClaimGUI(plugin, claims, player).open(player);
        });

        setItem(49, createNavigationItem(Material.BARRIER, "&c&lClose", "&7\u279C Click to close"), e -> {
            SoundAPI.fail(player);
            e.getClickedInventory().close();
        });
    }

    private ItemStack createNavigationItem(Material material, String displayName, String lore) {
        return new ItemBuilder(material)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .name(ColorAPI.formatString(displayName))
                .addLore(ColorAPI.formatString(lore))
                .build();
    }
}

/*
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
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilesGUI extends FastInv {
    private boolean preventClose = false;
    ClaimManager claims;
    KKClaims plugin;

    public ProfilesGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&a&lClaims &7>> &8Profiles"));
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

        setItem(slotMap.get(10), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(ColorAPI.formatString("&c&lEmpty Profile")).build());
        setItem(slotMap.get(11), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(ColorAPI.formatString("&c&lEmpty Profile")).build());
        setItem(slotMap.get(12), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(ColorAPI.formatString("&c&lEmpty Profile")).build());
        setItem(slotMap.get(13), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(ColorAPI.formatString("&c&lEmpty Profile")).build());
        setItem(slotMap.get(14), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name(ColorAPI.formatString("&c&lEmpty Profile")).build());

        int i = 10;
        for (Object o : claims.getPlayerProfiles(player)) {
            JSONObject profile = (JSONObject) o;
            setItem(slotMap.get(i), new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name(ColorAPI.formatString("&a&l" + profile.get("name"))).build(),
                    e -> {
                        SoundAPI.click((Player) e.getWhoClicked());
                        new ParticlesGUI(plugin, claims, player).open((Player) e.getWhoClicked());
                    });
            ++i;
        }


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
*/