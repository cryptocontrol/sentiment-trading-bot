package io.cryptocontrol.whitebird.trading;

import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.models.Opportunity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author enamakel@cryptocontrol.io
 */
public class Trader implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(Analyzer.class);
    private Context context;
    private Opportunity opportunity;


    public Trader(Opportunity opportunity) {
        Context context = Context.getInstance();
        this.context = context;
        this.opportunity = opportunity;
    }


    public void executeArbitrageOppotunity() throws InterruptedException {
        if (isAlreadyArbitrageRunning()) return;

        // TODO: make the entry trade
        logger.debug("Making the trade");
        opportunity.getLongExchange().setIsParticipatingInArbitrage(true);
        opportunity.getShortExchange().setIsParticipatingInArbitrage(true);

        // We are now in the market!

        // Loop until we find an exit opportunity
        Boolean hasFoundExitOpportunity = false;
//        while (!hasFoundExitOpportunity) {
//            // Niceness, to avoid DDoSing
//            Thread.sleep(1000);
//
//            // Query both the exchanges
//            Quote longQuote = opportunity.getLongExchange().getQuote();
//            Quote shortQuote = opportunity.getShortExchange().getQuote();
//
//            // We need both quotes from both exchanges. If one of them fails, we bail
//            if (longQuote == null || shortQuote == null) continue;
//
//            // Find an opportunity to exit.
//            logger.debug("Searching for opportunities");
////            hasFoundExitOpportunity = Analyzer.findArbitrageExitOpportunity(opportunity, longQuote,
////                    shortQuote);
//
//            // Once found, we exit from the trade.
//            if (hasFoundExitOpportunity) {
//                logger.debug("Exiting the trade");
//                // TODO: Make the exit trade
//                opportunity.getLongExchange().setIsParticipatingInArbitrage(false);
//                opportunity.getShortExchange().setIsParticipatingInArbitrage(false);
//                break;
//            }
//        }
    }


    /**
     * Checks if there is already an arbitrage trade running. Here we make that only one exchange can be involved in
     * exactly one arbitrage trade at a time.
     *
     * @return Return True iff there is already an arbitrage strategy running in one of the exchanges.
     */
    public boolean isAlreadyArbitrageRunning() {
        return opportunity.getLongExchange().getIsParticipatingInArbitrage() ||
                opportunity.getShortExchange().getIsParticipatingInArbitrage();
    }


    /**
     * Helper function to spawn the arbitrage trade in a new thread. This allows us to have multiple arbitrage
     * opportunities being executed at the same time.
     */
    public void runTradeInThread() {
        logger.debug("Spawning a thread");
        Thread thread = new Thread(this);
        thread.start();
    }


    @Override public void run() {
        try {
            executeArbitrageOppotunity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
