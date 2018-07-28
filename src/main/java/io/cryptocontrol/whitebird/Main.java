package io.cryptocontrol.whitebird;

import io.cryptocontrol.whitebird.models.Exchange;
import io.cryptocontrol.whitebird.models.exchanges.Bitfinex;
import io.cryptocontrol.whitebird.trading.Analyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * @author enamakel@cryptocontrol.io
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String DEFAULT_PARAMETER_FILE = "parameters.conf";


    /**
     * A helper function to read details from the parameter files and initialize all the
     * exchanges
     */
    private static void initializeExchanges()
            throws Exception {
        Context context = Context.getInstance();
        Parameters parameters = context.getParameters();
        List<Exchange> exchanges = context.getExchanges();

        // Start initializing and adding the exchanges one by one..
        if (parameters.bitfinexEnable) exchanges.add(new Bitfinex());
//        if (parameters.okcoinEnable) exchanges.add(new OkCoin());
//        if (parameters.bitstampEnable) exchanges.add(new Bitstamp());

        // Check if we have a minimum of two exchanges.
        if (exchanges.size() < 2) {
            throw new Exception(String.format("Whitebird needs at least two Bitcoin exchanges. Please edit the " +
                    "%s file to add new exchanges", DEFAULT_PARAMETER_FILE));
        }

        logger.info(String.format("Working with %d exchanges", exchanges.size()));

        // Now, get the user's balance for each one of the exchanges
        Analyzer.updateExchangeBalances();
        Analyzer.updateExchangePositions();
    }


    /**
     * A helper function to start the trading loop
     */
    private static void startTrading() {
//        Boolean stillRunning = true;
//        while (stillRunning) {
//            List<Quote> quotes = Analyzer.queryExchangesQuotes();
//            List<Opportunity> opportunities = Analyzer.findArbitrageEntryOpportunity(quotes);
//
//            for (Opportunity opportunity : opportunities) {
//                Trader trader = new Trader(opportunity);
//
//                // If an already existing arbitrage is running amongst any of the two exchanges, then we bail..
//                if (!trader.isAlreadyArbitrageRunning()) {
//                    // If not, then we spawn a new thread and perform the arbitrage there.
//                    trader.runTradeInThread();
//                }
//            }
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                logger.error(e.getMessage());
//            }
//        }
    }


    /**
     * Entry point of the app
     *
     * @param args Command line arguements
     */
    public static void main(String args[]) {
        Parameters parameters;
        Context context = Context.getInstance();

        System.out.println("Whitebird - Sentiment-based trading bot - " + new Date());
        System.out.println("Java based arbitrage system (powered by CryptoControl; inspired by Blackbird)");
        System.out.println("DISCLAIMER: USE THE SOFTWARE AT YOUR OWN RISK");
        System.out.println("---------------------------------------------------");

        try {
            // Get all the parameters
            logger.debug("reading parameters");
            if (args.length >= 1) parameters = Parameters.readFromFile(args[0]);
            else parameters = Parameters.readFromFile(DEFAULT_PARAMETER_FILE);
            context.setParameters(parameters);

            // Initialize all the exchanges
            logger.debug("initializing exchanges");
            initializeExchanges();

            // Start trading!
            startTrading();
        } catch (Exception e) {
            logger.error("shutting down because of exception: " + e);
            e.printStackTrace();
        }

        logger.debug("finish!");
    }
}
