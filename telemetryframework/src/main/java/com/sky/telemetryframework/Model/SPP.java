package com.sky.telemetryframework.Model;

import java.util.ArrayList;


//SPP : Session Playback Pause

public class SPP extends Download {
    String pause_time;

    public SPP() {

    }

    public String getPause_time() {
        return pause_time;
    }

    public void setPause_time(String pause_time) {
        this.pause_time = pause_time;
        this.update();
    }

    public void update() {
        payload = new ArrayList<String>();
        payload.add(sessionId);
        payload.add(pause_time);
    }
}
