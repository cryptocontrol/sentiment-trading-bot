package io.cryptocontrol.whitebird.models.exchanges;

import com.google.gson.JsonObject;
import io.cryptocontrol.whitebird.exceptions.BalanceFetchException;
import io.cryptocontrol.whitebird.exceptions.PositionFetchException;
import io.cryptocontrol.whitebird.exceptions.QuoteFetchException;
import io.cryptocontrol.whitebird.models.CurrencyPair;
import io.cryptocontrol.whitebird.models.Exchange;
import io.cryptocontrol.whitebird.models.Position;
import io.cryptocontrol.whitebird.models.Quote;
import io.cryptocontrol.whitebird.utils.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Bitfinex implementation
 *
 * @author enamakel@cryptocontrol.io
 */
public class Bitfinex extends Exchange {
    private static final String ROOT_URL = "https://api.bitfinex.com/v1";


    public Bitfinex() {
        super();
        name = "Bitfinex";
        tradingFees = parameters.getBitfinexTradingFees();
    }


    @Override public Quote getQuote(CurrencyPair pair) throws QuoteFetchException {
        try {
            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/ticker/ltcbtc");
            return new Quote(this, pair,
                    response.get("bid").getAsDouble(),
                    response.get("ask").getAsDouble(),
                    response.get("last_price").getAsDouble());
        } catch (Exception e) {
            throw new QuoteFetchException(this, pair);
        }
    }


    @Override protected List<Position> getPositions(CurrencyPair pair) throws PositionFetchException {
        List<Position> positions = new ArrayList<>();
        return positions;
    }


    @Override public List<Quote> updateQuotes() throws QuoteFetchException {
        List<Quote> quotes = new ArrayList<>();

        try {
            quotes.add(this.getQuote(CurrencyPair.LTCBTC));
        } catch (QuoteFetchException e) {
            e.printStackTrace();
        }

        return quotes;
    }


    @Override public void updateBalances() throws BalanceFetchException {
        try {
            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/ticker/btcusd");
//            return new Quote(this, response.get("bid").getAsDouble(),
//                    response.get("ask").getAsDouble(),
//                    response.get("last_price").getAsDouble());
        } catch (Exception e) {
            throw new BalanceFetchException(this);
        }
    }


    @Override public void updatePositions() throws PositionFetchException {
        List<Position> positions = new ArrayList<>();
        positions.addAll(getPositions(CurrencyPair.LTCBTC));
    }


    @Override public Position openLongPosition(CurrencyPair pair, Double quantity, Double price) {
        return null;
    }


    @Override public Position openShortPosition(CurrencyPair pair, Double quantity, Double price) {
        return null;
    }


    @Override public void closePosition(Position position) {

    }
}
