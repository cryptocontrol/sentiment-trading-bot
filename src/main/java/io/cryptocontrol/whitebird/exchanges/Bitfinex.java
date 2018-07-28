package io.cryptocontrol.whitebird.exchanges;

import com.google.gson.JsonObject;
import io.cryptocontrol.whitebird.models.*;
import io.cryptocontrol.whitebird.utils.Request;

/**
 * @author steven.enamakel@gmail.com
 */
public class Bitfinex extends Exchange {
    private static final String ROOT_URL = "https://api.bitfinex.com/v1";


    public Bitfinex() {
        super();
        name = "Bitfinex";
        tradingFeeds = parameters.getBitfinexTradingFees();
        withdrawlFees = parameters.getBitfinexWithdrawlFees();
        depositFees = parameters.getBitfinexDepositFees();
    }


    public Quote getQuote() {
        try {
            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/ticker/ltcbtc");
            return new Quote(this, CurrencyPair.LTCBTC,
                    response.get("bid").getAsDouble(),
                    response.get("ask").getAsDouble(),
                    response.get("last_price").getAsDouble());
        } catch (Exception e) {
            logger.error("Couldn't fetch from Bitfinex");
        }

        return null;
    }


    @Override public Balances updateBalances() {
        try {
            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/ticker/btcusd");
//            return new Quote(this, response.get("bid").getAsDouble(),
//                    response.get("ask").getAsDouble(),
//                    response.get("last_price").getAsDouble());
        } catch (Exception e) {
            logger.error("Couldn't fetch balances from Bitfinex");
        }
        return null;
    }


    @Override public String sendBuyOrder(CurrencyPair pair, Currency currency, Double quantity, Double price) {
//        balances.updateBalance()
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
