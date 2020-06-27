/*
 *   @author TeamHardcore
 *
 *   Copyright (c) 2020 by TeamHardcore to present. All rights reserved
 */

package de.teamhardcore.pvp.model.fakeentity;

import de.teamhardcore.pvp.Main;
import org.bukkit.entity.Player;

public class FakeEntityOptionImpl extends FakeEntityOptionBase {

    private final FakeEntityOptionBase base;
    private final FakeEntity entity;
    private String[] params;

    public FakeEntityOptionImpl(FakeEntityOptionBase option, FakeEntity entity, String... params) {
        super(option.getExecutingState(), option.getCategory(), option.getOptionName());
        this.base = option;
        this.entity = entity;
        this.params = params;
    }

    public FakeEntityOptionBase getBase() {
        return base;
    }

    public FakeEntity getEntity() {
        return entity;
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

    public void executeOnSpawn() {

    }

    public void executeOnClick(Player player) {

    }

    public void executeOnDestroy() {

    }

    public static String serialize(FakeEntityOptionImpl option) {
        FakeEntityOptionBase.ExecutingState state = option.getExecutingState();
        String category = option.getCategory();
        String optionName = option.getOptionName();
        String[] params = option.getParams();
        StringBuilder sb = new StringBuilder();
        sb.append(state.name()).append("@@@").append(category).append("@@@").append(optionName);
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

    public static FakeEntityOptionImpl deserialize(FakeEntity fakeEntity, String optionStr) {
        FakeEntityOptionImpl handlingOption;
        String[] optionSpl = optionStr.split("@@@");
        FakeEntityOptionBase.ExecutingState state = FakeEntityOptionBase.ExecutingState.getByName(optionSpl[0]);
        String category = optionSpl[1];
        String optionName = optionSpl[2];
        String[] params = new String[0];

        if (optionSpl.length > 3)
            params = optionSpl[3].split("###");

        FakeEntityOptionBase option = Main.getInstance().getFakeEntityManager().getOption(state, category, optionName);
        if (option == null)
            return null;
        Class<?> optionClass = Main.getInstance().getFakeEntityManager().getOptionClass(option);
        if (optionClass == null) {
            return null;
        }
        try {
            handlingOption = (FakeEntityOptionImpl) optionClass.getConstructor(new Class[]{FakeEntityOptionBase.class, FakeEntity.class, String[].class}).newInstance(new Object[]{option, fakeEntity, params});
        } catch (InstantiationException | IllegalAccessException | java.lang.reflect.InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        if (!handlingOption.validateParams())
            return null;
        return handlingOption;
    }

    public String toString() {
        if (this.params.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String param : this.params)
                sb.append(param).append(" ");
            return super.toString() + " [Params: " + sb.substring(0, sb.length() - 1) + "]";
        }
        return super.toString();
    }
}
