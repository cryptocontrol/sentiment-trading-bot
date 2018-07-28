package io.cryptocontrol.whitebird.models;

import lombok.Data;

/**
 * @author enamakel@cryptocontrol.io
 */
@Data
public class Opportunity {
    Exchange shortExchange;
    Exchange longExchange;

    // The prices at which the trade is entered
    Double priceLongIn;
    Double priceShortIn;

    // The prices at which the trades are being exited
    Double priceLongOut;
    Double priceShortOut;

    Double exitTarget;

    // A simple number that should be incrememnted for every unit of time we are querying for an arbitrage opportunity.
    // Ideally this should determined if our trade is gotten too old and whether we force an exit or not.
    Integer timePeriod = 0;

    Double trailingSpread;
    Integer trailingWaitCount;


    public String toString() {
        // printing an opportunity that has found an exit
        if (priceLongOut != null && priceShortOut != null) {
            Double spread = (priceShortOut - priceLongOut) / priceLongOut;
            return String.format("%s @ %.02f->%.02f [long] | %s @ %.02f->%.02f [short] | %.02f%% [exit target] | %.02f%% [exit spread] ",
                    longExchange, priceLongIn, priceLongOut, shortExchange, priceShortIn, priceShortOut, exitTarget, spread);
        }

        // printing an opportunity that has just found an entry
        Double spread = (priceShortIn - priceLongIn) / priceLongIn;
        return String.format("%s @ %.02f [long] | %s @ %.02f [short] | %.02f%% [exit target]  | %.02f%% [entry spread]", longExchange,
                priceLongIn, shortExchange, priceShortIn, exitTarget, spread);
    }
}
