package com.example.util;

public interface SlideHolder {
    boolean rivals$isSliding();
    void rivals$setSliding(boolean sliding);
    int rivals$getSlideTicks();
    void rivals$setSlideTicks(int ticks);
    int rivals$getSlideCooldown();
    void rivals$setSlideCooldown(int cooldown);
    boolean rivals$canStand();
}
