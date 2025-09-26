package org.gaboegui.backend.usersapp.security.workfactor;

public class BcryptWorkFactor {

    private int strength;
    private long duration;

    public BcryptWorkFactor(int strength, long duration) {
        this.strength = strength;
        this.duration = duration;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}