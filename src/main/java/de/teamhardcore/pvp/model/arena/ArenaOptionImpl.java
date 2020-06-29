/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.arena;

import de.teamhardcore.pvp.Main;
import de.teamhardcore.pvp.model.fakeentity.FakeEntity;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionBase;
import de.teamhardcore.pvp.model.fakeentity.FakeEntityOptionImpl;
import org.bukkit.entity.Player;

public class ArenaOptionImpl extends ArenaOptionBase {

    private final ArenaOptionBase optionBase;
    private final Arena arena;
    private String[] params;

    public ArenaOptionImpl(ArenaOptionBase optionBase, Arena arena, String... params) {
        super(optionBase.getCategory(), optionBase.getOptionName());
        this.optionBase = optionBase;
        this.arena = arena;
        this.params = params;
    }

    public ArenaOptionBase getOptionBase() {
        return optionBase;
    }

    public Arena getArena() {
        return arena;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public boolean validateApplicableOption() {
        return true;
    }

    public void executeOnKill(Player player) {

    }

    public void executeOnDestroy() {

    }

    public static String serialize(ArenaOptionImpl option) {
        String category = option.getCategory();
        String optionName = option.getOptionName();
        String[] params = option.getParams();
        StringBuilder sb = new StringBuilder();
        sb.append(category).append("@@@").append(optionName);
        if (params != null && params.length > 0) {
            sb.append("@@@");
            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                sb.append(param);
                if (i < params.length - 1)
                    sb.append("###");
            }
        }
        return sb.toString();
    }

    public static ArenaOptionImpl deserialize(Arena arena, String optionStr) {
        ArenaOptionImpl handlingOption;
        String[] optionSpl = optionStr.split("@@@");
        String category = optionSpl[0];
        String optionName = optionSpl[1];
        String[] params = new String[0];

        if (optionSpl.length > 2)
            params = optionSpl[2].split("###");

        ArenaOptionBase option = Main.getInstance().getArenaManager().getOption(category, optionName);

        if (option == null)
            return null;

        Class<?> optionClass = Main.getInstance().getArenaManager().getOptionClass(option);

        if (optionClass == null) {
            return null;
        }

        try {
            handlingOption = (ArenaOptionImpl) optionClass.getConstructor(new Class[]{ArenaOptionBase.class, Arena.class, String[].class}).newInstance(new Object[]{option, arena, params});
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        if (!handlingOption.validateParams())
            return null;

        return handlingOption;
    }


}
