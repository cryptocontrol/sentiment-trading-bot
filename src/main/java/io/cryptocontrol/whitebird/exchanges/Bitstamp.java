package io.cryptocontrol.whitebird.exchanges;

import com.google.gson.JsonObject;
import io.cryptocontrol.whitebird.models.*;
import io.cryptocontrol.whitebird.utils.Request;

/**
 * @author steven.enamakel@gmail.com
 */
public class Bitstamp extends Exchange {
    private static final String ROOT_URL = "https://www.bitstamp.net/api";


    public Bitstamp() {
        super();
        name = "Bitstamp";
        tradingFeeds = parameters.getBitfinexTradingFees();
        withdrawlFees = parameters.getBitfinexWithdrawlFees();
        depositFees = parameters.getBitfinexDepositFees();
    }


    @Override public Quote getQuote() {
        try {
            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/v2/ticker/ltcbtc");
            return new Quote(this, CurrencyPair.LTCBTC,
                    response.get("bid").getAsDouble(),
                    response.get("ask").getAsDouble(),
                    response.get("last").getAsDouble());
        } catch (Exception e) {
            logger.error("Couldn't fetch from Bitstamp");
        }

        return null;
    }


    @Override public Balances updateBalances() {
        return null;
    }


    @Override public String sendBuyOrder(CurrencyPair pair, Currency currency, Double quantity, Double price) {
        return null;
    }


    @Override public Boolean isOrderComplete(String orderId) {
        return null;
    }


    @Override public String getDepositAddress(Currency currency) {
        return null;
    }


    @Override public String withdraw(Double amount, Currency currency, String address) {
        return null;
    }
}
