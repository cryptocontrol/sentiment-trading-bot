package io.cryptocontrol.whitebird.trading;

import io.cryptocontrol.whitebird.models.Exchange;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author enamakel@cryptocontrol.io
 */
public class SpreadHistory {
    private static Map<String, Spread> map = new HashMap<>();


    public static Spread getSpread(Exchange longExchange, Exchange shortExchange) {
        String key = String.format("%s:%s", longExchange, shortExchange);

        if (map.containsKey(key)) return map.get(key);

        Spread spread = new Spread();
        map.put(key, spread);
        return spread;
    }


    @Data
    public static class Spread {
        Double lastSpread = 0.0;
        Double minSpread = 0.0;
        Double maxSpread = 0.0;
        Double trail = 0.0;
        Integer trailingWaitCount = 0;
        List<Double> volatility = new ArrayList<>();


        public void updateSpread(Double spread) {
            // Update the min/max spread
            lastSpread = spread;
            maxSpread = Math.max(spread, maxSpread);
            minSpread = Math.min(spread, minSpread);
        }


        public String toString() {
            if (trailingWaitCount > 1) return String.format("min %.4f%% | max %.4f%% | trail %.4f%% | trail count %d",
                    minSpread, maxSpread, trail, trailingWaitCount);

            return String.format("min %.4f%% | max %.4f%%", minSpread, maxSpread);
        }
    }
}
