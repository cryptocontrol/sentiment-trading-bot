package io.cryptocontrol.whitebird.models;

/**
 * @author enamakel@cryptocontrol.io
 */
public class Currency {
    private String name;

    public static final Currency BTC = new Currency("BTC");
    public static final Currency LTC = new Currency("LTC");
    public static final Currency USD = new Currency("USD");


    private Currency(String name) {
        this.name = name;
    }


    public String toString() {
        return name;
    }
}
