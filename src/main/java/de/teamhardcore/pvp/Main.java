/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp;

import de.howaner.FakeMobs.FakeMobsPlugin;
import de.realmeze.impl.MezeMain;
import de.teamhardcore.pvp.commands.abuse.CommandBan;
import de.teamhardcore.pvp.commands.abuse.CommandKick;
import de.teamhardcore.pvp.commands.abuse.CommandMute;
import de.teamhardcore.pvp.commands.arena.CommandArena;
import de.teamhardcore.pvp.commands.chat.CommandBroadcast;
import de.teamhardcore.pvp.commands.chat.CommandCommandSpy;
import de.teamhardcore.pvp.commands.chat.CommandGlobalmute;
import de.teamhardcore.pvp.commands.chat.CommandMsg;
import de.teamhardcore.pvp.commands.dev.CommandDebug;
import de.teamhardcore.pvp.commands.dev.CommandRelore;
import de.teamhardcore.pvp.commands.dev.CommandRename;
import de.teamhardcore.pvp.commands.entity.CommandFakeEntity;
import de.teamhardcore.pvp.commands.gambling.CommandCoinFlip;
import de.teamhardcore.pvp.commands.help.CommandReport;
import de.teamhardcore.pvp.commands.help.CommandSupport;
import de.teamhardcore.pvp.commands.inventory.*;
import de.teamhardcore.pvp.commands.player.*;
import de.teamhardcore.pvp.commands.punishment.CommandPunishment;
import de.teamhardcore.pvp.commands.pvp.*;
import de.teamhardcore.pvp.commands.rewards.CommandReward;
import de.teamhardcore.pvp.commands.teleport.*;
import de.teamhardcore.pvp.commands.warp.CommandHome;
import de.teamhardcore.pvp.commands.warp.CommandSpawn;
import de.teamhardcore.pvp.commands.warp.CommandWarp;
import de.teamhardcore.pvp.commands.world.CommandNear;
import de.teamhardcore.pvp.commands.world.CommandSpawner;
import de.teamhardcore.pvp.database.DatabaseManager;
import de.teamhardcore.pvp.listeners.block.BlockBreak;
import de.teamhardcore.pvp.listeners.block.BlockPlace;
import de.teamhardcore.pvp.listeners.custom.AchievementReceive;
import de.teamhardcore.pvp.listeners.custom.AchievementTierReceive;
import de.teamhardcore.pvp.listeners.custom.MobStackEvents;
import de.teamhardcore.pvp.listeners.entity.EntityDamage;
import de.teamhardcore.pvp.listeners.inventory.CrateEvents;
import de.teamhardcore.pvp.listeners.inventory.InventoryClick;
import de.teamhardcore.pvp.listeners.inventory.InventoryClose;
import de.teamhardcore.pvp.listeners.player.*;
import de.teamhardcore.pvp.listeners.world.FoodLevelChange;
import de.teamhardcore.pvp.listeners.world.PotionSplash;
import de.teamhardcore.pvp.listeners.world.SignChange;
import de.teamhardcore.pvp.managers.*;
import de.teamhardcore.pvp.utils.VirtualAnvil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private Manager manager;
    private FileManager fileManager;
    private WarpManager warpManager;
    private SupportManager supportManager;
    private ScoreboardManager scoreboardManager;
    private RankingManager rankingManager;
    private ReportManager reportManager;
    private ChatManager chatManager;
    private CrateManager crateManager;
    private CombatManager combatManager;
    private UserManager userManager;
    private ClanManager clanManager;
    private SpawnerManager spawnerManager;
    private TransactionManager transactionManager;
    private AbuseManager abuseManager;
    private KitManager kitManager;
    private MarketManager marketManager;
    private DuelManager duelManager;
    private AchievementManager achievementManager;
    private LootProtectionManager lootProtectionManager;
    private FakeEntityManager fakeEntityManager;
    private ArenaManager arenaManager;
    private CoinFlipManager coinFlipManager;
    private PunishmentManager punishmentManager;
    private TradeManager tradeManager;

    private DatabaseManager databaseManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDisable() {
        this.arenaManager.onDisable();
        this.tradeManager.onDisable();
        this.rankingManager.onDisable();
        this.crateManager.stopAllOpenings();
        this.fakeEntityManager.onDisable();
        FakeMobsPlugin.onDisable(this);
        //  this.databaseManager.onDisable();
    }

    @Override
    public void onEnable() {
        FakeMobsPlugin.onEnable(this);
        registerAll();
        VirtualAnvil.onEnable();
    }

    private void registerAll() {
        new MezeMain(this);
        this.fileManager = new FileManager(this);

    /*    this.databaseManager = new DatabaseManager(this);
        boolean hasConnection = this.databaseManager.init();

        if (!hasConnection) {
            Bukkit.getConsoleSender().sendMessage("§cCannot connect with database. Please check your settings.");
            Bukkit.getConsoleSender().sendMessage("§cServer is shutting down.");
            Bukkit.getServer().shutdown();
            return;
        }*/
        this.abuseManager = new AbuseManager(this);

        this.warpManager = new WarpManager(this);
        this.manager = new Manager(this);
        this.supportManager = new SupportManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.rankingManager = new RankingManager(this);
        this.reportManager = new ReportManager(this);
        this.chatManager = new ChatManager(this);
        this.userManager = new UserManager(this);
        this.crateManager = new CrateManager(this);
        this.combatManager = new CombatManager(this);
        this.clanManager = new ClanManager(this);
        this.spawnerManager = new SpawnerManager(this);
        this.transactionManager = new TransactionManager(this);
        this.kitManager = new KitManager(this);
        this.marketManager = new MarketManager(this);
        this.duelManager = new DuelManager(this);
        this.achievementManager = new AchievementManager(this);
        this.lootProtectionManager = new LootProtectionManager(this);
        this.fakeEntityManager = new FakeEntityManager(this);
        this.fakeEntityManager.loadAllCustomEntities();
        this.arenaManager = new ArenaManager(this);
        this.arenaManager.loadArenas();
        this.coinFlipManager = new CoinFlipManager(this);
        this.punishmentManager = new PunishmentManager(this);
        this.tradeManager = new TradeManager(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AsyncPlayerChat(this), this);
        pm.registerEvents(new AsyncPlayerPreLogin(), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerQuit(this), this);
        pm.registerEvents(new PlayerDeath(this), this);
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new PlayerCommandPreprocess(this), this);
        pm.registerEvents(new InventoryClick(this), this);
        pm.registerEvents(new InventoryClose(this), this);
        pm.registerEvents(new SignChange(this), this);
        pm.registerEvents(new BlockPlace(this), this);
        pm.registerEvents(new BlockBreak(this), this);
        pm.registerEvents(new FoodLevelChange(this), this);
        pm.registerEvents(new PotionSplash(this), this);
        pm.registerEvents(new PlayerItemConsume(this), this);
        pm.registerEvents(new PlayerMove(this), this);
        pm.registerEvents(new EntityDamage(this), this);
        pm.registerEvents(new PlayerPickupItem(this), this);
        pm.registerEvents(new AchievementReceive(this), this);
        pm.registerEvents(new AchievementTierReceive(this), this);
        pm.registerEvents(new CrateEvents(this), this);
        pm.registerEvents(new PlayerInteractFakeMob(this), this);
        pm.registerEvents(new MobStackEvents(this), this);

        getCommand("fix").setExecutor(new CommandFix());
        getCommand("stack").setExecutor(new CommandStack());
        getCommand("tpa").setExecutor(new CommandTpa());
        getCommand("tpall").setExecutor(new CommandTpall());
        getCommand("tphere").setExecutor(new CommandTphere());
        getCommand("tptop").setExecutor(new CommandTptop());
        getCommand("warp").setExecutor(new CommandWarp());
        getCommand("spawn").setExecutor(new CommandSpawn());
        getCommand("support").setExecutor(new CommandSupport());
        getCommand("back").setExecutor(new CommandBack());
        getCommand("workbench").setExecutor(new CommandWorkbench());
        getCommand("playertime").setExecutor(new CommandPlayertime());
        getCommand("feed").setExecutor(new CommandFeed());
        getCommand("heal").setExecutor(new CommandHeal());
        getCommand("near").setExecutor(new CommandNear());
        getCommand("enderchest").setExecutor(new CommandEnderchest());
        getCommand("hat").setExecutor(new CommandHat());
        getCommand("vanish").setExecutor(new CommandVanish());
        getCommand("bodysee").setExecutor(new CommandBodysee());
        getCommand("report").setExecutor(new CommandReport());
        getCommand("money").setExecutor(new CommandMoney());
        getCommand("globalmute").setExecutor(new CommandGlobalmute());
        getCommand("fill").setExecutor(new CommandFill());
        getCommand("broadcast").setExecutor(new CommandBroadcast());
        getCommand("msg").setExecutor(new CommandMsg());
        getCommand("trash").setExecutor(new CommandTrash());
        getCommand("clan").setExecutor(new CommandClan());
        getCommand("fly").setExecutor(new CommandFly());
        getCommand("spawner").setExecutor(new CommandSpawner());
        getCommand("extras").setExecutor(new CommandExtras());
        getCommand("ban").setExecutor(new CommandBan());
        getCommand("mute").setExecutor(new CommandMute());
        getCommand("debug").setExecutor(new CommandDebug());
        getCommand("kit").setExecutor(new CommandKit());
        getCommand("markt").setExecutor(new CommandMarkt());
        getCommand("more").setExecutor(new CommandMore());
        getCommand("duel").setExecutor(new CommandDuel());
        getCommand("invsee").setExecutor(new CommandInvsee());
        getCommand("home").setExecutor(new CommandHome());
        getCommand("kick").setExecutor(new CommandKick());
        getCommand("commandspy").setExecutor(new CommandCommandSpy());
        getCommand("achievements").setExecutor(new CommandAchievements());
        getCommand("rename").setExecutor(new CommandRename());
        getCommand("relore").setExecutor(new CommandRelore());
        getCommand("crates").setExecutor(new CommandCrate());
        getCommand("stats").setExecutor(new CommandStats());
        getCommand("ranking").setExecutor(new CommandRanking());
        getCommand("fakeentity").setExecutor(new CommandFakeEntity());
        getCommand("arena").setExecutor(new CommandArena());
        getCommand("liga").setExecutor(new CommandLeague());
        getCommand("reward").setExecutor(new CommandReward());
        getCommand("coinflip").setExecutor(new CommandCoinFlip());
        getCommand("punishment").setExecutor(new CommandPunishment());
        getCommand("trade").setExecutor(new CommandTrade());
        getCommand("friend").setExecutor(new CommandFriend());
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SupportManager getSupportManager() {
        return supportManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public RankingManager getRankingManager() {
        return rankingManager;
    }

    public Manager getManager() {
        return manager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public CrateManager getCrateManager() {
        return crateManager;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public SpawnerManager getSpawnerManager() {
        return spawnerManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public AbuseManager getAbuseManager() {
        return abuseManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public MarketManager getMarketManager() {
        return marketManager;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public LootProtectionManager getLootProtectionManager() {
        return lootProtectionManager;
    }

    public FakeEntityManager getFakeEntityManager() {
        return fakeEntityManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public CoinFlipManager getCoinFlipManager() {
        return coinFlipManager;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public TradeManager getTradeManager() {
        return tradeManager;
    }

    public static Main getInstance() {
        return instance;
    }
}

