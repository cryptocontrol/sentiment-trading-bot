package io.cryptocontrol.whitebird.utils;

import io.cryptocontrol.cryptonewsapi.models.Article;
import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.models.Currency;
import io.cryptocontrol.whitebird.models.CurrencyPair;
import io.cryptocontrol.whitebird.models.Exchange;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author enamakel@cryptocontrol.io
 */
public class Analytics {
    private static final Logger logger = LoggerFactory.getLogger(Analytics.class);

    private static InfluxDB influxDB;
    private static String dbName;
    private static String rpName;


    public static void initialize() {
        if (influxDB != null) return;

        Context context = Context.getInstance();

        if (!context.getParameters().getInfluxdbEnabled()) return;

        influxDB = org.influxdb.InfluxDBFactory.connect(
                context.getParameters().getInfluxDBUrl(),
                context.getParameters().getInfluxDBUsername(),
                context.getParameters().getInfluxDBPassword()
        );

        logger.info("connected to influxdb!");

        dbName = context.getParameters().getInfluxDBDatabase();
        influxDB.createDatabase(dbName);

        rpName = "aRetentionPolicy";
        influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);

        return;
    }


    public static void trackCurrencyPriceChange(Exchange exchange, CurrencyPair pair, Double price, Double volume) {
        Point point = Point.measurement("currency_price")
                .tag("pair", pair.toString())
                .tag("exchange", exchange.toString())
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("price", price)
                .addField("volume", volume)
                .build();

        if (influxDB != null) influxDB.write(dbName, rpName, point);
    }


    public static void updateCoinArticleSentiment(Currency currency, Double sentiment) {
        Point point = Point.measurement("coin_sentiment")
                .tag("kind", "article")
                .tag("coin", currency.toString())
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("sentiment", sentiment)
                .build();

        if (influxDB != null) influxDB.write(dbName, rpName, point);
    }


    public static void trackNewArticleProcessed(Currency currency, Article article) {
        Point point = Point.measurement("articles_history")
                .tag("currency", currency.toString())
                .tag("article", article.getId())
                .tag("source", article.getSourceDomain())
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("count", 1)
                .addField("url", article.getUrl().replaceFirst("https://", ""))
                .addField("title", article.getTitle())
                .addField("source", article.getSourceDomain())
                .addField("hotness", article.getHotness())
                .addField("sentiment", article.getSentiment())
                .build();

        if (influxDB != null) influxDB.write(dbName, rpName, point);
    }
}
