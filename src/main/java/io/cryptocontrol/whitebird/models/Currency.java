package io.cryptocontrol.whitebird.models;

/**
 * @author steven.enamakel@gmail.com
 */
public class Currency {
    String name;

    public static final Currency BTC = new Currency("BTC");
    public static final Currency LTC = new Currency("LTC");


    private Currency(String name) {
        this.name = name;
    }


    public String toString() {
        return name;
    }
}
