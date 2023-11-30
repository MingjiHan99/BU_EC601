package tese1;

public class Sale {
    public Sale(String category, double amount, long time) {
        this.category = category;
        this.amount = amount;
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    private String category;
    private double amount;
    private long time;
}
