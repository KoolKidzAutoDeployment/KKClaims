package com.koolkidzmc.kkclaims.gui;

import com.koolkidzmc.kkclaims.KKClaims;
import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.utils.ColorAPI;
import com.koolkidzmc.kkclaims.utils.FastInv;
import com.koolkidzmc.kkclaims.utils.ItemBuilder;
import com.koolkidzmc.kkclaims.utils.SoundAPI;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.LinkedList;
import java.util.List;

public class ClaimGUI extends FastInv {
    private boolean preventClose = false;
    ClaimManager claims;
    KKClaims plugin;

    public ClaimGUI(KKClaims plugin, ClaimManager claims, Player player) {
        super(54, ColorAPI.formatString("&a&lClaims &7>> &8Claim Land"));
        this.plugin = plugin;
        this.claims = claims;
        int index = 0;
        for (Chunk chunk : getNearByChunksRelativeToPlayerAndMenu(player)) {
            if (index == 45) return;
            setItem(index, new ItemBuilder(getClaimedMat(chunk, player))
                    .name(ColorAPI.formatString(getClaimedName(chunk, player) + " &8" + claims.getChunkCoord(chunk)))
                    .addLore(ColorAPI.formatString(getClaimedStatus(chunk, player)))
                    .build(),
            e -> {
                if (claims.isClaimed(chunk)) {
                    SoundAPI.fail(player);
                } else {
                    claims.createClaim(chunk, player.getUniqueId());
                    new ClaimGUI(plugin, claims, player).open(player);
                    SoundAPI.success(player);
                }
            });
            ++index;
        }
        setItem(22, new ItemBuilder(Material.NETHER_STAR).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS).enchant(Enchantment.DAMAGE_ALL)
                .name(ColorAPI.formatString(getClaimedName(player.getChunk(), player) + " &8" + claims.getChunkCoord(player.getChunk())))
                .addLore(ColorAPI.formatString(getClaimedStatus(player.getChunk(), player)))
                .build(),
                e -> {
                    if (claims.isClaimed(player.getChunk())) {
                        SoundAPI.fail(player);
                    } else {
                        claims.createClaim(player.getChunk(), player.getUniqueId());
                        new ClaimGUI(plugin, claims, player).open(player);
                        SoundAPI.success(player);
                    }
                });

        setItem(49, new ItemBuilder(Material.BARRIER).flags(ItemFlag.HIDE_ATTRIBUTES)
                .name(ColorAPI.formatString("&c&lClose"))
                .addLore(ColorAPI.formatString("&7\u279C Click to close"))
                .build(),
                e -> {
                    SoundAPI.fail((Player) e.getWhoClicked());
                    e.getClickedInventory().close();
                });
        setItem(50, new ItemBuilder(Material.OAK_SIGN).flags(ItemFlag.HIDE_ATTRIBUTES)
                .name(ColorAPI.formatString("&9Your Claim Profiles"))
                .addLore(ColorAPI.formatString("&7\u279C Click to view"))
                .build(),
                e -> {
                    SoundAPI.click((Player) e.getWhoClicked());
                    new ProfilesGUI(plugin, claims, player).open((Player) e.getWhoClicked());
                });

    }
    private List<Chunk> getNearByChunksRelativeToPlayerAndMenu(Player player) {
        List<Chunk> chunkList = new LinkedList<>();
        BlockFace facing = player.getFacing();
        int playerChunkX = player.getChunk().getX();
        int playerChunkZ = player.getChunk().getZ();
        switch (facing) {
            case NORTH:
                for (int z = -2; z <= 2; z++) { // normal z-order for SOUTH
                    for (int x = -4; x <= 4; x++) {
                        int chunkX = playerChunkX + x, chunkZ = playerChunkZ + z;
                        chunkList.add(player.getWorld().getChunkAt(chunkX, chunkZ));
                    }
                }
                break;
            case EAST:
                for (int x = 2; x >= -2; x--) {
                    for (int z = -4; z <= 4; z++) {
                        int chunkX = playerChunkX + x, chunkZ = playerChunkZ + z;
                        chunkList.add(player.getWorld().getChunkAt(chunkX, chunkZ));
                    }
                }
                break;
            case SOUTH:
                for (int z = 2; z >= -2; z--){ // reverse z-order for NORTH
                    for (int x = 4; x >= -4; x --) {
                        int chunkX = playerChunkX + x, chunkZ = playerChunkZ + z;
                        chunkList.add(player.getWorld().getChunkAt(chunkX, chunkZ));
                    }
                }
                break;
            case WEST:
                for (int x = -2; x <= 2; x++) {
                    for (int z = 4; z >= -4; z--){
                        int chunkX = playerChunkX + x, chunkZ = playerChunkZ + z;
                        chunkList.add(player.getWorld().getChunkAt(chunkX, chunkZ));
                    }
                }
                break;
        }
        return chunkList;
    }

    public String getClaimedName(Chunk chunk, Player player) {
        if (claims.getClaim(chunk) == null) return "&7Available Claim";
        if (claims.getClaim(chunk).get("profile") == null) return "&7Available Claim";
        if (!claims.isClaimed(chunk)) return "&7Available Claim";
        if (claims.getClaimOwner(chunk).equals(player.getUniqueId())) return "&aYour Claim";
        return "&e" +  claims.getClaimOwner(chunk) + "'s Claim";
    }
    public String getClaimedStatus(Chunk chunk, Player player) {
        if (claims.getClaim(chunk) == null) return "&f&l| &7Status: &fAvailable";
        if (claims.getClaim(chunk).get("profile") == null) return "&f&l| &7Status: &fAvailable";
        if (!claims.isClaimed(chunk)) return "&f&l| &7Status: &fAvailable";
        if (claims.getClaimOwner(chunk).equals(player.getUniqueId())) return "&f&l| &7Status: &aClaimed";
        return "&f&l| &7Status: &aClaimed";
    }
    public ItemStack getClaimedMat(Chunk chunk, Player player) {
        if (claims.getClaim(chunk) == null) return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        if (claims.getClaim(chunk).get("profile") == null) return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        if (!claims.isClaimed(chunk)) return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        skullMeta.setOwningPlayer(plugin.getServer().getOfflinePlayer(player.getUniqueId()));
        playerHead.setItemMeta(skullMeta);
        if (claims.getClaimOwner(chunk).equals(player.getUniqueId())) return playerHead;
        skullMeta.setOwningPlayer(plugin.getServer().getOfflinePlayer(claims.getClaimOwner(chunk)));
        playerHead.setItemMeta(skullMeta);
        return playerHead;
    }

}