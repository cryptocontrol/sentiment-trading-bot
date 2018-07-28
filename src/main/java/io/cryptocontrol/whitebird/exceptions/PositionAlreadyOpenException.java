package io.cryptocontrol.whitebird.exceptions;

import io.cryptocontrol.whitebird.models.Exchange;
import io.cryptocontrol.whitebird.models.Position;

/**
 * @author enamakel@cryptocontrol.io
 */
public class PositionAlreadyOpenException extends Exception {
    public PositionAlreadyOpenException(Position pos, Exchange ex) {
        super(String.format("the %s position is already open with the exchange %s.", pos.isShort() ? "short" : "long",
                ex));
    }
}
