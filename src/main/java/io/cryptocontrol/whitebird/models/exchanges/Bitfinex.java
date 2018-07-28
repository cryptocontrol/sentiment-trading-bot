package io.cryptocontrol.whitebird.models.exchanges;

import com.google.gson.JsonObject;
import com.oracle.javafx.jmx.json.JSONException;
import io.cryptocontrol.whitebird.exceptions.BalanceFetchException;
import io.cryptocontrol.whitebird.exceptions.PositionFetchException;
import io.cryptocontrol.whitebird.exceptions.QuoteFetchException;
import io.cryptocontrol.whitebird.models.*;
import io.cryptocontrol.whitebird.utils.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Bitfinex implementation
 *
 * @author enamakel@cryptocontrol.io
 */
public class Bitfinex extends Exchange {
    private static final Logger logger = LoggerFactory.getLogger(Bitfinex.class);
    private static final String ROOT_URL = "https://api.bitfinex.com";

    private static final String ALGORITHM_HMACSHA384 = "HmacSHA384";
    private long nonce = System.currentTimeMillis();
    private String apiKey;
    private String apiKeySecret;


    public Bitfinex() {
        super();
        name = "Bitfinex";

        apiKey = parameters.getBitfinexKey();
        apiKeySecret = parameters.getBitfinexSecret();
        tradingFees = parameters.getBitfinexTradingFees();

        logger.debug(String.format("initializing with key: %s, secret: %s, tradingfee: %f", apiKey, apiKeySecret,
                tradingFees));
    }


    @Override public Quote getQuote(CurrencyPair pair) throws QuoteFetchException {
        logger.debug("fetching pair: " + pair);

        try {
            JsonObject response = Request.makeGetJSONRequest(ROOT_URL + "/v1/ticker/ltcbtc");
            return new Quote(this, pair,
                    response.get("bid").getAsDouble(),
                    response.get("ask").getAsDouble(),
                    response.get("last_price").getAsDouble());
        } catch (Exception e) {
            throw new QuoteFetchException(this, pair);
        }
    }


    @Override protected List<Position> getPositions(CurrencyPair pair) throws PositionFetchException {
        logger.debug("updating all positions");
        List<Position> positions = new ArrayList<>();
        return positions;
    }


    @Override public List<Quote> updateQuotes() throws QuoteFetchException {
        logger.debug("updating all quotes");
        List<Quote> quotes = new ArrayList<>();

        try {
            quotes.add(this.getQuote(CurrencyPair.LTCBTC));
        } catch (QuoteFetchException e) {
            e.printStackTrace();
        }

        return quotes;
    }


    @Override public void updateBalances() throws BalanceFetchException {
        logger.debug("updating user balances");

        try {
            String res = sendRequestV1("/v1/balances", "POST");
            JSONArray jsonArray = new JSONArray(res);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject balanceObj = jsonArray.getJSONObject(i);
                if (balanceObj.getString("type").equals("trading")) {
                    Double amount = balanceObj.getDouble("available");
                    Currency currency = getCurrency(balanceObj.getString("currency"));

                    balances.updateBalance(currency, amount);
                }
            }

            logger.debug("updated user balances");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BalanceFetchException(this);
        }
    }


    @Override public void updatePositions() throws PositionFetchException {
        List<Position> positions = new ArrayList<>();
        positions.addAll(getPositions(CurrencyPair.LTCBTC));
    }


    @Override public Position openLongPosition(CurrencyPair pair, Double quantity, Double price) {
        return null;
    }


    @Override public Position openShortPosition(CurrencyPair pair, Double quantity, Double price) {
        return null;
    }


    @Override public void closePosition(Position position) {

    }


    private Currency getCurrency(String text) {
        switch (text) {
            case "ltc":
                return Currency.LTC;
            case "btc":
                return Currency.BTC;
            case "usd":
                return Currency.USD;
        }

        return null;
    }


    /**
     * Creates an authenticated request WITHOUT request parameters. Send a request for Balances.
     *
     * @return Response string if request successful
     * @throws IOException
     */
    private String sendRequestV1(String urlPath, String method) throws IOException {
        String sResponse;

        HttpURLConnection conn = null;

        try {
            URL url = new URL(ROOT_URL + urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jo = new JSONObject();
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(getNonce()));

            // API v1
            String payload = jo.toString();

            // this is usage for Base64 Implementation in Android. For pure java you can use java.util.Base64.Encoder
            // Base64.NO_WRAP: Base64-string have to be as one line string
            String payload_base64 = Base64.getEncoder().encodeToString(payload.getBytes("utf-8"));


            String payload_sha384hmac = hmacDigest(payload_base64, apiKeySecret, ALGORITHM_HMACSHA384);

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.addRequestProperty("X-BFX-APIKEY", apiKey);
            conn.addRequestProperty("X-BFX-PAYLOAD", payload_base64);
            conn.addRequestProperty("X-BFX-SIGNATURE", payload_sha384hmac);

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            return convertStreamToString(in);

        } catch (MalformedURLException e) {
            throw new IOException(e.getClass().getName(), e);
        } catch (ProtocolException e) {
            throw new IOException(e.getClass().getName(), e);
        } catch (IOException e) {

            String errMsg = e.getLocalizedMessage();

            if (conn != null) {
                try {
                    sResponse = convertStreamToString(conn.getErrorStream());
                    errMsg += " -> " + sResponse;
//                    Log.e(TAG, errMsg, e);
                    return sResponse;
                } catch (IOException e1) {
                    errMsg += " Error on reading error-stream. -> " + e1.getLocalizedMessage();
//                    Log.e(TAG, errMsg, e);
                    throw new IOException(e.getClass().getName(), e1);
                }
            } else {
                throw new IOException(e.getClass().getName(), e);
            }
        } catch (JSONException e) {
            String msg = "Error on setting up the connection to server";
            throw new IOException(msg, e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    private long getNonce() {
        return ++nonce;
    }


    private String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (InvalidKeyException e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return digest;

    }
}
