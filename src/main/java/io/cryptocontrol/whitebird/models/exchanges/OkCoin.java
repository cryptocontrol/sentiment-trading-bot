package io.cryptocontrol.whitebird.models.exchanges;

/**
 * @author enamakel@cryptocontrol.io
 */
public class OkCoin {
//    private static final String ROOT_URL = "https://www.okcoin.com/api";
//
//
//    public OkCoin() {
//        super();
//        name = "OkCoin";
//        withdrawlFees = parameters.getOkcoinWithdrawlFees();
//        tradingFees = parameters.getOkcoinTradingFees();
//        depositFees = parameters.getOkcoinDepositFees();
//    }
//
//
//    public Quote getQuote() {
//        try {
//            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/ticker.do?ok=1");
//            JsonObject ticker = response.get("ticker").getAsJsonObject();
//            return new Quote(this, CurrencyPair.LTCBTC,
//                    ticker.get("buy").getAsDouble(),
//                    ticker.get("sell").getAsDouble(),
//                    ticker.get("last").getAsDouble());
//        } catch (Exception e) {
//            logger.error("Couldn't fetch from OkCoin");
//        }
//
//        return null;
//    }
//
//
//    @Override public Balances updateBalances() {
//        return null;
//    }
//
//
//    @Override public String sendBuyOrder(CurrencyPair pair, Currency currency, Double quantity, Double price) {
//        return null;
//    }
//
//
//    @Override public Boolean isOrderComplete(String orderId) {
//        return null;
//    }
//
//
//    @Override public String getDepositAddress(Currency currency) {
//        return null;
//    }
//
//
//    @Override public String withdraw(Double amount, Currency currency, String address) {
//        return null;
//    }
}
