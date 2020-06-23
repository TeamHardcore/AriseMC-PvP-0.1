/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.duel.Duel;
import de.teamhardcore.pvp.duel.event.DuelWinEvent;
import de.teamhardcore.pvp.duel.phases.DuelPhase;
import de.teamhardcore.pvp.duel.request.DuelRequest;
import de.teamhardcore.pvp.duel.map.DuelMap;
import de.teamhardcore.pvp.model.achievements.AchievementGroups;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class DuelManager {

    private final Main plugin;

    private final Map<UUID, Duel> duelCache;
    private final Set<String> gameIdCache;
    private final Set<UUID> playerQuits;

    private final Map<String, List<DuelMap>> duelMaps;
    private final Map<String, List<DuelMap>> availableMaps;

    public DuelManager(Main plugin) {
        this.plugin = plugin;
        this.duelCache = new HashMap<>();
        this.gameIdCache = new HashSet<>();
        this.playerQuits = new HashSet<>();
        this.duelMaps = new HashMap<>();
        this.availableMaps = new HashMap<>();

        this.plugin.getServer().getPluginManager().registerEvents(new DuelEvents(this), this.plugin);


        DuelMap map = new DuelMap("Test1", "Prison");
        map.addLocation(new Location(Bukkit.getWorld("world"), -263, 68, 308));
        map.addLocation(new Location(Bukkit.getWorld("world"), -263, 68, 300));

        DuelMap map2 = new DuelMap("Test2", "Prison");
        map2.addLocation(new Location(Bukkit.getWorld("world"), -263, 68, 308));
        map2.addLocation(new Location(Bukkit.getWorld("world"), -263, 68, 300));

        ArrayList<DuelMap> maps = new ArrayList<>();
        maps.add(map);
        maps.add(map2);
        this.duelMaps.put("Prison", maps);
        saveMaps();
        loadMaps();
    }

    private void loadMaps() {
        this.duelMaps.clear();

        FileConfiguration cfg = this.plugin.getFileManager().getDuelFile().getConfig();

        if (cfg.get("Maps") == null)
            return;

        JSONObject mainObject = new JSONObject(cfg.getString("Maps"));

        for (String category : mainObject.keySet()) {
            JSONArray categoryArray = mainObject.getJSONArray(category);

            for (Object object : categoryArray) {
                JSONObject map = (JSONObject) object;
                String name = map.getString("name");

                DuelMap duelMap = new DuelMap(name, category);
                JSONArray array = map.getJSONArray("locations");
                array.forEach(o -> duelMap.getLocations().add(Util.stringToLocation((String) o)));

                System.out.println("Category: " + category + " / Name: " + name);
                List<DuelMap> maps = this.duelMaps.containsKey(category) ? this.duelMaps.get(category) : new ArrayList<>();
                maps.add(duelMap);
                this.duelMaps.put(category, maps);
                this.availableMaps.put(category, maps);
            }
        }
    }

    public void saveMaps() {
        if (this.duelMaps.isEmpty()) return;
        FileConfiguration cfg = this.plugin.getFileManager().getDuelFile().getConfig();

        JSONObject mainObject = new JSONObject();

        for (Map.Entry<String, List<DuelMap>> entry : this.duelMaps.entrySet()) {
            JSONArray categoryArray = new JSONArray();
            for (DuelMap map : entry.getValue()) {
                JSONObject object = new JSONObject();
                object.put("name", map.getName());
                object.put("category", map.getCategory());

                JSONArray locationArray = new JSONArray();
                for (Location location : map.getLocations())
                    locationArray.put(Util.locationToString(location));

                object.put("locations", locationArray);
                categoryArray.put(object);
            }
            mainObject.put(entry.getKey(), categoryArray);
        }

        cfg.set("Maps", mainObject.length() == 0 ? null : mainObject.toString());
        this.plugin.getFileManager().getDuelFile().saveConfig();
    }

    public void createDuel(DuelRequest request, Player player, Player target) {
        if (request.getPlayers().size() <= 1) return;

        String gameID = null;

        while (gameID == null || this.gameIdCache.contains(gameID))
            gameID = "duel-" + UUID.randomUUID().toString().substring(0, 8);

        Duel duel = new Duel(request, gameID);

        this.availableMaps.get(duel.getMap().getCategory()).remove(duel.getMap());
        this.duelCache.put(player.getUniqueId(), duel);
        this.duelCache.put(target.getUniqueId(), duel);
        this.gameIdCache.add(gameID);
    }

    public void stopDuel(Duel duel) {
        for (Player player : duel.getPlayers()) {
            this.duelCache.remove(player.getUniqueId());
        }
        this.gameIdCache.remove(duel.getGameID());
        this.availableMaps.get(duel.getMap().getCategory()).add(duel.getMap());
    }

    public Duel getDuel(String gameID) {
        return this.duelCache.values().stream().filter(duel -> duel.getGameID().equals(gameID)).findFirst().orElse(null);
    }

    public DuelMap getAvailableMap(String category) {
        if (!this.availableMaps.containsKey(category))
            return null;

        List<DuelMap> available = new ArrayList<>(this.availableMaps.get(category));
        if (available.isEmpty())
            return null;

        return available.get(0);
    }

    public List<DuelMap> getMaps(String category) {
        if (!this.duelMaps.containsKey(category))
            return null;
        return this.duelMaps.get(category);
    }

    public Map<String, List<DuelMap>> getAvailableMaps() {
        return availableMaps;
    }

    public Map<UUID, Duel> getDuelCache() {
        return duelCache;
    }

    public Set<UUID> getPlayerQuits() {
        return playerQuits;
    }

    public Main getPlugin() {
        return plugin;
    }

    public static class DuelEvents implements Listener {

        private final DuelManager manager;

        public DuelEvents(DuelManager manager) {
            this.manager = manager;
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();

            if (this.manager.getDuelCache().containsKey(player.getUniqueId())) {
                Duel duel = this.manager.getDuelCache().get(player.getUniqueId());
                duel.performDeath(player);
                this.manager.getPlayerQuits().add(player.getUniqueId());
            }
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            if (this.manager.getPlayerQuits().contains(player.getUniqueId())) {
                player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast dich während eines Duells ausgeloggt.");
            }

        }

        @EventHandler
        public void onEntityDamage(EntityDamageByEntityEvent event) {
            if (!(event.getEntity() instanceof Player)) return;

            Player player = (Player) event.getEntity();
            Player damager = null;

            if (event.getDamager() instanceof Player) {
                damager = (Player) event.getDamager();
            } else if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    damager = (Player) ((Projectile) event.getDamager()).getShooter();
                }
            }

            if (damager == null) return;
            if (damager == player) return;

            if (this.manager.getDuelCache().containsKey(player.getUniqueId()) && this.manager.getDuelCache().containsKey(damager.getUniqueId())) {
                Duel playerDuel = this.manager.getDuelCache().get(player.getUniqueId());
                Duel damagerDuel = this.manager.getDuelCache().get(damager.getUniqueId());

                if (playerDuel.getGameID().equals(damagerDuel.getGameID()))
                    if (playerDuel.getPhase().getType() == DuelPhase.START)
                        event.setCancelled(true);

            }
        }

        @EventHandler
        public void onDeath(PlayerDeathEvent event) {
            Player player = event.getEntity();
            Player killer = player.getKiller();

            if (killer != null && killer != player && killer.isOnline()) {
                if (this.manager.getDuelCache().containsKey(player.getUniqueId()) && this.manager.getDuelCache().containsKey(killer.getUniqueId())) {
                    Duel duel = this.manager.getDuelCache().get(player.getUniqueId());
                    duel.performDeath(player);
                    event.getDrops().clear();
                }
            }
        }

        @EventHandler
        public void onGamemodeChange(PlayerGameModeChangeEvent event) {
            Player player = event.getPlayer();
            if (this.manager.getDuelCache().containsKey(player.getUniqueId()) && !player.hasPermission("arisemc.duel.admin"))
                event.setCancelled(true);
        }

        @EventHandler
        public void onTeleport(PlayerTeleportEvent event) {
            Player player = event.getPlayer();

            if (this.manager.getDuelCache().containsKey(player.getUniqueId()) && !player.hasPermission("arisemc.combat.admin"))
                event.setCancelled(true);
        }

        @EventHandler
        public void onToggleFlight(PlayerToggleFlightEvent event) {
            Player player = event.getPlayer();
            if (this.manager.getDuelCache().containsKey(player.getUniqueId()) && player.getGameMode() != GameMode.CREATIVE && !player.hasPermission("arisemc.combat.admin"))
                event.setCancelled(true);
        }

        @EventHandler
        public void onPotionSplash(PotionSplashEvent event) {
            ThrownPotion potion = event.getPotion();

            Player player = (Player) potion.getShooter();

            for (PotionEffect effect : potion.getEffects()) {
                if (effect.getType().equals(PotionEffectType.HEAL) || effect.getType().equals(PotionEffectType.HEALTH_BOOST)) {
                    if (this.manager.getDuelCache().containsKey(player.getUniqueId())) {
                        Duel duel = this.manager.getDuelCache().get(player.getUniqueId());

                        int remainingPotions = duel.getRemainingPotions(player);

                        if (duel.hasRemainingPotions(player))
                            duel.setRemainingPotions(player, 1);

                        if (remainingPotions == 64 || remainingPotions == 32 || remainingPotions == 10)
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cAchtung, du kannst nur noch §7" + remainingPotions + " §cHeiltränke benutzen.");

                        if (!duel.hasRemainingPotions(player)) {
                            event.setCancelled(true);
                            player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu kannst keine Heiltränke mehr verwenden.");
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        public void onWin(DuelWinEvent event) {
            Player player = event.getWinner();
            Duel duel = event.getDuel();


            UserMoney playerMoney = Main.getInstance().getUserManager().getUser(player.getUniqueId()).getUserMoney();
            playerMoney.addMoney(duel.getRequest().getDeployment().getCoins() * 2);

            duel.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            duel.sendMessage(StringDefaults.PREFIX + "§c§l" + player.getName() + " §ehat das Duell gewonnen.");
            duel.sendMessage(" ");
            duel.sendMessage(StringDefaults.PREFIX + "§6§lGewinn§8: ");
            if (duel.getRequest().getDeployment().getCoins() > 0)
                duel.sendMessage("  §8● §eMünzen§8: §a§l" + Util.formatNumber((duel.getRequest().getDeployment().getCoins() * 2)) + "$");
            duel.sendMessage(" ");
            duel.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);

            AchievementGroups.$().getGroup(Category.COMABT).getAchievements().forEach(abstractAchievement -> {
                AchievementListener listener;
                if ((listener = abstractAchievement.getClass().getAnnotation(AchievementListener.class)) != null) {
                    if (listener.type() == Type.DUEL_WIN)
                        abstractAchievement.onEvent(event);
                }
            });
        }

    }

}
