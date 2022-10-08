package indicator;

import base.Quintuple;
import base.Pair;
import base.Triple;

import static indicator.Helper.*;
import static indicator.TrendIndicators.*;
import static indicator.VolumeIndicators.AccumulationDistribution;

/**
 * @author jinfeng.hu  @Date 2022/10/8
 **/
public class MomentumIndicators {

    // Awesome Oscillator.
    //
    // Median Price = ((Low + High) / 2).
    // AO = 5-Period SMA - 34-Period SMA.
    //
    // Returns ao.
    public static double[] AwesomeOscillator(double[] low, double[] high) {
        double[] medianPrice = divideBy(add(low, high), 2);
        double[] sma5 = Sma(5, medianPrice);
        double[] sma34 = Sma(34, medianPrice);
        double[] ao = subtract(sma5, sma34);

        return ao;
    }

    // The ChaikinOscillator function measures the momentum of the
    // Accumulation/Distribution (A/D) using the Moving Average
    // Convergence Divergence (MACD) formula. It takes the
    // difference between fast and slow periods EMA of the A/D.
    // Cross above the A/D line indicates bullish.
    //
    // CO = Ema(fastPeriod, AD) - Ema(slowPeriod, AD)
    //
    // Returns co, ad.
    public static Pair<double[], double[]> ChaikinOscillator(int fastPeriod, int slowPeriod, double[] low, double[] high, double[] closing, long[] volume) {
        double[] ad = AccumulationDistribution(high, low, closing, volume);
        double[] co = subtract(Ema(fastPeriod, ad), Ema(slowPeriod, ad));

        return Pair.of(co, ad);
    }

    // The DefaultChaikinOscillator function calculates Chaikin
    // Oscillator with the most frequently used fast and short
    // periods, 3 and 10.
    //
    // Returns co, ad.
    public static Pair<double[], double[]> DefaultChaikinOscillator(double[] low, double[] high, double[] closing, long[] volume) {
        return ChaikinOscillator(3, 10, low, high, closing, volume);
    }

    // Ichimoku Cloud. Also known as Ichimoku Kinko Hyo, is a versatile indicator that defines support and
    // resistence, identifies trend direction, gauges momentum, and provides trading signals.
    //
    // Tenkan-sen (Conversion Line) = (9-Period High + 9-Period Low) / 2
    // Kijun-sen (Base Line) = (26-Period High + 26-Period Low) / 2
    // Senkou Span A (Leading Span A) = (Conversion Line + Base Line) / 2
    // Senkou Span B (Leading Span B) = (52-Period High + 52-Period Low) / 2
    // Chikou Span (Lagging Span) = Closing plotted 26 days in the past.
    //
    // Returns conversionLine, baseLine, leadingSpanA, leadingSpanB, laggingSpan
    public static Quintuple<double[], double[], double[], double[], double[]> IchimokuCloud(double[] high, double[] low, double[] closing) {
        checkSameSize(high, low, closing);

        double[] conversionLine = divideBy(add(Max(9, high), Min(9, low)), 2);
        double[] baseLine = divideBy(add(Max(26, high), Min(26, low)), 2);
        double[] leadingSpanA = divideBy(add(conversionLine, baseLine), 2);
        double[] leadingSpanB = divideBy(add(Max(52, high), Min(52, low)), 2);
        double[] laggingLine = shiftRight(26, closing);

        return Quintuple.of(conversionLine, baseLine, leadingSpanA, leadingSpanB, laggingLine);
    }

    // Percentage Price Oscillator (PPO). It is a momentum oscillator for the price.
    // It is used to indicate the ups and downs based on the price. A breakout is
    // confirmed when PPO is positive.
    //
    // PPO = ((EMA(fastPeriod, prices) - EMA(slowPeriod, prices)) / EMA(longPeriod, prices)) * 100
    // Signal = EMA(9, PPO)
    // Histogram = PPO - Signal
    //
    // Returns ppo, signal, histogram
    public static Triple<double[], double[], double[]> PercentagePriceOscillator(int fastPeriod, int slowPeriod, int signalPeriod, double[] price) {
        double[] fastEma = Ema(fastPeriod, price);
        double[] slowEma = Ema(slowPeriod, price);
        double[] ppo = multiplyBy(divide(subtract(fastEma, slowEma), slowEma), 100);
        double[] signal = Ema(signalPeriod, ppo);
        double[] histogram = subtract(ppo, signal);

        return Triple.of(ppo, signal, histogram);
    }

    // Default Percentage Price Oscillator calculates it with the default periods of 12, 26, 9.
    //
    // Returns ppo, signal, histogram
    public static Triple<double[], double[], double[]> DefaultPercentagePriceOscillator(double[] price) {
        return PercentagePriceOscillator(12, 26, 9, price);
    }

    // Percentage Volume Oscillator (PVO). It is a momentum oscillator for the volume.
    // It is used to indicate the ups and downs based on the volume. A breakout is
    // confirmed when PVO is positive.
    //
    // PVO = ((EMA(fastPeriod, volumes) - EMA(slowPeriod, volumes)) / EMA(longPeriod, volumes)) * 100
    // Signal = EMA(9, PVO)
    // Histogram = PVO - Signal
    //
    // Returns pvo, signal, histogram
    public static Triple<double[], double[], double[]> PercentageVolumeOscillator(int fastPeriod, int slowPeriod, int signalPeriod, long[] volume) {
        double[] volumeAsFloat = asDouble(volume);
        double[] fastEma = Ema(fastPeriod, volumeAsFloat);
        double[] slowEma = Ema(slowPeriod, volumeAsFloat);
        double[] pvo = multiplyBy(divide(subtract(fastEma, slowEma), slowEma), 100);
        double[] signal = Ema(signalPeriod, pvo);
        double[] histogram = subtract(pvo, signal);

        return Triple.of(pvo, signal, histogram);
    }

    // Default Percentage Volume Oscillator calculates it with the default periods of 12, 26, 9.
    //
    // Returns pvo, signal, histogram
    public static Triple<double[], double[], double[]> DefaultPercentageVolumeOscillator(long[] volume) {
        return PercentageVolumeOscillator(12, 26, 9, volume);
    }

    // Relative Strength Index (RSI). It is a momentum indicator that measures the magnitude
    // of recent price changes to evaluate overbought and oversold conditions.
    //
    // RS = Average Gain / Average Loss
    // RSI = 100 - (100 / (1 + RS))
    //
    // Returns rs, rsi
    public static Pair<double[], double[]> Rsi(double[] closing) {
        return RsiPeriod(14, closing);
    }

    // RSI with 2 period, a mean-reversion trading strategy
    // developed by Larry Connors.
    //
    // REturns rs, rsi
    public static Pair<double[], double[]> Rsi2(double[] closing) {
        return RsiPeriod(2, closing);
    }

    // RsiPeriod allows to calculate the RSI indicator with a non-standard period.
    public static Pair<double[], double[]> RsiPeriod(int period, double[] closing) {
        double[] gains = new double[closing.length];
        double[] losses = new double[closing.length];

        for (int i = 1; i < closing.length; i++) {
            double difference = closing[i] - closing[i - 1];

            if (difference > 0) {
                gains[i] = difference;
                losses[i] = 0;
            } else {
                losses[i] = -difference;
                gains[i] = 0;
            }
        }

        double[] meanGains = Rma(period, gains);
        double[] meanLosses = Rma(period, losses);

        double[] rsi = new double[closing.length];
        double[] rs = new double[closing.length];

        for (int i = 0; i < rsi.length; i++) {
            rs[i] = meanGains[i] / meanLosses[i];
            rsi[i] = 100 - (100 / (1 + rs[i]));
        }

        return Pair.of(rs, rsi);
    }

    // Stochastic Oscillator. It is a momentum indicator that shows the location of the closing
    // relative to high-low range over a set number of periods.
    //
    // K = (Closing - Lowest Low) / (Highest High - Lowest Low) * 100
    // D = 3-Period SMA of K
    //
    // Returns k, d
    public static Pair<double[], double[]> StochasticOscillator(double[] high, double[] low, double[] closing) {
        checkSameSize(high, low, closing);

        double[] highestHigh14 = Max(14, high);
        double[] lowestLow14 = Min(14, low);

        double[] k = multiplyBy(divide(subtract(closing, lowestLow14), subtract(highestHigh14, lowestLow14)), 100);
        double[] d = Sma(3, k);

        return Pair.of(k, d);
    }

    // Williams R. Determine overbought and oversold.
    //
    // WR = (Highest High - Closing) / (Highest High - Lowest Low) * -100.
    //
    // Buy when -80 and below. Sell when -20 and above.
    //
    // Returns wr.
    public static double[] WilliamsR(double[] low, double[] high, double[] closing) {
        int period = 14;

        double[] highestHigh = Max(period, high);
        double[] lowestLow = Min(period, low);

        double[] result = new double[closing.length];

        for (int i = 0; i < closing.length; i++) {
            result[i] = (highestHigh[i] - closing[i]) / (highestHigh[i] - lowestLow[i]) * (-100);
        }

        return result;
    }

}
