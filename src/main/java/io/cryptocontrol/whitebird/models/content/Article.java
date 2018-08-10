package io.cryptocontrol.whitebird.models.content;

/**
 * @author enamakel@cryptocontrol.io
 */
public class Article implements Content {
    private io.cryptocontrol.cryptonewsapi.models.Article CCArticle;


    public Article(io.cryptocontrol.cryptonewsapi.models.Article CCArticle) {
        this.CCArticle = CCArticle;
    }


    @Override public String getId() {
        return CCArticle.getId();
    }


    @Override public ContentType getType() {
        return ContentType.ARTICLE;
    }


    @Override public Double getSentiment() {
        return null;
    }


    /**
     * Get's the reachable audience count which can be estimated by taking the source's alexa rank.
     * <p>
     * Formula is rough estimate and fetched from: http://weeklym.com/alexa-rank-to-traffic-estimator/
     *
     * @return The reachable audience for this piece of content.
     */
    @Override public Double getReachableAudienceCount() {
        int alexaRank = 100000;

        if (alexaRank == 1) return 15000000d;
        if (alexaRank < 100000) return Math.ceil(30000 + (100000 - alexaRank) * 149.7);
        if (alexaRank < 200000) return Math.ceil(15000 + (200000 - alexaRank) * 0.15);
        if (alexaRank < 300000) return Math.ceil(10500 + (300000 - alexaRank) * 0.045);
        if (alexaRank < 400000) return Math.ceil(7500 + (400000 - alexaRank) * 0.03);
        if (alexaRank < 500000) return Math.ceil(4500 + (500000 - alexaRank) * 0.03);
        if (alexaRank < 750000) return Math.ceil(3000 + (750000 - alexaRank) * 0.012);
        if (alexaRank < 1000000) return Math.ceil(1500 + (1000000 - alexaRank) * 0.006);
        if (alexaRank < 1500000) return Math.ceil(750 + (1500000 - alexaRank) * 0.0015);
        if (alexaRank < 2000000) return 750d;
        if (alexaRank < 3000000) return 450d;
        if (alexaRank < 4000000) return 200d;
        if (alexaRank > 4000000) return 1d;

        return null;
    }
}
