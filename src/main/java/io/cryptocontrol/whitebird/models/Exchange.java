package io.cryptocontrol.whitebird.models;

import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.Parameters;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author steven.enamakel@gmail.com
 */
@Data
public abstract class Exchange {
    protected static final Logger logger = LoggerFactory.getLogger(Exchange.class);

    protected String name;
    protected Double tradingFeeds;
    protected Double withdrawlFees;
    protected Double depositFees;
    protected Parameters parameters;
    protected Balances balances = new Balances();


    // True iff the current exchange is participating is in an arbitrage already.
    protected Boolean isParticipatingInArbitrage = false;


    public Exchange() {
        parameters = Context.getInstance().getParameters();
    }


    /**
     * Gets the prices for the different currencies
     *
     * @return A quote of all the different prices, null if the prices fail to be found
     */
    public abstract Quote getQuote();


    /**
     * Updates the user's balances of the different currency pairs he/she holds in this exchange
     *
     * @return The balance of the user
     */
    public abstract Balances updateBalances();


    /**
     * Sends a buy order to buy a particular currency a
     *
     * @param currency
     * @param quantity
     * @param price
     * @return
     */
    public abstract String sendBuyOrder(CurrencyPair pair, Currency currency, Double quantity, Double price);


    /**
     * Checks if the order that has been sent has been completed or not
     *
     * @param orderId The id of the order
     * @return True iff the order has been completed
     */
    public abstract Boolean isOrderComplete(String orderId);


    /**
     * Gets a deposit address for this not to send the current currency to
     *
     * @param currency The current to be deposited
     * @return The address to deposit to
     */
    public abstract String getDepositAddress(Currency currency);


    /**
     * Withdraw the given currency form this exchange to the next one
     *
     * @param amount   The amount to transfer
     * @param currency The currency to be sent
     * @param address  The address to be sent to
     * @return The transaction id
     */
    public abstract String withdraw(Double amount, Currency currency, String address);


    public String toString() {
        return name;
    }
}
