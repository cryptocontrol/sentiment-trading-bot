package io.cryptocontrol.whitebird.exceptions;

import io.cryptocontrol.whitebird.models.Exchange;

/**
 * @author enamakel@cryptocontrol.io
 */
public class PositionFetchException extends Exception {
    public PositionFetchException(Exchange ex) {
        super(String.format("the positions failed to fetch from exchange: %s. please check your API keys.", ex));
    }
}
