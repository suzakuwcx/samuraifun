package Assert.Config;

public enum Role {
    COMMON(0),
    SAMURAI(1),
    RONIN(2),
    SHINBI(3),
    SOHEI(4);

    private final int value;
    
    private Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getSwordModelData(int data) {
        return value * 1000 + data;
    }
}
