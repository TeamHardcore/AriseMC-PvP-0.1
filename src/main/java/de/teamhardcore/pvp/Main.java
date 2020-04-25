/*
 *
 * @author TeamHardcore
 *
 * Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp;

import de.teamhardcore.pvp.commands.help.CommandReport;
import de.teamhardcore.pvp.commands.help.CommandSupport;
import de.teamhardcore.pvp.commands.inventory.CommandBodysee;
import de.teamhardcore.pvp.commands.inventory.CommandEnderchest;
import de.teamhardcore.pvp.commands.inventory.CommandWorkbench;
import de.teamhardcore.pvp.commands.player.*;
import de.teamhardcore.pvp.commands.pvp.CommandFix;
import de.teamhardcore.pvp.commands.pvp.CommandStack;
import de.teamhardcore.pvp.commands.teleport.*;
import de.teamhardcore.pvp.commands.warp.CommandSpawn;
import de.teamhardcore.pvp.commands.warp.CommandWarp;
import de.teamhardcore.pvp.commands.world.CommandNear;
import de.teamhardcore.pvp.database.DatabaseManager;
import de.teamhardcore.pvp.listeners.inventory.InventoryClick;
import de.teamhardcore.pvp.listeners.inventory.InventoryClose;
import de.teamhardcore.pvp.listeners.player.AsyncPlayerChat;
import de.teamhardcore.pvp.listeners.player.PlayerJoin;
import de.teamhardcore.pvp.listeners.player.PlayerQuit;
import de.teamhardcore.pvp.managers.*;
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

    private UserManager userManager;

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

        this.warpManager = new WarpManager(this);
        this.manager = new Manager(this);
        this.supportManager = new SupportManager(this);
        this.scoreboardManager = new ScoreboardManager(this);
        this.rankingManager = new RankingManager(this);
        this.reportManager = new ReportManager(this);
        this.chatManager = new ChatManager(this);
        this.leagueManager = new LeagueManager(this);
        this.userManager = new UserManager(this);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AsyncPlayerChat(), this);
        pm.registerEvents(new PlayerJoin(this), this);
        pm.registerEvents(new PlayerQuit(this), this);
        pm.registerEvents(new InventoryClick(this), this);
        pm.registerEvents(new InventoryClose(this), this);

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

    public static Main getInstance() {
        return instance;
    }

}
