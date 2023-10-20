package tese1;

public class Transaction {
    private final String payer;
    private final String payee;
    private final double amount;
    private final long timestamp;

    public Transaction(String payer, String payee, double amount, long timestamp) {
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getPayer() {
        return payer;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "payer='" + payer + '\'' +
                ", payee='" + payee + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
