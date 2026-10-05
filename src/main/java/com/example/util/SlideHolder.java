package com.example.util;

public interface SlideHolder {
    boolean slide$isSliding();
    void slide$setSliding(boolean sliding);
    int slide$getSlideTicks();
    void slide$setSlideTicks(int ticks);
    int slide$getSlideCooldown();
    void slide$setSlideCooldown(int cooldown);
    boolean slide$canStand();
}
