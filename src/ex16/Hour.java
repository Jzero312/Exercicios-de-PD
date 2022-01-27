package ex16;

import java.io.Serial;
import java.io.Serializable;

public class Hour implements Serializable
{
    @Serial
    private final static long serialVersionID = 1L;

    private int hours;
    private int minutes;
    private int seconds;

    public Hour(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return hours + ":" +
                minutes + ":" +
                seconds;
    }
}
