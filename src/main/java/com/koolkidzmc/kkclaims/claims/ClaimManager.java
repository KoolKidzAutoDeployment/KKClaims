package com.koolkidzmc.kkclaims.claims;

import com.koolkidzmc.kkclaims.KKClaims;
import org.bukkit.Chunk;
import org.bukkit.Particle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;


public class ClaimManager {
    private KKClaims plugin;
    private Logger console;
    public ClaimManager(KKClaims plugin, Logger console) {
        this.plugin = plugin;
        this.console = console;
    }
    /**
     * Creates a claim
     * @param chunk Chunk to claim
     * @param owner Player to own the claim
     */
    public void createClaim(Chunk chunk, UUID owner) {
        String claimID = getClaimID(chunk);
        JSONObject claimObject = new JSONObject();
        claimObject.put("owner", owner.toString());
        claimObject.put("profile", owner + ".global");
        JSONObject preObject = new JSONObject();
        try {
            JSONArray a = (JSONArray) new JSONParser().parse(new FileReader("./plugins/KKClaims/claims.json"));
            for (Object o : a) {
                preObject = (JSONObject) o;
            }
            preObject.put(claimID, claimObject);

            JSONArray allProfiles = new JSONArray();
            allProfiles.add(preObject);
        } catch (IOException | ParseException e) {
            console.warning("Error: " + e);
        }


        JSONArray allClaims = new JSONArray();
        allClaims.add(preObject);

        File claimFile = new File("./plugins/KKClaims/claims.json");
        try (FileWriter file = new FileWriter(claimFile)) {
            file.write(allClaims.toJSONString());
            file.flush();
        } catch (IOException e) {
            console.warning(e.toString());
        }
    }

    public boolean playerHasProfiles(UUID player) {
        try {
            String json = new JSONParser().parse(new FileReader("./plugins/KKClaims/profiles.json")).toString();
            if (json.contains(player.toString())) return true;
        } catch (ParseException | IOException e) {
            console.warning("Error while reading claim data: " + Arrays.toString(e.getStackTrace()));
        }
        return false;
    }
    public void createGlobalProfile(UUID player) {
        String profileKey = player + ".global";
        JSONObject profileObject = new JSONObject();
        profileObject.put("border", Particle.VILLAGER_HAPPY.toString());
        profileObject.put("pvp", false);
        JSONObject groupsObject = new JSONObject();
        JSONObject groupObject = prepareGroupObject("Default",
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);

        groupsObject.put("default", groupObject);
        groupObject = prepareGroupObject("Member",
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
        groupsObject.put("1", groupObject);
        groupObject = prepareGroupObject("Trusted Member",
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
        groupsObject.put("2", groupObject);
        groupObject = prepareGroupObject("Staff",
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
        groupsObject.put("3", groupObject);
        groupObject = prepareGroupObject("Admin",
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
        groupsObject.put("4", groupObject);
        groupObject = prepareGroupObject("Co Owner",
                false,
                false,
                true,
                true,
                true,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
        groupsObject.put("5", groupObject);

        JSONObject playersObject = new JSONObject();
        playersObject.put(player.toString(), "owner");

        profileObject.put("groups", groupsObject);
        profileObject.put("players", playersObject);
        JSONObject profileData = new JSONObject();
        profileData.put(profileKey, profileObject);


        JSONArray allProfiles = new JSONArray();
        allProfiles.add(profileData);

        File profileFile = new File("./plugins/KKClaims/profiles.json");
        try (FileWriter file = new FileWriter(profileFile)) {
            file.write(allProfiles.toJSONString());
            file.flush();
        } catch (IOException e) {
            console.warning("Error while reading claim data: " + Arrays.toString(e.getStackTrace()));
        }
    }


    /**
     * prepares a group object for profiles
     * @param groupName The name for the group
     * @param modifyBlocks Permission
     * @param doorsInteraction Permission
     * @param enterClaim Permission
     * @param fly Permission
     * @param dropItems Permission
     * @param pickupItems Permission
     * @param openContainers Permission
     * @param openEnderchests Permission
     * @param redstoneInteraction Permission
     * @param setHome Permission
     * @param killHostile Permission
     * @param killPassive Permission
     * @return JSONObject
     */
    public JSONObject prepareGroupObject(String groupName,
                                         Boolean modifyBlocks,
                                         Boolean doorsInteraction,
                                         Boolean enterClaim,
                                         Boolean fly,
                                         Boolean dropItems,
                                         Boolean pickupItems,
                                         Boolean openContainers,
                                         Boolean openEnderchests,
                                         Boolean redstoneInteraction,
                                         Boolean setHome,
                                         Boolean killHostile,
                                         Boolean killPassive) {
        JSONObject groupObject = new JSONObject();
        groupObject.put("groupName", groupName);
        groupObject.put("modifyBlocks", modifyBlocks);
        groupObject.put("doorsInteraction", doorsInteraction);
        groupObject.put("enterClaim", enterClaim);
        groupObject.put("fly", fly);
        groupObject.put("dropItems", dropItems);
        groupObject.put("pickupItems", pickupItems);
        groupObject.put("openContainers", openContainers);
        groupObject.put("openEnderchests", openEnderchests);
        groupObject.put("redstoneInteraction", redstoneInteraction);
        groupObject.put("setHome", setHome);
        groupObject.put("killHostile", killHostile);
        groupObject.put("killPassive", killPassive);

        return groupObject;
    }

    /**
     * Shortcut for creating a claim ID
     * @return String (ClaimID)
     */
    public String getClaimID(Chunk chunk) { return chunk.getX() + "." + chunk.getZ();}


    /**
     * Gets the claim object from claims.json
     * <p>
     * If it does not exist it returns null
     * @return JSONArray or null
     */
    public JSONObject getClaim(Chunk chunk) {
        String claimID = getClaimID(chunk);
        char[] array = new char[100];
        try {
            FileReader file = new FileReader("./plugins/KKClaims/claims.json");
            file.read(array);
            if (array[0] == '\0') return null;
            JSONArray a = (JSONArray) new JSONParser().parse(new FileReader("./plugins/KKClaims/claims.json"));
            JSONObject claimObject = new JSONObject();
            for (Object o : a) {
                JSONObject claims = (JSONObject) o;
                claimObject = (JSONObject) claims.get(claimID);
            }
            return claimObject;
        } catch (ParseException | IOException e) {
            console.warning("Error while reading claim data: " + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }

    /**
     * Gets the profile object from profiles.json
     * <p>
     * If it does not exist it returns null
     * @return JSONArray or null
     */
    public JSONObject getClaimProfile(Chunk chunk) {
        JSONObject claim = getClaim(chunk);
        if (claim == null) return null;
        String profile = claim.get("profile").toString();
        char[] array = new char[100];
        try {
            FileReader file = new FileReader("./plugins/KKClaims/profiles.json");
            file.read(array);
            if (array[0] == '\0') return null;
            JSONArray a = (JSONArray) new JSONParser().parse(new FileReader("./plugins/KKClaims/profiles.json"));
            JSONObject profileObject = new JSONObject();
            for (Object o : a) {
                JSONObject profiles = (JSONObject) o;
                profileObject = (JSONObject) profiles.get(profile);
            }
            return profileObject;
        } catch (ParseException | IOException e) {
            console.warning("Error while reading claim data: " + Arrays.toString(e.getStackTrace()));
        }
        return null;
    }


    /**
     * Checks if the given chunk is claimed
     * @return boolean
     */
    public boolean isClaimed(Chunk chunk) {
        return getClaim(chunk) != null;
    }

    public boolean checkClaimEdge(Chunk chunk, String dir) {
        if (dir.equalsIgnoreCase("west")) {
            Chunk chunkToWest = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ());
            return isClaimed(chunkToWest);
        }
        if (dir.equalsIgnoreCase("east")) {
            Chunk chunkToWest = chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ());
            return isClaimed(chunkToWest);
        }
        if (dir.equalsIgnoreCase("south")) {
            Chunk chunkToWest = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1);
            return isClaimed(chunkToWest);
        }
        if (dir.equalsIgnoreCase("north")) {
            Chunk chunkToWest = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1);
            return isClaimed(chunkToWest);
        }
        return false;
    }


    public UUID getClaimOwner(Chunk chunk) {
        JSONObject claim = getClaim(chunk);
        return UUID.fromString(claim.get("owner").toString());
    }


    public Particle getClaimBorder(Chunk chunk) {
        JSONObject profile = getClaimProfile(chunk);
        if (profile == null) return Particle.VILLAGER_HAPPY;
        return Particle.valueOf(profile.get("border").toString());
    }

    public String getChunkCoord(Chunk chunk) {
        String x = String.valueOf(chunk.getX());
        String z = String.valueOf(chunk.getZ());

        return "("+x+","+z+")";
    }

    public void setClaimBorder(Chunk chunk, Particle particle) {
        JSONObject claim = getClaim(chunk);
        if (claim == null) return;
        String claimID = getClaimID(chunk);
        String profileID = claim.get("profile").toString();
        JSONObject profile = getClaimProfile(chunk);
        profile.put("border", particle.toString());
        try {
            JSONArray a = (JSONArray) new JSONParser().parse(new FileReader("./plugins/KKClaims/profiles.json"));
            JSONObject profileObject = new JSONObject();
            for (Object o : a) {
                profileObject = (JSONObject) o;
            }
            profileObject.put(profileID, profile);

            JSONArray allProfiles = new JSONArray();
            allProfiles.add(profileObject);

            FileWriter file = new FileWriter("./plugins/KKClaims/profiles.json");
            file.write(allProfiles.toJSONString());
            file.flush();
        } catch (ParseException | IOException e) {
            console.warning("Error while reading claim data: " + Arrays.toString(e.getStackTrace()));
        }

    }
}
