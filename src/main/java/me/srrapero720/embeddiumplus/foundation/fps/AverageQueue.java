package me.srrapero720.embeddiumplus.foundation.fps;

import java.util.Arrays;

public class AverageQueue {
    private final int[] AVG_COUNT = new int[24];
    private boolean f = false;
    private int used = 0;

    public void push(int value) {
        if (this.used == this.AVG_COUNT.length) {
            this.used = 0;
            this.f = true;
        }

        if (!this.f) {
            Arrays.fill(this.AVG_COUNT, this.used, this.AVG_COUNT.length, value);
        }

        this.AVG_COUNT[this.used++] = value;
    }

    int calculate() {
        int times = 0;
        for (int i : AVG_COUNT) {
            times += i;
        }

        return times / AVG_COUNT.length;
    }
}
