/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp;

import de.teamhardcore.pvp.commands.abuse.CommandAbuse;
import de.teamhardcore.pvp.commands.abuse.CommandBan;
import de.teamhardcore.pvp.commands.abuse.CommandMute;
import de.teamhardcore.pvp.commands.chat.CommandBroadcast;
import de.teamhardcore.pvp.commands.chat.CommandGlobalmute;
import de.teamhardcore.pvp.commands.chat.CommandMsg;
import de.teamhardcore.pvp.commands.help.CommandReport;
import de.teamhardcore.pvp.commands.help.CommandSupport;
import de.teamhardcore.pvp.commands.inventory.CommandBodysee;
import de.teamhardcore.pvp.commands.inventory.CommandEnderchest;
import de.teamhardcore.pvp.commands.inventory.CommandTrash;
import de.teamhardcore.pvp.commands.inventory.CommandWorkbench;
import de.teamhardcore.pvp.commands.player.*;
import de.teamhardcore.pvp.commands.pvp.CommandClan;
import de.teamhardcore.pvp.commands.pvp.CommandFix;
import de.teamhardcore.pvp.commands.pvp.CommandStack;
import de.teamhardcore.pvp.commands.teleport.*;
import de.teamhardcore.pvp.commands.warp.CommandSpawn;
import de.teamhardcore.pvp.commands.warp.CommandWarp;
import de.teamhardcore.pvp.commands.world.CommandNear;
import de.teamhardcore.pvp.commands.world.CommandSpawner;
import de.teamhardcore.pvp.database.DatabaseManager;
import de.teamhardcore.pvp.listeners.block.BlockBreak;
import de.teamhardcore.pvp.listeners.block.BlockPlace;
import de.teamhardcore.pvp.listeners.inventory.InventoryClick;
import de.teamhardcore.pvp.listeners.inventory.InventoryClose;
import de.teamhardcore.pvp.listeners.player.*;
import de.teamhardcore.pvp.listeners.world.SignChange;
import de.teamhardcore.pvp.managers.*;
import de.teamhardcore.pvp.model.clan.shop.upgrades.EnumUpgrade;
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
    private LeagueManager leagueManager;
    private ChatManager chatManager;
    private CrateManager crateManager;
    private CombatManager combatManager;
    private UserManager userManager;
    private ClanManager clanManager;
    private SpawnerManager spawnerManager;
    private TransactionManager transactionManager;
    private AbuseManager abuseManager;


    private DatabaseManager databaseManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        registerAll();
    }

    private void registerAll() {
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
        this.leagueManager = new LeagueManager(this);
        this.userManager = new UserManager(this);
        this.crateManager = new CrateManager(this);
        this.combatManager = new CombatManager(this);
        this.clanManager = new ClanManager(this);
        this.spawnerManager = new SpawnerManager(this);
        this.transactionManager = new TransactionManager(this);

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

    public LeagueManager getLeagueManager() {
        return leagueManager;
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

    public static Main getInstance() {
        return instance;
    }
}

