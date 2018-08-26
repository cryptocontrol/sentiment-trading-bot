package io.cryptocontrol.whitebird.models;

import lombok.Data;

/**
 * @author enamakel@cryptocontrol.io
 */
@Data
public class CurrencyPair {
    private Currency primary;
    private Currency secondary;

    public static final CurrencyPair LTCBTC = new CurrencyPair(Currency.LTC, Currency.BTC);
    public static final CurrencyPair BTCUSD = new CurrencyPair(Currency.BTC, Currency.USD);


    private CurrencyPair(Currency p, Currency s) {
        this.primary = p;
        this.secondary = s;
    }


    public String toString() {
        return "" + primary + "/" + secondary;
    }
}
