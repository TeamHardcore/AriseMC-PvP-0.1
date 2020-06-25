/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.managers;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.inventories.DuelInventory;
import de.teamhardcore.pvp.model.duel.Duel;
import de.teamhardcore.pvp.model.duel.event.DuelWinEvent;
import de.teamhardcore.pvp.model.duel.phases.DuelPhase;
import de.teamhardcore.pvp.model.duel.request.DuelConfiguration;
import de.teamhardcore.pvp.model.duel.request.DuelRequest;
import de.teamhardcore.pvp.model.duel.map.DuelMap;
import de.teamhardcore.pvp.model.achievements.AchievementGroups;
import de.teamhardcore.pvp.model.achievements.annotations.AchievementListener;
import de.teamhardcore.pvp.model.achievements.type.Category;
import de.teamhardcore.pvp.model.achievements.type.Type;
import de.teamhardcore.pvp.user.User;
import de.teamhardcore.pvp.user.UserData;
import de.teamhardcore.pvp.user.UserMoney;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import de.teamhardcore.pvp.utils.VirtualAnvil;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DuelManager {

    private final Main plugin;

    private final Map<UUID, Duel> duelCache;
    private final Set<UUID> playerQuits;

    private final Map<String, List<DuelMap>> duelMaps;
    private final Map<String, List<DuelMap>> availableMaps;
    private final Map<Player, DuelConfiguration> configurations;
    private final Map<Player, DuelRequest> requests;

    public DuelManager(Main plugin) {
        this.plugin = plugin;
        this.configurations = new HashMap<>();
        this.duelCache = new HashMap<>();
        this.playerQuits = new HashSet<>();
        this.duelMaps = new HashMap<>();
        this.availableMaps = new HashMap<>();
        this.requests = new HashMap<>();

        this.plugin.getServer().getPluginManager().registerEvents(new DuelEvents(this), this.plugin);
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

                System.out.println("-------- " + name + " ---------");

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

    public void addDuelRequest(Player player, DuelRequest request) {
        this.requests.put(player, request);
    }

    public void removeDuelRequest(Player player, DuelRequest request) {
        this.requests.remove(player);
    }

    public DuelRequest getDuelRequest(Player player) {
        if (!this.requests.containsKey(player))
            return null;
        return this.requests.get(player);
    }

    public void createDuel(DuelConfiguration request, Player player, Player target) {
        if (request.getPlayers().size() <= 1) return;

        Duel duel = new Duel(request);

        this.availableMaps.get(duel.getMap().getCategory()).remove(duel.getMap());
        this.duelCache.put(player.getUniqueId(), duel);
        this.duelCache.put(target.getUniqueId(), duel);
    }

    public void stopDuel(Duel duel) {
        for (Player player : duel.getPlayers())
            this.duelCache.remove(player.getUniqueId());
        this.availableMaps.get(duel.getMap().getCategory()).add(duel.getMap());
    }

    public Duel getDuel(String id) {
        return this.duelCache.values().stream().filter(duel -> duel.getId().toString().equals(id)).findFirst().orElse(null);
    }

    public Duel getDuel(Player player) {
        if (!this.duelCache.containsKey(player.getUniqueId()))
            return null;
        return this.duelCache.get(player.getUniqueId());
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

    public DuelMap getMap(String mapName) {
        for (Map.Entry<String, List<DuelMap>> entry : this.duelMaps.entrySet()) {
            for (DuelMap map : entry.getValue()) {
                if (map.getName().equals(mapName))
                    return map;
            }
        }
        return null;
    }

    public void removeMap(String name) {
        DuelMap toRemove = null;

        for (Map.Entry<String, List<DuelMap>> entry : this.duelMaps.entrySet()) {
            for (DuelMap map : entry.getValue()) {
                if (map.getName().equals(name)) {
                    toRemove = map;
                }
            }
            if (toRemove != null)
                entry.getValue().remove(toRemove);
        }
    }

    public void addMap(String name, String category) {
        if (!this.duelMaps.containsKey(category))
            return;

        if (getMap(name) != null) return;
        this.duelMaps.get(category).add(new DuelMap(name, category));
    }

    public Map<String, List<DuelMap>> getDuelMaps() {
        return duelMaps;
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

    public Map<Player, DuelConfiguration> getConfigurations() {
        return configurations;
    }

    public Map<Player, DuelRequest> getRequests() {
        return requests;
    }

    public Main getPlugin() {
        return plugin;
    }

    public static class DuelEvents implements Listener {

        private final String[] allowedDuelCommands = new String[]{"fix", "ifix", "bodyrepair", "stack", "report", "support"};

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

                if (playerDuel.getId().equals(damagerDuel.getId())) {
                    if (playerDuel.getPhase().getType() == DuelPhase.START)
                        event.setCancelled(true);
                }
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
            playerMoney.addMoney(duel.getConfiguration().getCoins() * 2);

            duel.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
            duel.sendMessage(StringDefaults.PREFIX + "§c§l" + player.getName() + " §ehat das Duell gewonnen.");
            duel.sendMessage(" ");
            duel.sendMessage(StringDefaults.PREFIX + "§6§lGewinn§8: ");
            if (duel.getConfiguration().getCoins() > 0)
                duel.sendMessage("  §8● §eMünzen§8: §a§l" + Util.formatNumber((duel.getConfiguration().getCoins() * 2)) + "$");
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

        @EventHandler
        public void onPlayerConsume(PlayerItemConsumeEvent event) {
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItem();

            if (itemStack.getType().equals(Material.GOLDEN_APPLE)) {
                if (this.manager.getDuelCache().containsKey(player.getUniqueId())) {
                    Duel duel = this.manager.getDuel(player);

                    if (duel.getConfiguration().getGoldenAppleOption() == 1) {
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDu kannst keine Goldenen Äpfel benutzen.");
                        event.setCancelled(true);
                        player.setItemInHand(itemStack);
                    }
                }
            }
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            Inventory inventory = event.getInventory();
            int slot = event.getRawSlot();
            ItemStack itemStack = event.getCurrentItem();

            if (inventory == null || itemStack == null || itemStack.getType() == Material.AIR) return;

            User user = this.manager.getPlugin().getUserManager().getUser(player.getUniqueId());
            UserData data = user.getUserData();
            if (inventory.getTitle().equalsIgnoreCase("§c§lDuelleinsatz")) {
                event.setCancelled(true);

                DuelConfiguration configuration = this.manager.getPlugin().getDuelManager().getConfigurations().get(player);
                if (configuration == null) {
                    player.closeInventory();
                    return;
                }

                if (slot == 11) {
                    configuration.switchArmorOption();
                    DuelInventory.openDeploymentInventory(player, configuration);
                    return;
                }

                if (slot == 13) {
                    new VirtualAnvil(player, "Einsatz: ") {
                        @Override
                        public void onConfirm(String text) {
                            if (text == null) {
                                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cBitte gebe einen gültigen Betrag an.");
                                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
                                return;
                            }

                            String coinString = text.startsWith("Einsatz: ") ? text.substring(9) : text;

                            long coins;
                            try {
                                coins = Long.parseLong(coinString);
                            } catch (NumberFormatException ex) {
                                player.sendMessage(StringDefaults.DUEL_PREFIX + "§cBitte gebe einen gültigen Betrag an.");
                                return;
                            }

                            setConfirmedSuccessfully(true);
                            configuration.setCoins(coins);
                            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> DuelInventory.openDeploymentInventory(player, configuration), 1L);
                        }

                        @Override
                        public void onCancel() {
                            if (!isConfirmedSuccessfully())
                                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> DuelInventory.openDeploymentInventory(player, configuration), 1L);
                        }
                    };
                    return;
                }

                if (slot == 18) {
                    DuelInventory.openRequestInventory(player, true, configuration);
                    return;
                }

            }

            if (inventory.getTitle().equalsIgnoreCase("§c§lDuelleinstellungen")) {
                event.setCancelled(true);

                DuelConfiguration configuration = this.manager.getPlugin().getDuelManager().getConfigurations().get(player);
                if (configuration == null) {
                    player.closeInventory();
                    return;
                }

                if (slot == 11) {
                    configuration.switchGoldenAppleOption();
                    DuelInventory.openSettingsInventory(player, configuration);
                    return;
                }

                if (slot == 13) {
                    configuration.switchPotionLimitation();
                    DuelInventory.openSettingsInventory(player, configuration);
                    return;
                }

                if (slot == 15) {
                    configuration.switchDebuffOption();
                    DuelInventory.openSettingsInventory(player, configuration);
                    return;
                }


                if (slot == 18) {
                    DuelInventory.openRequestInventory(player, true, configuration);
                    return;
                }

            }

            if (inventory.getTitle().equalsIgnoreCase("§c§lDuell")) {
                event.setCancelled(true);

                DuelConfiguration configuration = this.manager.getPlugin().getDuelManager().getConfigurations().get(player);
                if (configuration == null) {
                    player.closeInventory();
                    return;
                }

                if (slot == 11) {
                    configuration.switchCategory();
                    DuelInventory.openRequestInventory(player, true, configuration);
                    return;
                }

                if (slot == 13) {
                    DuelInventory.openSettingsInventory(player, configuration);
                }

                if (slot == 15) {
                    DuelInventory.openDeploymentInventory(player, configuration);
                }

                if (slot == 31) {
                    player.closeInventory();
                    player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                    player.sendMessage(" ");
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDie Konfiguration wurde gespeichert.");
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§eFordere jetzt deinen Gegner heraus:");
                    new JSONMessage(StringDefaults.DUEL_PREFIX + "§6/duell invite <Spieler> §7§o[Klick]").tooltip("§6Spieler herausfordern").suggestCommand("/duell invite ").send(player);
                    player.sendMessage(" ");
                    player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lDUELL §8§l§m*-*-*-*-*-*-*-*-*");
                }

            }

            if (inventory.getTitle().equalsIgnoreCase("§c§lDuell annehmen")) {
                event.setCancelled(true);

                DuelRequest request = this.manager.getDuelRequest(player);

                if (request == null) {
                    player.closeInventory();
                    return;
                }

                if (slot == 30) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Duellanfrage abgelehnt.");
                    request.getPlayer().sendMessage(StringDefaults.DUEL_PREFIX + "§eDer Spieler §7" + player.getName() + " §ehat die Duellanfrage abgelehnt.");
                    Main.getInstance().getDuelManager().removeDuelRequest(player, request);
                    player.closeInventory();
                }

                if (slot == 32) {
                    player.sendMessage(StringDefaults.DUEL_PREFIX + "§eDu hast die Duellanfrage angenommen.");
                    request.getPlayer().sendMessage(StringDefaults.DUEL_PREFIX + "§eDer Spieler §7" + player.getName() + " §ehat die Duellanfrage angenommen.");

                    request.getConfiguration().getPlayers().add(player);
                    Main.getInstance().getDuelManager().getRequests().remove(player);
                    Main.getInstance().getDuelManager().getConfigurations().remove(request.getPlayer());
                    Main.getInstance().getDuelManager().createDuel(request.getConfiguration(), request.getPlayer(), player);
                    player.closeInventory();
                }

            }


        }

        @EventHandler
        public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
            Player player = event.getPlayer();
            String fullCmd = event.getMessage().substring(1);
            String cmd = fullCmd.split(" ")[0];

            if (this.manager.getDuelCache().containsKey(player.getUniqueId())) {
                for (String allowed : this.allowedDuelCommands) {
                    if (!cmd.startsWith(allowed)) {
                        event.setCancelled(true);
                        player.sendMessage(StringDefaults.DUEL_PREFIX + "§cDieser Befehl ist im Duell nicht ausführbar.");
                        return;
                    }
                }
            }

        }
    }

}
