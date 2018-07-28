package io.cryptocontrol.whitebird.exceptions;

import io.cryptocontrol.whitebird.models.CurrencyPair;
import io.cryptocontrol.whitebird.models.Exchange;

/**
 * @author enamakel@cryptocontrol.io
 */
public class QuoteFetchException extends Exception {
    public QuoteFetchException(Exchange ex, CurrencyPair pair) {
        super(String.format("The quote (%s) failed to fetch from exchange: %s", pair, ex));
    }
}
