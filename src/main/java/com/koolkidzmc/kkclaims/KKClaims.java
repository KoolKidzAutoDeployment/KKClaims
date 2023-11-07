package com.koolkidzmc.kkclaims;

import com.koolkidzmc.kkclaims.claims.ClaimManager;
import com.koolkidzmc.kkclaims.commands.CommandClaim;
import com.koolkidzmc.kkclaims.commands.CommandParticle;
import com.koolkidzmc.kkclaims.commands.CommandUnClaim;
import com.koolkidzmc.kkclaims.listeners.JoinListener;
import com.koolkidzmc.kkclaims.listeners.PreventionListener;
import com.koolkidzmc.kkclaims.utils.FastInvManager;
import com.koolkidzmc.kkclaims.utils.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public final class KKClaims extends JavaPlugin {
    Logger console = getLogger();
    ClaimManager claims = new ClaimManager(this, console);
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        console.info("Starting Plugin");
        FastInvManager.register(this);
        console.info("Loading Files...");
        saveDefaultConfig();
        reloadConfig();
        console.info("config.yml loaded!");
        console.info("Pausing File Loader!");
        console.info("Checking Licence Key...");
        String key = config.get("licence-key").toString();
        if (key == null || key.isEmpty()) {
            console.warning("*** No Licence Key Found in config.yml! ***");
            console.warning("-*-* Plugin Will Now Disable! *-*-");
            this.getPluginLoader().disablePlugin(this);
        }
        checkLicenceKey(key);
        console.info("Resuming File Loader");
        try {
            File f = new File("./plugins/KKClaims/claims.json");
            if (!f.exists()) {
                FileWriter file = new FileWriter("./plugins/KKClaims/claims.json");
                file.append("[]");
                file.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        console.info("claims.json loaded!");
        try {
            File f = new File("./plugins/KKClaims/profiles.json");
            if (!f.exists()) {
                FileWriter file = new FileWriter("./plugins/KKClaims/profiles.json");
                file.append("[]");
                file.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        console.info("profiles.json loaded!");
        console.info("Loading Commands...");
        loadCommands();
        console.info("Commands Loaded!");
        console.info("Starting Listeners");
        getServer().getPluginManager().registerEvents(new PreventionListener(this, claims), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this, claims), this);
        console.info("Listeners Started!");
        console.info("Starting Asynchronous Tasks");
        TaskManager.Async.runTask(chunkBorders, 5);
        console.info("Asynchronous Tasks Started!");
    }
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getLogger().info("Plugin Disabled");
    }
    public void loadCommands() {
        this.getCommand("particle").setExecutor(new CommandParticle(this, claims));
        this.getCommand("claim").setExecutor(new CommandClaim(this, claims));
        this.getCommand("unclaim").setExecutor(new CommandUnClaim(this, claims));
    }
    Runnable chunkBorders = () -> {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Chunk cnk = player.getChunk();
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    Chunk neighbor = player.getWorld().getChunkAt(cnk.getX() + xOffset, cnk.getZ() + zOffset);
                    showBorder(neighbor, claims.getClaimBorder(neighbor), player);
                }
            }
            /*
            Chunk cnk = player.getChunk();
            Chunk cnk1 = player.getWorld().getChunkAt(cnk.getX() + 1, cnk.getZ());
            Chunk cnk2 = player.getWorld().getChunkAt(cnk.getX(), cnk.getZ() + 1);
            Chunk cnk3 = player.getWorld().getChunkAt(cnk.getX() - 1, cnk.getZ());
            Chunk cnk4 = player.getWorld().getChunkAt(cnk.getX(), cnk.getZ() - 1);
            Chunk cnk5 = player.getWorld().getChunkAt(cnk.getX() + 1, cnk.getZ() + 1);
            Chunk cnk6 = player.getWorld().getChunkAt(cnk.getX() + 1, cnk.getZ() - 1);
            Chunk cnk7 = player.getWorld().getChunkAt(cnk.getX() - 1, cnk.getZ() + 1);
            Chunk cnk8 = player.getWorld().getChunkAt(cnk.getX() - 1, cnk.getZ() - 1);
            showBorder(cnk, claims.getClaimBorder(cnk), player);
            showBorder(cnk1, claims.getClaimBorder(cnk1), player);
            showBorder(cnk2, claims.getClaimBorder(cnk2), player);
            showBorder(cnk3, claims.getClaimBorder(cnk3), player);
            showBorder(cnk4, claims.getClaimBorder(cnk4), player);
            showBorder(cnk5, claims.getClaimBorder(cnk5), player);
            showBorder(cnk6, claims.getClaimBorder(cnk6), player);
            showBorder(cnk7, claims.getClaimBorder(cnk7), player);
            showBorder(cnk8, claims.getClaimBorder(cnk8), player);

             */
        }
    };

    public void checkLicenceKey(String key) {
        try {
            URL url = new URL("https://api.github.com/repos/KoolKidzAutoDeployment/KKClaims-Licence-Keys/contents/keys.txt");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer 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");
            con.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
            con.setRequestProperty("Accept", "application/vnd.github.v3.raw");
            con.connect();
            String keys = con.getContent().toString();
            if (keys.contains(key)) {
                console.info("Licence Key Accepted!");
            } else {
                console.warning("-*#-*#-*# Licence Key Not Valid! #*-#*-#*-");
                console.warning("*** Plugin Will Now Disable! ***");
                this.getPluginLoader().disablePlugin(this);
            }
        } catch (IOException e) {
            console.info("Error: " + e);
            console.warning("-*#-*#-*# Licence Key Failed To Load! #*-#*-#*-");
            console.warning("*** Plugin Will Now Disable! ***");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void showBorder(Chunk chunk, Particle particle, Player player) {
        if (!claims.isClaimed(chunk)) return;
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int minY = (int) player.getLocation().getY() - 1;

        if (!claims.checkClaimEdge(chunk, "north")) {
            for (int x = minX; x < minX + 17; x++) {
                for (int y = minY; y < minY + 5; y++) {
                    player.spawnParticle(particle, x, y, minZ, 1, 0, 0, 0, 0);
                }
            }
        }

        if (!claims.checkClaimEdge(chunk, "south")) {
            for (int x = minX; x < minX + 17; x++) {
                for (int y = minY; y < minY + 5; y++) {
                    player.spawnParticle(particle, x, y, minZ + 16, 1, 0, 0, 0, 0);
                }
            }
        }

        if (!claims.checkClaimEdge(chunk, "west")) {
            for (int z = minZ; z < minZ + 17; z++) {
                for (int y = minY; y < minY + 5; y++) {
                    player.spawnParticle(particle, minX, y, z, 1, 0, 0, 0, 0);
                }
            }
        }

        if (!claims.checkClaimEdge(chunk, "east")) {
            for (int z = minZ; z < minZ + 17; z++) {
                for (int y = minY; y < minY + 5; y++) {
                    player.spawnParticle(particle, minX + 16, y, z, 1, 0, 0, 0, 0);
                }
            }
        }
    }
}

// for (int y = minY; y < minY + 5; y++) {