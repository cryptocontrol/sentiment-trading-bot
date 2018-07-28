package io.cryptocontrol.whitebird.models;

import lombok.Data;

/**
 * @author enamakel@cryptocontrol.io
 */
@Data
public class Quote {
    private Exchange exchange;
    private CurrencyPair currencyPair;
    private Double bid;
    private Double ask;
    private Double lastPrice;


    public Quote(Exchange exchange, CurrencyPair currencyPair, Double bid, Double ask, Double lastPrice) {
        this.exchange = exchange;
        this.currencyPair = currencyPair;
        this.bid = bid;
        this.ask = ask;
        this.lastPrice = lastPrice;
    }


    public Quote(Exchange exchange, CurrencyPair currencyPair, Double bid, Double ask) {
        this(exchange, currencyPair, bid, ask, null);
    }


    public String toString() {
        if (lastPrice != null)
            return String.format("%10s %s [bid: %.04f | ask: %.04f | last: %.04f]", exchange, currencyPair, bid, ask,
                    lastPrice);
        return String.format("%10s %s [bid: %.04f | ask: %.04f]", exchange, currencyPair, bid, ask);
    }
}
