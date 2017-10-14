package com.example.vlad.commitsupervisor;

import org.json.JSONObject;

/**
 * Created by vlad on 13/10/2017.
 */

public abstract class Event {

    abstract public void setEventData(JSONObject rawEvent);
}
