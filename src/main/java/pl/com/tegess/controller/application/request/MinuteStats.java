package pl.com.tegess.controller.application.request;

public class MinuteStats {

    private long time;
    private long amount;

    public MinuteStats() {
    }

    public MinuteStats(long time, long amount) {
        this.time = time;
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
