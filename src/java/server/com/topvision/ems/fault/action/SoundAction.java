package com.topvision.ems.fault.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.fault.service.FaultService;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.Action;

@Controller("soundAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SoundAction extends BaseAction {
    private static final long serialVersionUID = -1155836168471483010L;
    public static final int ACTION_TYPE = 4;
    private Long actionId;
    private String name = null;
    private String sound = null;
    @Autowired
    private FaultService faultService;
    private final String soundPath = SystemConstants.ROOT_REAL_PATH + "epon/sound/alertSounds";
    private List<String> sounds;

    private Action createAction() {
        Action action = new Action();
        action.setActionTypeId(ACTION_TYPE);
        action.setEnabled(true);
        action.setName(name);
        action.setParams(sound.getBytes());
        return action;
    }

    public String newSoundAction() {
        if (name == null) {
            sounds = new ArrayList<String>();
            for (String filename : new File(soundPath).list()) {
                sounds.add(filename);
            }
            return SUCCESS;
        }
        faultService.insertAction(createAction());
        return NONE;
    }

    public String showSoundAction() {
        Action action = faultService.getActionById(actionId);
        name = action.getName();
        sound = (String) action.getParamsObject();
        sounds = new ArrayList<String>();
        for (String filename : new File(soundPath).list()) {
            sounds.add(filename);
        }
        return SUCCESS;
    }

    public long getActionId() {
        return actionId;
    }

    public FaultService getFaultService() {
        return faultService;
    }

    public String getName() {
        return name;
    }

    public String getSound() {
        return sound;
    }

    public List<String> getSounds() {
        return sounds;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public void setFaultService(FaultService faultService) {
        this.faultService = faultService;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setSounds(List<String> sounds) {
        this.sounds = sounds;
    }

}
