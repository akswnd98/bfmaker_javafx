package bfmaker;

public class Cord {
    private int day;
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    private int time;
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public Cord(int day, int time) {
        this.day = day;
        this.time = time;
    }
}
