package io.cryptocontrol.whitebird.models.content;

/**
 * Represents a piece of content which is used to influence a coin.
 *
 * @author enamakel@cryptocontrol.io
 */
public interface Content {
    /**
     * Get the id from the content.
     *
     * @return A unique id
     */
    String getId();


    /**
     * Get the type of the content. (Article, tweet, reddit post, telegram post)
     *
     * @return The type of content.
     */
    ContentType getType();


    /**
     * Get's the sentiment for the given piece of content which is a number from -1 to 1. 0 is neutral, -1 is negative
     * sentiment, 1 is positive sentiment
     *
     * @return A number from -1 to 1
     */
    Double getSentiment();


    /**
     * This fn figures out how many people could this particular piece of content have reached. If the number is high
     * then we know that this piece of content can have more influence than others. Which means in effect, if the
     * sentiment is negative and audience count is high, then in effect the sentiment of the coin should become more
     * negative.
     *
     * @return The number of people this piece of content could reach out to.
     */
    Double getReachableAudienceCount();


    enum ContentType {
        ARTICLE,
        REDDIT_POST,
        TWEET,
        TELEGRAM_MESSAGE
    }
}
