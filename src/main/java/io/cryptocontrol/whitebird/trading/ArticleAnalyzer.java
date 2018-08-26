package io.cryptocontrol.whitebird.trading;

import io.cryptocontrol.cryptonewsapi.CryptoControlApi;
import io.cryptocontrol.cryptonewsapi.models.Article;
import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.models.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author enamakel@cryptocontrol.io
 */
public class ArticleAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(ArticleAnalyzer.class);
    private static final List<Article> articles = new ArrayList<>();
    private static final List<String> articleIds = new ArrayList<>();

    private static Double sentiment24hr = 0d;


    public static void calculateGeneralSentiment(Currency coin) {
        Context context = Context.getInstance();

        CryptoControlApi api = new CryptoControlApi(context.getParameters().getCryptocontrolKey());

        // Enable the sentiment API
        api.enableSentiment();

        // Get news articles for the top coins
//        api.getTopNewsByCoin(getCoinSlug(coin), new CryptoControlApi.OnResponseHandler<List<Article>>() {
        api.getTopNews(new CryptoControlApi.OnResponseHandler<List<Article>>() {
            @Override public void onSuccess(List<Article> body) {
                processArticles(body);
            }


            @Override public void onFailure(Exception e) {

            }
        });
    }


    private static void processArticles(List<Article> body) {
        // First process all new articles
        for (Article article : body) {
            if (articleIds.contains(article.getId())) continue;
            if (article.getSentiment() == null) continue;

            articles.add(article);
            articleIds.add(article.getId());
        }

        logger.debug(String.format("processing a total of %d articles", articles.size()));

        // Now we calculate the general sentiment
        sentiment24hr = 0d;
        for (Article article : articles) {
            switch ((article.getSentiment())) {
                case "positive":
                    sentiment24hr += (article.getSentimentPositiveScore() / articles.size());
                    break;

                case "negative":
                    sentiment24hr -= (article.getSentimentNegativeScore() / articles.size());
                    break;

                case "neutral":
                default:
                    break;
            }
        }

        logger.debug(String.format("new sentiment is %d", sentiment24hr));
    }


    /**
     * Helper function to translate the Currency obj used in this java app to a string
     *
     * @param coin
     * @return
     */
    private static String getCoinSlug(Currency coin) {
        if (coin == Currency.BTC) return "bitcoin";
        return null;
    }
}
