/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.user;


import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.customspawner.EnumSpawnerType;
import de.teamhardcore.pvp.model.extras.EnumChatColor;
import de.teamhardcore.pvp.model.extras.EnumCommand;
import de.teamhardcore.pvp.model.extras.EnumKilleffect;
import de.teamhardcore.pvp.model.extras.EnumPerk;
import de.teamhardcore.pvp.model.gambling.crates.base.BaseCrate;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.entity.EntityType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class UserData {

    private final User user;

    private Set<EntityType> spawnerTypes;
    private Set<EnumPerk> unlockedPerks;
    private Set<EnumPerk> activatedPerks;
    private Set<EnumCommand> unlockedCommands;
    private Set<EnumChatColor> unlockedChatColors;
    private Set<EnumKilleffect> unlockedKilleffects;
    private Set<String> claimedUniqueKits;

    private List<UUID> friends;
    private List<UUID> friendRequests;
    private List<BaseCrate> ownedCrates;

    private boolean dailyReward;
    private boolean weeklyReward;
    private boolean monthlyReward;
    private boolean voteReward;

    private long dailyRewardTime;
    private long weeklyRewardTime;
    private long monthlyRewardTime;


    private Map<String, Long> kitCooldowns;

    private long mutePoints, banPoints;

    private EnumChatColor activeColor;
    private EnumKilleffect activeKilleffect;

    public UserData(User user) {
        this(user, true);
    }

    public UserData(User user, boolean async) {
        this.user = user;

        this.friends = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
        this.spawnerTypes = new HashSet<>();
        this.unlockedPerks = new HashSet<>();
        this.activatedPerks = new HashSet<>();
        this.unlockedCommands = new HashSet<>();
        this.unlockedKilleffects = new HashSet<>();
        this.unlockedChatColors = new HashSet<>();
        this.claimedUniqueKits = new HashSet<>();

        this.ownedCrates = new ArrayList<>();
        this.kitCooldowns = new HashMap<>();

        this.activeColor = null;

        this.mutePoints = 0;
        this.banPoints = 0;

        saveDefaults(async);
        loadData(async);
    }

    public void addFriend(UUID uuid) {
        if (this.user.getUuid().equals(uuid)) return;
        if (this.friends.contains(uuid)) return;
        this.friends.add(uuid);
        saveData(this.user.getPlayer() != null);
    }

    public void removeFriend(UUID uuid) {
        if (this.user.getUuid().equals(uuid)) return;
        if (!this.friends.contains(uuid)) return;
        this.friends.remove(uuid);
        saveData(this.user.getPlayer() != null);
    }

    private void loadFriendData(String json) {
        JSONArray mainArray = new JSONArray(json);
        for (Object friend : mainArray) {
            UUID uuid = UUID.fromString((String) friend);
            this.friends.add(uuid);
        }
    }

    private JSONArray saveFriendData() {
        JSONArray array = new JSONArray();
        for (UUID friend : this.friends)
            array.put(friend.toString());
        return array;
    }

    public List<UUID> getFriends() {
        return friends;
    }

    public void addFriendRequest(UUID uuid) {
        //    if (this.user.getUuid().equals(uuid)) return;
        if (this.friends.contains(uuid)) return;
        if (this.friendRequests.contains(uuid)) return;
        this.friendRequests.add(uuid);
    }

    public void removeFriendRequest(UUID uuid) {
        if (this.user.getUuid().equals(uuid)) return;
        if (!this.friendRequests.contains(uuid)) return;
        this.friendRequests.remove(uuid);
    }

    public List<UUID> getFriendRequests() {
        return friendRequests;
    }

    public EnumKilleffect getActiveKilleffect() {
        return activeKilleffect;
    }

    public Set<EnumKilleffect> getUnlockedKilleffects() {
        return unlockedKilleffects;
    }

    public void setActiveKilleffect(EnumKilleffect effect) {
        if (effect != null && !this.unlockedKilleffects.contains(effect)) return;
        if (this.activeKilleffect == effect) return;
        this.activeKilleffect = effect;
        saveData(this.user.getPlayer() != null);
    }

    public void addKillEffect(EnumKilleffect effect) {
        if (this.unlockedKilleffects.contains(effect))
            return;
        this.unlockedKilleffects.add(effect);
        saveData((this.user.getPlayer() != null));
    }

    public void removeKillEffect(EnumKilleffect effect) {
        if (!this.unlockedKilleffects.contains(effect))
            return;
        this.unlockedKilleffects.remove(effect);
        saveData((this.user.getPlayer() != null));
    }

    private void loadEffectData(String json) {
        JSONObject mainObject = new JSONObject(json);

        if (mainObject.has("killeffects")) {
            JSONObject killObject = mainObject.getJSONObject("killeffects");
            JSONArray ownedArray = killObject.getJSONArray("owned");
            for (Object objOwned : ownedArray) {
                EnumKilleffect effect = EnumKilleffect.getByName((String) objOwned);
                if (effect != null)
                    this.unlockedKilleffects.add(effect);
            }
            if (killObject.has("active")) {
                EnumKilleffect effect = EnumKilleffect.getByName(killObject.getString("active"));
                if (effect != null)
                    this.activeKilleffect = effect;
            }
        }
    }

    private JSONObject saveEffectData() {
        JSONObject mainObject = new JSONObject();
        if (!this.unlockedKilleffects.isEmpty()) {
            JSONObject killObject = new JSONObject();
            JSONArray ownedArray = new JSONArray();
            for (EnumKilleffect effect : this.unlockedKilleffects)
                ownedArray.put(effect.name());
            killObject.put("owned", ownedArray);
            if (this.activeKilleffect != null)
                killObject.put("active", this.activeKilleffect.name());
            mainObject.put("killeffects", killObject);
        }
        return mainObject;
    }

    private void loadSpawnerTypes(String json) {
        JSONArray array = new JSONArray(json);
        for (Object type : array) {
            EntityType entityType = EntityType.valueOf((String) type);
            this.spawnerTypes.add(entityType);
        }
    }

    private JSONArray saveSpawnerTypes() {
        JSONArray array = new JSONArray();
        for (EntityType type : this.spawnerTypes) {
            array.put(type.name());
        }
        return array;
    }

    public void addSpawnerType(EnumSpawnerType type) {
        if (this.spawnerTypes.contains(type.getType())) return;

        this.spawnerTypes.add(type.getType());
        saveData(this.user.getPlayer() != null);
    }

    public void removeSpawnerType(EnumSpawnerType type) {
        if (!this.spawnerTypes.contains(type.getType())) return;
        this.spawnerTypes.remove(type.getType());
        saveData(this.user.getPlayer() != null);
    }

    public Set<EntityType> getSpawnerTypes() {
        return spawnerTypes;
    }

    public void addActivatedPerk(EnumPerk perk) {
        if (this.activatedPerks.contains(perk)) return;

        this.activatedPerks.add(perk);

        if (this.user.getPlayer() != null) {
            if (perk == EnumPerk.NO_HUNGER)
                this.user.getPlayer().setFoodLevel(30);

            if (perk.getType() != null)
                Main.getInstance().getManager().addPerkEffect(this.user.getPlayer(), perk);
        }

        saveData(this.user.getPlayer() != null);
    }

    public void removeActivatedPerk(EnumPerk perk) {
        if (!this.activatedPerks.contains(perk)) return;
        this.activatedPerks.remove(perk);

        if (this.user.getPlayer() != null) {
            if (perk.getType() != null) {
                if (this.user.getPlayer().hasPotionEffect(perk.getType()))
                    this.user.getPlayer().removePotionEffect(perk.getType());
                Main.getInstance().getManager().removePerkEffect(this.user.getPlayer(), perk);
            }
        }
        saveData(this.user.getPlayer() != null);
    }

    public Set<EnumPerk> getActivatedPerks() {
        return activatedPerks;
    }

    public void addPerk(EnumPerk perk) {
        if (this.unlockedPerks.contains(perk)) return;
        this.unlockedPerks.add(perk);
        saveData(this.user.getPlayer() != null);
    }

    public void removePerk(EnumPerk perk) {
        if (!this.unlockedPerks.contains(perk)) return;
        this.unlockedPerks.remove(perk);
        saveData(this.user.getPlayer() != null);
    }

    public Set<EnumPerk> getUnlockedPerks() {
        return unlockedPerks;
    }

    private JSONObject savePerkData() {
        JSONObject object = new JSONObject();

        if (!this.unlockedPerks.isEmpty()) {
            JSONArray unlockedArray = new JSONArray();
            for (EnumPerk unlocked : this.unlockedPerks)
                unlockedArray.put(unlocked.name());
            object.put("unlocked", unlockedArray);
        }

        if (!this.activatedPerks.isEmpty()) {
            JSONArray activatedArray = new JSONArray();
            for (EnumPerk unlocked : this.activatedPerks)
                activatedArray.put(unlocked.name());
            object.put("activated", activatedArray);
        }

        return object;
    }

    private void loadPerkData(String json) {
        JSONObject object = new JSONObject(json);

        if (object.has("unlocked")) {
            JSONArray array = object.getJSONArray("unlocked");
            for (Object unlockedObj : array) {
                EnumPerk perk = EnumPerk.getPerkByName((String) unlockedObj);
                if (perk == null) continue;
                this.unlockedPerks.add(perk);
            }
        }

        if (object.has("activated")) {
            JSONArray array = object.getJSONArray("activated");
            for (Object activatedObj : array) {
                EnumPerk perk = EnumPerk.getPerkByName((String) activatedObj);
                if (perk == null) continue;
                this.activatedPerks.add(perk);
            }
        }
    }

    public void addExtraCommand(EnumCommand command) {
        if (this.unlockedCommands.contains(command)) return;
        this.unlockedCommands.add(command);
        saveData(this.user.getPlayer() != null);
    }

    private void loadCommandData(String json) {
        JSONObject object = new JSONObject(json);

        if (object.has("commands")) {
            JSONArray array = object.getJSONArray("commands");

            for (Object commandObj : array) {
                EnumCommand command = EnumCommand.getCommandByName((String) commandObj);

                if (command == null) continue;
                this.unlockedCommands.add(command);
            }
        }
    }

    private JSONObject saveCommandData() {
        JSONObject object = new JSONObject();

        if (!this.unlockedCommands.isEmpty()) {
            JSONArray array = new JSONArray();
            for (EnumCommand command : this.unlockedCommands) {
                array.put(command.name());
            }
            object.put("commands", array);
        }
        return object;
    }

    public Set<EnumCommand> getUnlockedCommands() {
        return unlockedCommands;
    }

    public void addChatColor(EnumChatColor chatColor) {
        if (this.unlockedChatColors.contains(chatColor)) return;
        this.unlockedChatColors.add(chatColor);
        saveData(this.user.getPlayer() != null);
    }

    public void removeChatColor(EnumChatColor chatColor) {
        if (!this.unlockedChatColors.contains(chatColor)) return;

        this.unlockedChatColors.remove(chatColor);
        if (this.activeColor.equals(chatColor))
            this.activeColor = null;

        saveData(this.user.getPlayer() != null);
    }

    public void setActiveColor(EnumChatColor chatColor) {
        if (chatColor != null && !this.unlockedChatColors.contains(chatColor)) return;
        if (this.activeColor == chatColor) return;
        this.activeColor = chatColor;
        saveData(this.user.getPlayer() != null);
    }

    private JSONObject saveChatColorData() {
        JSONObject object = new JSONObject();

        if (!this.unlockedChatColors.isEmpty()) {
            JSONArray array = new JSONArray();
            for (EnumChatColor color : this.unlockedChatColors)
                array.put(color.name());
            object.put("owned", array);

            if (this.activeColor != null) {
                object.put("active", this.activeColor.name());
            }
        }
        return object;
    }

    private void loadChatColorData(String json) {
        JSONObject object = new JSONObject(json);

        if (object.has("owned")) {
            JSONArray array = object.getJSONArray("owned");
            for (Object color : array) {
                EnumChatColor chatColor = EnumChatColor.getColorByName((String) color);
                this.unlockedChatColors.add(chatColor);
            }
        }

        if (object.has("active")) {
            this.activeColor = EnumChatColor.getColorByName(object.getString("active"));
        }
    }

    public Set<EnumChatColor> getUnlockedChatColors() {
        return unlockedChatColors;
    }

    public EnumChatColor getActiveColor() {
        return activeColor;
    }

    public long getBanPoints() {
        return banPoints;
    }

    public void setBanPoints(long banPoints) {
        this.banPoints = banPoints;
        saveData(this.user.getPlayer() != null);
    }

    public long getMutePoints() {
        return mutePoints;
    }

    public void setMutePoints(long mutePoints) {
        this.mutePoints = mutePoints;
        saveData(this.user.getPlayer() != null);
    }

    public Set<String> getClaimedUniqueKits() {
        return claimedUniqueKits;
    }

    public void addClaimedUniqueKit(String name) {
        if (this.claimedUniqueKits.contains(name))
            return;
        this.claimedUniqueKits.add(name);
        saveData(this.user.getPlayer() != null);
    }

    public void removeClaimedUniqueKit(String name) {
        if (!this.claimedUniqueKits.contains(name)) return;
        this.claimedUniqueKits.remove(name);
        saveData(this.user.getPlayer() != null);
    }

    public Map<String, Long> getKitCooldowns() {
        return kitCooldowns;
    }

    public void addKitCooldown(String name, long cooldown) {
        this.kitCooldowns.put(name, cooldown);
        saveData(this.user.getPlayer() != null);
    }

    public void removeKitCooldown(String name) {
        if (!this.kitCooldowns.containsKey(name)) return;
        this.kitCooldowns.remove(name);
        saveData(this.user.getPlayer() != null);
    }

    public boolean hasKitCooldown(String name) {
        if (!this.kitCooldowns.containsKey(name)) return false;
        return true;
    }

    public List<BaseCrate> getOwnedCrates() {
        return ownedCrates;
    }

    public void addCrate(BaseCrate crate) {
        this.ownedCrates.add(crate);
        saveData(this.user.getPlayer() != null);
    }

    public void removeCrate(int index) {
        if (index < 0 || index >= this.ownedCrates.size())
            return;
        this.ownedCrates.remove(index);
        saveData((this.user.getPlayer() != null));
    }

    public void setDailyReward(boolean dailyReward) {
        this.dailyReward = dailyReward;
        this.dailyRewardTime = System.currentTimeMillis();
        saveData(this.user.getPlayer() != null);
    }

    public boolean hasDailyReward() {
        return dailyReward;
    }

    public void setWeeklyReward(boolean weeklyReward) {
        this.weeklyReward = weeklyReward;
        this.weeklyRewardTime = System.currentTimeMillis();
        saveData(this.user.getPlayer() != null);
    }

    public boolean hasWeeklyReward() {
        return weeklyReward;
    }

    public void setMonthlyReward(boolean monthlyReward) {
        this.monthlyReward = monthlyReward;
        this.monthlyRewardTime = System.currentTimeMillis();
        saveData(this.user.getPlayer() != null);
    }

    public boolean hasMonthlyReward() {
        return monthlyReward;
    }

    public void setVoteReward(boolean voteReward) {
        this.voteReward = voteReward;
        saveData(this.user.getPlayer() != null);
    }

    public boolean hasVoteReward() {
        return voteReward;
    }

    public void checkRewardState() {
        /*if (this.voteReward && getRemainingVoteCount() == 2) {
            setVoteReward(false);
        }*/
        if (!this.dailyReward) {
            LocalDate dateNow = LocalDate.now();
            LocalDate dateLastReward = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.dailyRewardTime), TimeZone.getDefault().toZoneId()).toLocalDate();
            if (dateNow.isAfter(dateLastReward)) {
                setDailyReward(true);
                if (this.user.getPlayer() != null)
                    this.user.getPlayer().sendMessage(StringDefaults.REWARDS_PREFIX + "§6Deine §eTägliche Belohnung §6ist wieder verfügbar.");
            }
        }

        if (!this.weeklyReward) {
            LocalDate dateNow = LocalDate.now();
            LocalDate dateLastReward = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.weeklyRewardTime), TimeZone.getDefault().toZoneId()).toLocalDate();
            if (dateNow.isAfter(dateLastReward)) {
                setWeeklyReward(true);
                if (this.user.getPlayer() != null)
                    this.user.getPlayer().sendMessage(StringDefaults.REWARDS_PREFIX + "§6Deine §eWöchentliche Belohnung §6ist wieder verfügbar.");
            }
        }

        if (!this.monthlyReward) {
            LocalDate dateNow = LocalDate.now();
            LocalDate dateLastReward = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.monthlyRewardTime), TimeZone.getDefault().toZoneId()).toLocalDate();
            if (dateNow.isAfter(dateLastReward)) {
                setMonthlyReward(true);
                if (this.user.getPlayer() != null)
                    this.user.getPlayer().sendMessage(StringDefaults.REWARDS_PREFIX + "§6Deine §eMonatliche Belohnung §6ist wieder verfügbar.");
            }
        }
    }

    /*
    todo: save reward data
     */

    private JSONArray saveCrateData() {
        JSONArray array = new JSONArray();
        for (BaseCrate crate : this.ownedCrates)
            array.put(crate.getAddon().getName());
        return array;
    }

    private void loadCrateData(String jsonString) {
        JSONArray array = new JSONArray(jsonString);
        for (Object entry : array) {
            String crateName = (String) entry;
            BaseCrate crate = Main.getInstance().getCrateManager().getCrate(crateName);
            if (crate == null) continue;
            this.ownedCrates.add(crate);
        }
    }

    private void saveDefaults(boolean async) {

    }

    private void loadData(boolean async) {

    }

    private void saveData(boolean async) {

    }

    public User getUser() {
        return user;
    }
}
