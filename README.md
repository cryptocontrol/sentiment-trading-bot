![license](https://img.shields.io/hexpm/l/plug.svg)
![wip](https://img.shields.io/badge/project%20status-work--in--progress-yellow.svg)

<p align="center">
<img src="https://raw.githubusercontent.com/cryptocontrol/sentiment-trading-bot/master/logo.png" width="50%" alt="Whitebird Sentiment-based crypto-trading bot">
</p>


### Introduction
(WIP) Whitebird is a crypto-trading bot written in Java (inspired by [Blackbird](https://github.com/butor/blackbird), powered by [CryptoControl.io](https://cryptocontrol.io)) that trades on the basis of the public sentiment of a coin. If the coin sentiment is negative, Whitebird opens a short position on it. If the sentiment for a coin is positive, Whitebird goes long.

Sentiment refers to the attitude expressed by an individual regarding a certain topic. This is especially relevant in trading, where so much of the change in price is dictated by emotions.

Whitebird uses exchange APIs to open/close positions and uses the [CryptoControl Sentiment API](https://cryptocontrol.io/apis) to get the sentiment for a particular coin based on crypto news sources.

Whitebird processes information from Twitter, Reddit & News articles and makes trading decisions on the basis of the activity from each of them.

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

If the current trend doesn't match the coin sentiment, then Whitebird accordingly opens a long/short position (for eg: if the general sentiment of a coin is negative but there is an upward trend in the coin's price, then Whitebird opens a short position).

Whitebird closes a position if a certain threshold has been met or if the coin sentiment changes over time. (for eg: if a coin's sentiment goes from positive to negative, then Whitebird closes the position).


### Usage
Have a look at the [`config.example.yml`](./config.example.yml) file for configuration options for Whitebird. Configuration options include api keys, profit margins, variables to control trading etc.

The easiest way to get Whitebird up and running is to use Docker and Docker Compose. Simply run:
```shell
# Using docker build and run whitebird
docker-compose up
```

And Whitebird should automatically get compiled and start running right away.

Or if you'd like to run Whitebird manually you can use the command line to do so.
```shell
# build and create the jar file
mvn clean package

# run the java file
java -cp target/whitebird.jar io.cryptocontrol.whitebird.Main
```


### Exchanges Supported
- Bitfinex
- Bitstamp
- OkCoin


### Papers Used
These are links to some of the papers and material used when building Whitebird.
- [Trading Strategies to Exploit Blog and News Sentiment](https://www.aaai.org/ocs/index.php/ICWSM/ICWSM10/paper/view/1529) by *Wenbin Zhang and Steven Skiena*
- [Blackbird](https://github.com/butor/blackbird) - a Bitcoin arbitrage bot with a long/short market-neutral strategy.

### Todo
Todo list for Whitebird and the CryptoControl team.
- Process & Monitor sentiment from Telegram groups.
- Process & Monitor sentiment from pump/dump groups & trading signal groups.