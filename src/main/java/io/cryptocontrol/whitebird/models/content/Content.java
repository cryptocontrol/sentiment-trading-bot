package io.cryptocontrol.whitebird.models.content;

/**
 * @author enamakel@cryptocontrol.io
 */
public interface Content {
    String getId();


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
    Long getReachableAudienceCount();
}
