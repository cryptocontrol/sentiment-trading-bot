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
     *
     * @return The reachable audience for this piece of content.
     */
    @Override public Long getReachableAudienceCount() {
        return null;
    }
}
