package io.cryptocontrol.whitebird.exceptions;

import io.cryptocontrol.whitebird.models.Exchange;

/**
 * @author enamakel@cryptocontrol.io
 */
public class BalanceFetchException extends Exception {
    public BalanceFetchException(Exchange ex) {
        super(String.format("the user balance failed to fetch from exchange: %s. please check your API keys.", ex));
    }
}
