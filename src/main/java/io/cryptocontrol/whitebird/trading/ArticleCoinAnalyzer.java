package io.cryptocontrol.whitebird.trading;

import io.cryptocontrol.cryptonewsapi.CryptoControlApi;
import io.cryptocontrol.cryptonewsapi.models.Article;
import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.models.Currency;
import io.cryptocontrol.whitebird.utils.Analytics;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author enamakel@cryptocontrol.io
 */
public class ArticleCoinAnalyzer {
    private final Logger logger;
    private final List<Article> articles = new ArrayList<>();
    private final List<String> articleIds = new ArrayList<>();

    @Getter private Double sentiment24hr = 0d;
    private Currency coin;


    public ArticleCoinAnalyzer(Currency coin) {
        this.coin = coin;
        logger = LoggerFactory.getLogger(coin.toString() + "ArticleAnalyzer");
    }


    public void queryGeneralSentiment() {
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


    private void processArticles(List<Article> body) {
        // First process all new articles
        for (Article article : body) {
            if (articleIds.contains(article.getId())) continue;
            if (article.getSentiment() == null) continue;

            articles.add(article);
            articleIds.add(article.getId());

            Analytics.trackNewArticleProcessed(coin, article);
        }

        logger.debug(String.format("processing a total of %d articles for %s", articles.size(), coin));

        // Now we calculate the general sentiment
        sentiment24hr = 0d;
        Double highestHotness = getHighestHotness(articles);
        for (Article article : articles) {
            Double sentimentScore = 0d;

            // Get the sentiment score; It should be a number from -1 to 1. Where 0 means neutral, -1 means negative
            // and 1 means positive
            switch ((article.getSentiment())) {
                case "positive":
                    sentimentScore += (article.getSentimentPositiveScore() / articles.size());
                    break;
                case "negative":
                    sentimentScore -= (article.getSentimentNegativeScore() / articles.size());
                    break;
                case "neutral":
                default:
                    break;
            }

            // Weigh the sentiment with the hotness of the article. The hotness is a number which comes from CryptoControl
            // that gives important to an article. The hotness takes into account many different factors such as the
            // alexa rank of the source,
            sentimentScore *= ((article.getHotness() + 1) / highestHotness);

            // TODO: add the sentiment with the alexa rank of the site being used as a weight. This is important to
            // weigh in the influence of the news source
            sentiment24hr += sentimentScore; // * (alexa rank) ^ -1
        }

        Analytics.updateCoinArticleSentiment(coin, sentiment24hr);

        // If the sentiment is negative
        logger.debug(String.format("new 24hr article sentiment for %s is %.04f", coin, sentiment24hr));
    }


    /**
     * Helper function to translate the Currency obj used in this java app to a string
     *
     * @param coin
     * @return
     */
    private String getCoinSlug(Currency coin) {
        if (coin == Currency.BTC) return "bitcoin";
        return null;
    }


    private Double getHighestHotness(List<Article> articles) {
        Double hotness = 0d;

        for (Article article : articles) {
            if (hotness < article.getHotness()) hotness = article.getHotness();
        }

        return 1d + hotness;
    }
}
