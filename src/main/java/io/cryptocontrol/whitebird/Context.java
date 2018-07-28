package io.cryptocontrol.whitebird;

import io.cryptocontrol.whitebird.models.Exchange;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A context object describes the current state (or context) of the application.
 *
 * @author enamakel@cryptocontrol.io
 */
@Data
public class Context {
    private List<Exchange> exchanges = new ArrayList<Exchange>();
    private Parameters parameters;


    private static Context instance = new Context();


    public static Context getInstance() {
        return instance;
    }
}
