/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.arena;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.arena.Arena;
import de.teamhardcore.pvp.model.arena.ArenaOptionBase;
import de.teamhardcore.pvp.model.arena.ArenaOptionImpl;
import de.teamhardcore.pvp.model.arena.ArenaSelection;
import de.teamhardcore.pvp.model.fakeentity.FakeEntity;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionBase;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionImpl;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CommandArena implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.arena")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 0) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("position")) {
                ArenaSelection selection = Main.getInstance().getArenaManager().getSelection(player);

                if (selection == null) {
                    Main.getInstance().getArenaManager().getSelections().put(player, new ArenaSelection());
                    selection = Main.getInstance().getArenaManager().getSelection(player);
                }

                if (selection.getSelections().size() >= 2) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast bereits zwei Positionen makiert.");
                    player.sendMessage(StringDefaults.PREFIX + "§cVerwendung§8: §7/arena create <Name>");
                    return true;
                }

                Location location = player.getLocation();

                selection.addSelection(location);
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast die " + (selection.getSelections().size() == 1 ? "erste" : "zweite") + " Position gesetzt. (X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ() + ")");

                if (selection.getSelections().size() >= 2) {
                    player.sendMessage(StringDefaults.PREFIX + "§eDu hast zwei Positionen gesetzt.");
                    player.sendMessage(StringDefaults.PREFIX + "§eDu kannst nun eine Arena erstellen.");
                    return true;
                    //todo: spawn particle wand to display the region
                }

                return true;
            }

            sendHelp(player, label);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("position") && args[1].equalsIgnoreCase("clear")) {
                ArenaSelection selection = Main.getInstance().getArenaManager().getSelection(player);

                if (selection == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu hast keine Position ausgewählt.");
                    return true;
                }

                selection.getSelections().clear();
                selection.removeArmorStands();
                player.sendMessage(StringDefaults.PREFIX + "§eDu hast deine Positionen zurückgesetzt.");
                return true;
            }

            String name = args[1];

            if (args[0].equalsIgnoreCase("create")) {
                if (Main.getInstance().getArenaManager().getArenaByName(name) != null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert bereits eine Arena mit diesem Namen!");
                    return true;
                }

                if (Main.getInstance().getArenaManager().getArenaByLocation(player.getLocation()) != null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cAn deiner Position existiert bereits eine Arena.");
                    return true;
                }

                ArenaSelection selection = Main.getInstance().getArenaManager().getSelection(player);

                if (selection == null || selection.getSelections().size() < 2 || selection.getMaxLocation() == null || selection.getMinLocation() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDu musst vorher eine Region ausgewählt haben.");
                    return true;
                }

                selection.removeArmorStands();
                Main.getInstance().getArenaManager().createArena(name, player.getLocation(), selection.getMaxLocation(), selection.getMinLocation());
                Main.getInstance().getArenaManager().getSelections().remove(player);
                player.sendMessage(StringDefaults.PREFIX + "§Die Arena wurde erfolgreich erstellt.");
                return true;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (Main.getInstance().getArenaManager().getArenaByName(name) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert keine Arena mit diesem Namen!");
                    return true;
                }

                Main.getInstance().getArenaManager().deleteArena(name);
                player.sendMessage(StringDefaults.PREFIX + "§Die Arena wurde erfolgreich gelöscht.");
                return true;
            }

            sendHelp(player, label);
        }

        if (args.length == 3) {
            String name = args[1];

            if (Main.getInstance().getArenaManager().getArenaByName(name) == null) {
                player.sendMessage(StringDefaults.PREFIX + "§cEs existiert keine Arena mit diesem Namen!");
                return true;
            }
            Arena arena = Main.getInstance().getArenaManager().getArenaByName(name);

            if (args[0].equalsIgnoreCase("option") && args[2].equalsIgnoreCase("list")) {
                if (arena.getOptions().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§eEs sind keine Optionen gesetzt.");
                    return true;
                }

                player.sendMessage(StringDefaults.PREFIX + "§eFolgende Optionen sind gesetzt:");
                for (ArenaOptionBase option : arena.getOptions())
                    player.sendMessage(StringDefaults.PREFIX + "§7" + option.toString());
                return true;
            }

            if (args[0].equalsIgnoreCase("reset")) {
                boolean clan = (args[2].equalsIgnoreCase("clan"));
                arena.getArenaStatistics().resetStatistics(clan, !clan);
                arena.refreshHologram();

                player.sendMessage(StringDefaults.PREFIX + "§eDu hast die Arena Statistiken zurückgesetzt.");
                return true;
            }


            sendHelp(player, label);
        }

        if (args.length >= 4) {
            if (args[0].equalsIgnoreCase("option")) {
                String name = args[1];

                if (Main.getInstance().getArenaManager().getArenaByName(name) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert keine Arena mit diesem Namen!");
                    return true;
                }

                Arena arena = Main.getInstance().getArenaManager().getArenaByName(name);

                if (!args[2].equalsIgnoreCase("set") && !args[2].equalsIgnoreCase("remove")) {
                    sendHelp(player, label);
                    return true;
                }

                boolean set = args[2].equalsIgnoreCase("set");
                String[] optionSpl = args[3].split(":");

                if (optionSpl.length != 2) {
                    player.sendMessage(StringDefaults.PREFIX + "§cUngültige Option angegeben.");
                    return true;
                }

                String category = optionSpl[0].toLowerCase();
                String optionName = optionSpl[1].toLowerCase();
                ArenaOptionBase option = Main.getInstance().getArenaManager().getOption(category, optionName);

                if (option == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cUngültige Option angegeben.");
                    return true;
                }

                if (set) {
                    ArenaOptionImpl handlingOption;
                    String[] params = new String[0];
                    Class<?> optionClass = Main.getInstance().getArenaManager().getOptionClass(option);

                    if (args.length > 4) {
                        params = new String[args.length - 4];
                        System.arraycopy(args, 4, params, 0, args.length - 4);
                    }

                    if (optionClass == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cFehler bei der Verarbeitung der Option.");
                        return true;
                    }

                    try {
                        handlingOption = (ArenaOptionImpl) optionClass.getConstructor(new Class[]{ArenaOptionBase.class, Arena.class, String[].class}).newInstance(new Object[]{option, arena, params});
                    } catch (InstantiationException | java.lang.reflect.InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                        player.sendMessage(StringDefaults.PREFIX + "§cOption konnte nicht erstellt werden.");
                        e.printStackTrace();
                        return true;
                    }

                    if (!handlingOption.validateApplicableOption()) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDiese Option ist für den angegebenen Mob nicht nutzbar.");
                        return true;
                    }

                    if (!handlingOption.validateParams()) {
                        player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe gültige Parameter an.");
                        return true;
                    }

                    if (arena.hasArenaOption(handlingOption)) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDiese Option wurde bereits gesetzt.");
                        return true;
                    }
                    arena.addArenaOption(handlingOption);
                    player.sendMessage(StringDefaults.PREFIX + "§eDie Option wurde gesetzt.");
                } else {
                    if (!arena.hasArenaOption(option)) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDiese Option wurde nicht gesetzt.");
                        return true;
                    }
                    arena.removeArenaOption(option);
                    player.sendMessage(StringDefaults.PREFIX + "§eDie Option wurde entfernt.");
                }

                return true;
            }
            sendHelp(player, label);
        }

        return true;
    }

    private void sendHelp(Player player, String label) {
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " position");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " position clear");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " create <Name>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " delete <Name>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " option <Name> set <Option>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " option <Name> remove <Option>");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " option <Name> list");
        player.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " reset <Name> <Clan/Player>");
    }

}
