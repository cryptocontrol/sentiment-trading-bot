package io.cryptocontrol.whitebird.models;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Balances {
    Map<Currency, Double> map = new HashMap<>();


    public Double getBalance(Currency currency) {
        if (map.containsKey(currency)) return map.get(currency);
        return 0d;
    }


    public Double updateBalance(Currency currency, Double balance) {
        return map.put(currency, balance);
    }
}
