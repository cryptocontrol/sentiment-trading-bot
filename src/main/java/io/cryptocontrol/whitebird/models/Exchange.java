package io.cryptocontrol.whitebird.models;

import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.Parameters;
import io.cryptocontrol.whitebird.exceptions.BalanceFetchException;
import io.cryptocontrol.whitebird.exceptions.PositionFetchException;
import io.cryptocontrol.whitebird.exceptions.QuoteFetchException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author enamakel@cryptocontrol.io
 */
@Data
public abstract class Exchange {
    protected static final Logger logger = LoggerFactory.getLogger(Exchange.class);

    protected String name;
    protected Double tradingFees;
    protected Parameters parameters;
    protected Balances balances = new Balances();
    protected List<Position> positions = new ArrayList<>();


    // True iff the current exchange is participating is in an arbitrage already.
    protected Boolean isParticipatingInArbitrage = false;


    public Exchange() {
        parameters = Context.getInstance().getParameters();
    }


    /**
     * Gets the price for a given currency pair.
     *
     * @param pair The currency pair to get the quote for
     * @return The quote price for the given currency pair
     */
    protected abstract Quote getQuote(CurrencyPair pair) throws QuoteFetchException;


    /**
     * Gets all the positions for a given currency pair.
     *
     * @param pair The currency pair to get the quote for
     * @return A list of positions for the given currency pair
     */
    protected abstract List<Position> getPositions(CurrencyPair pair) throws PositionFetchException;


    /**
     * Gets the prices for the different currencies
     *
     * @return A quote of all the different prices
     */
    public abstract List<Quote> updateQuotes() throws QuoteFetchException;


    /**
     * Updates the user's balances of the different currency pairs he/she holds in this exchange
     */
    public abstract void updateBalances() throws BalanceFetchException;


    /**
     * Updates the user's balances of the different currency pairs he/she holds in this exchange
     */
    public abstract void updatePositions() throws PositionFetchException;


    /**
     * Opens a long position for the given currency pair
     *
     * @param pair     The currency pair to go long on
     * @param quantity The amount to go long on
     * @param price    The price at which we should go long on
     * @return The position that has been opened
     */
    public abstract Position openLongPosition(CurrencyPair pair, Double quantity, Double price);


    /**
     * Opens a short position for the given currency pair
     *
     * @param pair     The currency pair to short
     * @param quantity The amount to short
     * @param price    The price at which we should short
     * @return The position that has been opened
     */
    public abstract Position openShortPosition(CurrencyPair pair, Double quantity, Double price);


    /**
     * Close a position.
     *
     * @param position The position that needs to be closed.
     */
    public abstract void closePosition(Position position);


    public String toString() {
        return name;
    }
}
