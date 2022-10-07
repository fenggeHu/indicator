package indicator;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import static indicator.Helper.*;
import static indicator.TrendIndicators.ema;
import static indicator.TrendIndicators.sma;

/**
 * @author jinfeng.hu  @Date 2022-10-07
 **/
public class VolatilityIndicators {

    // Acceleration Bands. Plots upper and lower envelope bands
    // around a simple moving average.
    //
    // Upper Band = SMA(High * (1 + 4 * (High - Low) / (High + Low)))
    // Middle Band = SMA(Closing)
    // Lower Band = SMA(Low * (1 - 4 * (High - Low) / (High + Low)))
    //
    // Returns upper band, middle band, lower band.
    public static Triple<double[], double[], double[]> AccelerationBands(double[] high, double[] low, double[] closing) {
        checkSameSize(high, low, closing);

        double[] k = divide(subtract(high, low), add(high, low));

        double[] upperBand = sma(20, multiply(high, addBy(multiplyBy(k, 4), 1)));
        double[] middleBand = sma(20, closing);
        double[] lowerBand = sma(20, multiply(low, addBy(multiplyBy(k, -4), 1)));

        return Triple.of(upperBand, middleBand, lowerBand);
    }

    // Average True Range (ATR). It is a technical analysis indicator that measures market
    // volatility by decomposing the entire range of stock prices for that period.
    //
    // TR = Max((High - Low), (High - Closing), (Closing - Low))
    // ATR = SMA TR
    //
    // Returns tr, atr
    public static Pair<double[], double[]> Atr(int period, double[] high, double[] low, double[] closing) {
        checkSameSize(high, low, closing);

        double[] tr = new double[closing.length];
        for (int i = 0; i < tr.length; i++) {
            tr[i] = Math.max(high[i] - low[i], Math.max(high[i] - closing[i], closing[i] - low[i]));
        }

        double[] atr = sma(period, tr);
        return Pair.of(tr, atr);
    }

    // Bollinger Band Width. It measures the percentage difference between the
    // upper band and the lower band. It decreases as Bollinger Bands narrows
    // and increases as Bollinger Bands widens
    //
    // During a period of rising price volatity the band width widens, and
    // during a period of low market volatity band width contracts.
    //
    // Band Width = (Upper Band - Lower Band) / Middle Band
    //
    // Returns bandWidth, bandWidthEma90
    public static Pair<double[], double[]> BollingerBandWidth(double[] middleBand, double[] upperBand, double[] lowerBand) {
        checkSameSize(middleBand, upperBand, lowerBand);
        double[] bandWidth = new double[middleBand.length];
        for (int i = 0; i < bandWidth.length; i++) {
            bandWidth[i] = (upperBand[i] - lowerBand[i]) / middleBand[i];
        }

        double[] bandWidthEma90 = ema(90, bandWidth);

        return Pair.of(bandWidth, bandWidthEma90);
    }

    // Bollinger Bands.
    //
    // Middle Band = 20-Period SMA.
    // Upper Band = 20-Period SMA + 2 (20-Period Std)
    // Lower Band = 20-Period SMA - 2 (20-Period Std)
    //
    // Returns middle band, upper band, lower band.
    public static Triple<double[], double[], double[]> BollingerBands(double[] closing) {
        double[] middleBand = sma(20, closing);

        double[] std = stdFromSma(20, closing, middleBand);
        double[] std2 = multiplyBy(std, 2);

        double[] upperBand = add(middleBand, std2);
        double[] lowerBand = subtract(middleBand, std2);

        return Triple.of(middleBand, upperBand, lowerBand);
    }

    // Standard deviation.
    public static double[] std(int period, double[] values) {
        return stdFromSma(period, values, sma(period, values));
    }

    // Standard deviation from the given SMA.
    public static double[] stdFromSma(int period, double[] values, double[] sma) {
        double[] result = new double[values.length];

        double sum2 = 0.0;
        for (int i = 0; i < values.length; i++) {
            sum2 += values[i] * values[i];
            if (i < period - 1) {
                result[i] = 0.0;
            } else {
                result[i] = Math.sqrt(sum2 / period - sma[i] * sma[i]);
                double w = values[i - (period - 1)];
                sum2 -= w * w;
            }
        }

        return result;
    }
}
