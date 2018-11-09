package bg.sofia.uni.fmi.mjt.carstore.enums;

public enum Region {
    SOFIA("CB", 1000),
    BURGAS("A", 1000),
    VARNA("B", 1000),
    PLOVDIV("PB", 1000),
    RUSE("P", 1000),
    GABROVO("EB", 1000),
    VIDIN("BH", 1000),
    VRATSA("BP", 1000);

    private String prefix;
    private int number;
    private Region(String prefix, int number) {
        this.prefix = prefix;
        this.number = number;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getNumber() {
        return number;
    }

    public void increaseNumber() {
        number++;
    }
}
