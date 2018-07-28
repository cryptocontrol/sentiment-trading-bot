package io.cryptocontrol.whitebird.trading;

import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.exceptions.BalanceFetchException;
import io.cryptocontrol.whitebird.exceptions.PositionFetchException;
import io.cryptocontrol.whitebird.exceptions.QuoteFetchException;
import io.cryptocontrol.whitebird.models.Balances;
import io.cryptocontrol.whitebird.models.Exchange;
import io.cryptocontrol.whitebird.models.Position;
import io.cryptocontrol.whitebird.models.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This class is responsible for checking the balance of all the different exchanges and
 * finding any opportunities for arbitrage.
 *
 * @author enamakel@cryptocontrol.io
 */
public class Analyzer {
    private static final Logger logger = LoggerFactory.getLogger(Analyzer.class);


    /**
     * Poll all the exchanges to find their bid/ask values.
     *
     * @return A list of all the quotes from the different exchanges
     */
    public static List<Quote> queryExchangesQuotes() {
        Context context = Context.getInstance();
        List<Exchange> exchanges = context.getExchanges();
        List<Quote> quotes = new ArrayList<>();

        logger.debug("querying different exchanges at " + new Date());

        for (Exchange exchange : exchanges) {
            // We don't need to query exchanges that already participating in an arbitrage here. (They get queried in
            // the Trade.java thread seperately.
            if (exchange.getIsParticipatingInArbitrage()) continue;

            // Get a quote from the exchange.
            try {
                List<Quote> exchangeQuotes = exchange.updateQuotes();

                // If we got the quote succesfully, we add it into our list
                quotes.addAll(exchangeQuotes);
                logger.debug(String.format("got %d quotes from %s", exchangeQuotes.size(), exchange));
            } catch (QuoteFetchException e) {
                e.printStackTrace();
            }
        }

        return quotes;
    }


    /**
     * Query the user's balances from the different exchanges
     *
     * @return A list of all the balances from the different exchanges
     * @throws BalanceFetchException if some the balances failed to get queried
     */
    public static List<Balances> updateExchangeBalances() throws BalanceFetchException {
        Context context = Context.getInstance();
        List<Exchange> exchanges = context.getExchanges();
        List<Balances> balances = new ArrayList<>();

        logger.debug("updating different exchanges balances at " + new Date());

        for (Exchange exchange : exchanges) exchange.updateBalances();

        return balances;
    }


    /**
     * Query the user's position from the different exchanges
     *
     * @return A list of all the positions  from the different exchanges
     * @throws PositionFetchException if some the positions failed to get queried
     */
    public static List<Position> updateExchangePositions() throws PositionFetchException {
        Context context = Context.getInstance();
        List<Exchange> exchanges = context.getExchanges();
        List<Position> positions = new ArrayList<>();

        logger.debug("updating different exchanges positions at " + new Date());

        for (Exchange exchange : exchanges) exchange.updatePositions();

        return positions;
    }
//
//
//    /**
//     * Given data polled from the different exchanges find any arbitrage opportunities
//     *
//     * @param quotes A list of all the quotes from the differnt exchanges
//     * @return A list of all the possible arbitrage opportunities.
//     */
//    public static List<Opportunity> findArbitrageEntryOpportunity(List<Quote> quotes) {
//        List<Opportunity> opportunities = new ArrayList<>();
////
////        // We need at least two quotes from different exchanges to check for arbitrage opportunities
////        if (quotes.size() < 2) return opportunities;
////
////        // Iterate through all the possible trade combinations to find any arbitrage opportunities.
////        for (Quote quote1 : quotes) {
////            for (Quote quote2 : quotes) {
////                if (quote1 == quote2) continue;
////
//////                Opportunity opportunity = findArbitrageOpportunity(quote1, quote2);
////                if (opportunity != null) opportunities.add(opportunity);
////            }
////        }
//
//        return opportunities;
//    }


//    /**
//     * Given an arbitrage oppotunity between two exchanges, find an exit oppotunity.
//     *
//     * @param opportunity
//     */
//    public static Boolean findArbitrageExitOpportunity(Opportunity opportunity, Quote longQuote, Quote shortQuote) {
//        Context context = Context.getInstance();
//        Parameters parameters = context.getParameters();
//        Double spreadOut = 0.0;
//        Exchange longExchange = longQuote.getExchange();
//        Exchange shortExchange = shortQuote.getExchange();
//        SpreadHistory.Spread spread = SpreadHistory.getSpread(longExchange, shortExchange);
//
//        // Gets the prices and computes the spread
//        Double priceLong = longQuote.getAsk();
//        Double priceShort = shortQuote.getBid();
//
//        // Calculate the spread
//        if (priceLong > 0.0 && priceShort > 0.0) spreadOut = (priceShort - priceLong) / priceLong;
//
//        // Update the min/max spread
//        spread.updateSpread(spreadOut);
//
//        // If the time period has exceeded the max time interval, we try to exit from the trade.
//        opportunity.setTimePeriod(opportunity.getTimePeriod() + 1);
//        if (opportunity.getTimePeriod() > parameters.getMaxTradeIterations()) {
//            opportunity.setPriceLongOut(priceLong);
//            opportunity.setPriceShortOut(priceShort);
//            return true;
//        }
//
//        if (spreadOut == 0.0) return false;
//
//        // TODO: comment on this
//        Double newTrailValue = spreadOut + parameters.getTrailingSpreadLim();
//        if (spread.trail == 1) {
//            spread.trail = Math.min(newTrailValue, opportunity.getExitTarget());
//            return false;
//        }
//
//        if (newTrailValue <= spread.trail) {
//            spread.trail = newTrailValue;
//            spread.trailingWaitCount = 0;
//        }
//
//        if (spreadOut <= spread.trail) {
//            spread.trailingWaitCount = 0;
//            return false;
//        }
//
//        // Check if we have exceed the trailing spread count
//        if (spread.trailingWaitCount < parameters.getTrailingSpreadCount()) {
//            spread.trailingWaitCount += 1;
//            return false;
//        }
//
//        opportunity.setPriceLongOut(priceLong);
//        opportunity.setPriceShortOut(priceShort);
//        spread.trailingWaitCount = 0;
//
//        logger.info("Exit opportunity found! " + opportunity);
//        return true;
//    }
//
//
//    /**
//     * Helper function to check for any arbitrage opportunity using the two different quotes given below.
//     *
//     * @param longQuote  A quote from the long exchange
//     * @param shortQuote A quote from the short exchange
//     * @return An arbitrage opportunity if found. Null otherwise
//     */
//    private static Opportunity findArbitrageOpportunity(Quote longQuote, Quote shortQuote) {
//        Context context = Context.getInstance();
//        Parameters parameters = context.getParameters();
//        Exchange longExchange = longQuote.getExchange();
//        Exchange shortExchange = shortQuote.getExchange();
//        SpreadHistory.Spread spread = SpreadHistory.getSpread(longExchange, shortExchange);
//        Double spreadIn = 0.0;
//
//        // Gets the prices and computes the spread
//        Double priceLong = longQuote.getAsk();
//        Double priceShort = shortQuote.getBid();
//
//        // If the prices are null we return a null spread to avoid false opportunities
//        if (priceLong > 0.0 && priceShort > 0.0) {
//            spreadIn = (priceShort - priceLong) / priceLong;
//        }
//
//        // Update the min/max spread
//        spread.updateSpread(spreadIn);
//
//
//        String logString = String.format("%s/%s \t [entry target %.4f | %s]",
//                longExchange, shortExchange, parameters.getSpreadEntry(), spread);
//
//        // The short-term volatility is computed and displayed. No other action with it for the moment.
////            if (parameters.getUseVolatility()) {
////                if (spread.volatility.size() > parameters.getVolatilityPeriod()) {
//////                    auto stdev = compute_sd(begin(res.volatility[longId][shortId]), end(res.volatility[longId][shortId]));
//////                    *params.logFile << "  volat. " << stdev * 100.0 << "%";
////                } else {
//////                    *params.logFile << "  volat. n/a " << res.volatility[longId][shortId].size() << "<" << params.volatilityPeriod << " ";
////                }
////            }
//
//        logger.debug(logString);
//
//
//        // the trailing spread is reset for this pair, because once the spread is *below* spreadEntry.
//        // Again, see #12 on GitHub for more details.
//        if (spreadIn < parameters.getSpreadEntry()) {
//            spread.setTrail(-1.0);
//            spread.setTrailingWaitCount(0);
//            return null;
//        }
//
//        // Update the trailingSpread with the new trail
//        Double newTrailValue = spreadIn - parameters.getTrailingSpreadLim();
//        if (spread.trail == -1) {
//            spread.trail = Math.max(newTrailValue, parameters.getSpreadEntry());
//            return null;
//        }
//
//        if (newTrailValue >= spread.trail) {
//            spread.setTrail(newTrailValue);
//            spread.setTrailingWaitCount(0);
//        }
//
//        if (spreadIn >= spread.trail) {
//            spread.setTrailingWaitCount(0);
//            return null;
//        }
//
//        // Check if we have exceed the trailing spread count
//        if (spread.getTrailingWaitCount() < parameters.getTrailingSpreadCount()) {
//            spread.setTrailingWaitCount(spread.getTrailingWaitCount() + 1);
//            return null;
//        }
//
//        // Create a new opportunity and return
//        Opportunity opportunity = new Opportunity();
//        opportunity.setLongExchange(longQuote.getExchange());
//        opportunity.setShortExchange(shortQuote.getExchange());
//        opportunity.setPriceLongIn(priceLong);
//        opportunity.setPriceShortIn(priceShort);
////        opportunity.setExitTarget(spreadIn - parameters.getSpreadTarget() - 2.0 *
////                (longExchange.getWithdrawlFees() + shortExchange.getWithdrawlFees()));
//
//        logger.info("New arbitrage opportunity found! " + opportunity);
//        return opportunity;
//    }
}
