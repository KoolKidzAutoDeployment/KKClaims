package com.koolkidzmc.kkclaims.gui.claim;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.gui.profile.ParticlesGUI;
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

public class ClaimSettingsGUI extends FastInv {
    private boolean preventClose = false;
    private ClaimManager claims;
    private KKClaims plugin;

    private Player player;

    public ClaimSettingsGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&a&lClaims &7>> &8Settings"));
        this.plugin = plugin;
        this.claims = claims;
        this.player = player;

        fillBackground();
        populateSettingsSlots();
        addNavigationButtons(player);
    }

    private void fillBackground() {
        for (int i = 0; i < 9; i++) {
            setItem(i, createBackgroundItem().build());
            setItem(i + 45, createBackgroundItem().build());
        }
        for (int i = 1; i < 6; i++) {
            setItem(i * 9, createBackgroundItem().build());
            setItem(i * 9 + 8, createBackgroundItem().build());
        }
    }

    private ItemBuilder createBackgroundItem() {
        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
                .name(" ")
                .lore(ColorAPI.formatString("&8www.koolkidzmc.com"));
    }

    private void populateSettingsSlots() {
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