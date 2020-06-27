/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.commands.entity;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.fakeentity.FakeEntity;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionBase;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionImpl;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityType;
import de.teamhardcore.pvp.utils.StringDefaults;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandFakeEntity implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player))
            return true;

        Player player = (Player) cs;

        if (!player.hasPermission("arisemc.fakeentity")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        if (args.length == 0) {
            sendHelp(player, label);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (Main.getInstance().getFakeEntityManager().getCustomEntities().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existieren keine FakeEntities.");
                    return true;
                }

                StringBuilder builder = new StringBuilder();
                for (String name : Main.getInstance().getFakeEntityManager().getCustomEntities().keySet())
                    builder.append("§e").append(name).append("§7,");

                String names = builder.substring(0, builder.length() - 4);
                player.sendMessage(StringDefaults.PREFIX + "§cFolgende FakeEntities existieren§8: " + names);
                return true;
            }

            sendHelp(player, label);
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("remove")) {

                String name = args[1];

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                Main.getInstance().getFakeEntityManager().removeCustomEntity(name);
                player.sendMessage(StringDefaults.PREFIX + "§eDas FakeEntity wurde gelöscht.");
                return true;
            }

            if (args[0].equalsIgnoreCase("teleport")) {
                String name = args[1];

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                FakeEntity entity = Main.getInstance().getFakeEntityManager().getEntityByName(name);
                entity.setLocation(player.getLocation());
                player.sendMessage(StringDefaults.PREFIX + "§eDas FakeEntity wurde an deine Position teleportiert.");
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                FakeEntityType type = FakeEntityType.getTypeByName(args[1]);

                if (type == null) {
                    StringBuilder sb = new StringBuilder();
                    for (FakeEntityType types : FakeEntityType.values())
                        sb.append("§7").append(types.getName()).append("§8, ");
                    player.sendMessage(StringDefaults.PREFIX + "§cVerfügbare Typen: " + sb.substring(0, sb.length() - 4));
                    return true;
                }

                String name = args[2];

                if (Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert bereits ein FakeEntity mit diesem Namen.");
                    return true;
                }

                Main.getInstance().getFakeEntityManager().createNewCustomEntity(name, player.getLocation(), type);
                player.sendMessage(StringDefaults.PREFIX + "§eDas FakeEntity wurde ersellt.");
                return true;
            }


            if (args[0].equalsIgnoreCase("option") && args[1].equalsIgnoreCase("list")) {
                String name = args[2];

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                FakeEntity entity = Main.getInstance().getFakeEntityManager().getEntityByName(name);
                if (entity.getEntityOptions().isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§eEs sind keine Optionen gesetzt.");
                    return true;
                }

                player.sendMessage(StringDefaults.PREFIX + "§eFolgende Optionen sind gesetzt:");
                for (FakeEntityOptionBase option : entity.getEntityOptions())
                    player.sendMessage(StringDefaults.PREFIX + "§7" + option.toString());

                return true;
            }
        }

        if (args.length == 3 || args.length == 4) {
            if (args[0].equalsIgnoreCase("line") && args[1].equalsIgnoreCase("remove")) {
                String name = args[2];

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                FakeEntity entity = Main.getInstance().getFakeEntityManager().getEntityByName(name);
                List<String> lines = entity.getNametag();

                if (lines.isEmpty()) {
                    player.sendMessage(StringDefaults.PREFIX + "§cDas FakeEntity besitzt keinen NameTag.");
                    return true;
                }

                int pos = -1;

                if (args.length == 4) {
                    try {
                        pos = Integer.parseInt(args[3]);
                        if (pos < 1 || pos > lines.size())
                            throw new NumberFormatException();
                        pos--;
                    } catch (NumberFormatException e) {
                        player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe eine gültige Position ein. (1-" + lines.size() + ")");
                        return true;
                    }
                }

                lines.remove((pos == -1) ? (lines.size() - 1) : pos);
                entity.setNametag(lines);
                player.sendMessage(StringDefaults.PREFIX + "§eDie " + ((pos == -1) ? "letzte Zeile" : ("Zeile " + ++pos)) + " §edes Nametags wurde gelöscht.");
                return true;
            }
        }

        if (args.length >= 4) {
            if (args[0].equalsIgnoreCase("line") && args[1].equalsIgnoreCase("add")) {
                String name = args[2];
                int pos;

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                FakeEntity entity = Main.getInstance().getFakeEntityManager().getEntityByName(name);
                List<String> lines = entity.getNametag();

                try {
                    pos = Integer.parseInt(args[3]);
                    if (pos < 1 || pos > lines.size() + 1)
                        throw new NumberFormatException();
                    pos--;
                } catch (NumberFormatException e) {
                    pos = -1;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = (pos == -1) ? 3 : 4; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                String newText = ChatColor.translateAlternateColorCodes('&', builder.substring(0, builder.length() - 1));

                if (pos == -1)
                    lines.add(newText);
                else lines.add(pos, newText);

                entity.setNametag(lines);
                player.sendMessage(StringDefaults.PREFIX + "§eEs wurde" + ((pos == -1) ? "" : (" an der Stelle §7" + ++pos + "§e")) + " eine Zeile des Nametags hinzugefügt.");
                return true;
            }
        }

        if (args.length >= 5) {
            if (args[0].equalsIgnoreCase("option")) {
                String name = args[1];

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                FakeEntity entity = Main.getInstance().getFakeEntityManager().getEntityByName(name);

                if (!args[2].equalsIgnoreCase("set") && !args[2].equalsIgnoreCase("remove")) {
                    sendHelp(player, label);
                    return true;
                }

                boolean set = args[2].equalsIgnoreCase("set");
                FakeEntityOptionBase.ExecutingState state = FakeEntityOptionBase.ExecutingState.getByName(args[3]);

                if (state == null) {
                    StringBuilder sb = new StringBuilder();
                    for (FakeEntityOptionBase.ExecutingState states : FakeEntityOptionBase.ExecutingState.values())
                        sb.append(states.name().toLowerCase()).append("§8, §7");
                    player.sendMessage(StringDefaults.PREFIX + "§cVerfügbare Typen: §7" + sb.substring(0, sb.length() - 6));
                    return true;
                }

                String[] optionSpl = args[4].split(":");

                if (optionSpl.length != 2) {
                    player.sendMessage(StringDefaults.PREFIX + "§cUngültige Option angegeben.");
                    return true;
                }

                String category = optionSpl[0].toLowerCase();
                String optionName = optionSpl[1].toLowerCase();
                FakeEntityOptionBase option = Main.getInstance().getFakeEntityManager().getOption(state, category, optionName);

                if (option == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§cUngültige Option angegeben.");
                    return true;
                }

                if (set) {
                    FakeEntityOptionImpl handlingOption;
                    String[] params = new String[0];

                    if (args.length > 5) {
                        params = new String[args.length - 5];
                        System.arraycopy(args, 5, params, 0, args.length - 5);
                    }

                    Class<?> optionClass = Main.getInstance().getFakeEntityManager().getOptionClass(option);

                    if (optionClass == null) {
                        player.sendMessage(StringDefaults.PREFIX + "§cFehler bei der Verarbeitung der Option.");
                        return true;
                    }

                    try {
                        handlingOption = (FakeEntityOptionImpl) optionClass.getConstructor(new Class[]{FakeEntityOptionBase.class, FakeEntity.class, String[].class}).newInstance(new Object[]{option, entity, params});
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

                    if (entity.hasEntityOption(handlingOption)) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDiese Option wurde bereits gesetzt.");
                        return true;
                    }
                    entity.addEntityOption(handlingOption);
                    player.sendMessage(StringDefaults.PREFIX + "§eDie Option wurde gesetzt.");
                } else {
                    if (!entity.hasEntityOption(option)) {
                        player.sendMessage(StringDefaults.PREFIX + "§cDiese Option wurde nicht gesetzt.");
                        return true;
                    }
                    entity.removeEntityOption(option);
                    player.sendMessage(StringDefaults.PREFIX + "§eDie Option wurde entfernt.");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("line") && args[1].equalsIgnoreCase("set")) {
                int pos;
                String name = args[2];

                if (!Main.getInstance().getFakeEntityManager().isEntityExists(name)) {
                    player.sendMessage(StringDefaults.PREFIX + "§cEs existiert kein FakeEntity mit diesem Namen.");
                    return true;
                }

                FakeEntity entity = Main.getInstance().getFakeEntityManager().getEntityByName(name);
                List<String> lines = entity.getNametag();


                try {
                    pos = Integer.parseInt(args[3]);
                    if (pos < 1 || pos > lines.size())
                        throw new NumberFormatException();
                    pos--;
                } catch (NumberFormatException e) {
                    player.sendMessage(StringDefaults.PREFIX + "§cBitte gebe eine gültige Zeile an.");
                    return true;
                }

                StringBuilder sb = new StringBuilder();

                for (int i = 4; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String newText = ChatColor.translateAlternateColorCodes('&', sb.substring(0, sb.length() - 1));

                lines.set(pos, newText);
                entity.setNametag(lines);

                player.sendMessage(StringDefaults.PREFIX + "§eEs wurde die Zeile §7" + ++pos + " §edes Nametags editiert.");
                return true;
            }
        }

        sendHelp(player, label);
        return true;
    }

    private void sendHelp(Player p, String label) {
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " create <Typ> <Name>");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " remove <Entity>");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " teleport <Entity>");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " line set <Entity> <Zeile> <Text>");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " line add <Entity> [Zeile] <Text>");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " line remove <Entity> [Zeile]");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " option list <Entity>");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " option <Entity> <set|remove> <Typ> <Option> [Parameter]");
        p.sendMessage(StringDefaults.PREFIX + "§cVerwendung: §7/" + label + " list");

        StringBuilder sb = new StringBuilder();
        for (FakeEntityType types : FakeEntityType.values())
            sb.append("§7").append(types.getName()).append("§8, ");
        p.sendMessage(StringDefaults.PREFIX + "r§cVerfügbare Typen: " + sb.substring(0, sb.length() - 4));
    }
}
