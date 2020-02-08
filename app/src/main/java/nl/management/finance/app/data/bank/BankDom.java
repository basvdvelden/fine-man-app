package nl.management.finance.app.data.bank;

public class BankDom {
    private int id;
    private String name;

    public BankDom(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
