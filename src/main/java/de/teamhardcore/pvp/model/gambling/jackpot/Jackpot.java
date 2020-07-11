/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.gambling.jackpot;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.gambling.jackpot.phases.JackpotPhaseBase;
import de.teamhardcore.pvp.model.gambling.jackpot.phases.JackpotWaitingPhase;
import de.teamhardcore.pvp.utils.JSONMessage;
import de.teamhardcore.pvp.utils.StringDefaults;
import de.teamhardcore.pvp.utils.Util;
import org.bukkit.Bukkit;

import java.util.Random;

public class Jackpot {

    private static final Random RANDOM = new Random();

    private final JackpotMemberList memberList;
    private final JackpotInventory jackpotInventory;
    private final JackpotStatistics statistics;
    private final int id;

    private JackpotPhaseBase phase;
    private JackpotState state;


    public Jackpot(long maxAmount, int id) {
        this.state = JackpotState.WAITING_FOR_BETS;
        this.id = id;

        this.jackpotInventory = new JackpotInventory(this);
        this.memberList = new JackpotMemberList(this);
        this.statistics = new JackpotStatistics(this);

        startPhase(new JackpotWaitingPhase(this));
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lJACKPOT §8§l§m*-*-*-*-*-*-*-*-*");
            player.sendMessage(StringDefaults.PREFIX + "§eEin neuer Jackpot wurde gestartet!");
            player.sendMessage(
                    StringDefaults.PREFIX + "§eMaximaler Teilnahmebetrag§8: §7" + Util.formatNumber(maxAmount) + "$");
            new JSONMessage(StringDefaults.PREFIX + "§eTeilnehmen? §7§oKlicke hier!").tooltip("§eAm Jackpot teilnehmen")
                    .suggestCommand("/jackpot teilnehmen ").send(player);
            player.sendMessage("§8§l§m*-*-*-*-*-*-*-*-*§r §c§lJACKPOT §8§l§m*-*-*-*-*-*-*-*-*");
        });


        this.statistics.setMaxAmount(maxAmount);
        this.jackpotInventory.loadInventory();
    }

    public void startDrawing() {
        if (this.memberList.getMembers().isEmpty()) {
            Bukkit.broadcastMessage(
                    StringDefaults.PREFIX + "§cKein Spieler ist dem Jackpot beigetreten, der Jackpot wird abgebrochen.");
            Main.getInstance().getJackpotManager().stopJackpot();
            //todo: stop jackpot
            return;
        }
        this.jackpotInventory.startDrawing();
    }

    public JackpotMemberList getMemberList() {
        return memberList;
    }

    public JackpotInventory getJackpotInventory() {
        return jackpotInventory;
    }

    public JackpotState getState() {
        return state;
    }

    public void setState(JackpotState state) {
        this.state = state;
    }

    public JackpotStatistics getStatistics() {
        return statistics;
    }

    public int getId() {
        return id;
    }

    public void startPhase(JackpotPhaseBase phase) {
        this.phase = phase;
    }

    public JackpotPhaseBase getPhase() {
        return phase;
    }

}
