package io.cryptocontrol.whitebird.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A position in a margin trade
 *
 * @author enamakel@cryptocontrol.io
 */
@Getter
@AllArgsConstructor
public class Position {
    private boolean isShort;
    private Exchange exchange;
    private CurrencyPair currencyPair;
}
