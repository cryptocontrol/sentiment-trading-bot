![license](https://img.shields.io/hexpm/l/plug.svg)

<p align="center">
<img src="https://raw.githubusercontent.com/cryptocontrol/sentiment-trading-bot/master/logo.png" width="50%" alt="Whitebird Sentiment-based crypto-trading bot">
</p>


### Introduction
Whitebird is a crypto-trading bot written in Java (inspired by [Blackbird](https://github.com/butor/blackbird), powered by [CryptoControl.io](https://cryptocontrol.io)) that trades on the basis of the public sentiment of a coin. If the coin sentiment is negative, Whitebird opens a short position on it. If the sentiment for a coin is positive, Whitebird goes long.

Whitebird uses exchange APIs to open/close positions and uses the [CryptoControl Sentiment API](https://cryptocontrol.io/apis) to get the sentiment for a particular coin based on crypto news sources.

For the purposes of this bot, CryptoControl has created a temporary API key that can be used for free until **September 31st 2018**. To get your own Sentiment API key visit [https://cryptocontrol.io/apis](https://cryptocontrol.io/apis)

```
API Key: 129310293j12k3m1238120391asdkj
```


### Disclaimer
USE THE SOFTWARE AT YOUR OWN RISK. YOU ARE RESPONSIBLE FOR YOUR OWN MONEY. PAST PERFORMANCE IS NOT NECESSARILY INDICATIVE OF FUTURE RESULTS.

THE AUTHORS AND ALL AFFILIATES ASSUME NO RESPONSIBILITY FOR YOUR TRADING RESULTS.


### How it works
Whitebird first takes a snapshot of your margin trading balance and queries prices every 5 seconds.

Using the Cryptocontrol API, Whitebird tries to figure out what the general sentiment of a coin looks like and if the current trend matches that sentiment (if the general sentiment of a coin is positive, then Whitebird expects the coin price should have gone up).

If the current trend doesn't match the coin sentiment, then Whitebird opens a respective long/short position (if the general sentiment of a coin is negative but there is an upward trend in the coin's price, then Whitebird opens a short position).

Whitebird closes a position if a certain threshold has been met or if the coin sentiment changes over time. (ie. if a coin's sentiment goes from positive to negative, Whitebird closes the position).

### Exchanges Supported
- Bitfinex
- Bitstamp
- OkCoin