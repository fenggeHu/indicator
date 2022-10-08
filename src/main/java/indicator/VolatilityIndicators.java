package indicator;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import static indicator.Helper.*;
import static indicator.TrendIndicators.Ema;
import static indicator.TrendIndicators.Sma;

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

        double[] upperBand = Sma(20, multiply(high, addBy(multiplyBy(k, 4), 1)));
        double[] middleBand = Sma(20, closing);
        double[] lowerBand = Sma(20, multiply(low, addBy(multiplyBy(k, -4), 1)));

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

        double[] atr = Sma(period, tr);
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

        double[] bandWidthEma90 = Ema(90, bandWidth);

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
        double[] middleBand = Sma(20, closing);

        double[] std = StdFromSma(20, closing, middleBand);
        double[] std2 = multiplyBy(std, 2);

        double[] upperBand = add(middleBand, std2);
        double[] lowerBand = subtract(middleBand, std2);

        return Triple.of(middleBand, upperBand, lowerBand);
    }

    // Chandelier Exit. It sets a trailing stop-loss based on the Average True Value (ATR).
    //
    // Chandelier Exit Long = 22-Period SMA High - ATR(22) * 3
    // Chandelier Exit Short = 22-Period SMA Low + ATR(22) * 3
    //
    // Returns chandelierExitLong, chandelierExitShort
    public static Pair<double[], double[]> ChandelierExit(double[] high, double[] low, double[] closing) {
        double[] atr22 = Atr(22, high, low, closing).getRight();
        double[] highestHigh22 = TrendIndicators.Max(22, high);
        double[] lowestLow22 = TrendIndicators.Min(22, low);

        double[] chandelierExitLong = new double[closing.length];
        double[] chandelierExitShort = new double[closing.length];

        for (int i = 0; i < chandelierExitLong.length; i++) {
            chandelierExitLong[i] = highestHigh22[i] - (atr22[i] * 3);
            chandelierExitShort[i] = lowestLow22[i] + (atr22[i] * 3);
        }

        return Pair.of(chandelierExitLong, chandelierExitShort);
    }

    // Standard deviation.
    public static double[] Std(int period, double[] values) {
        return StdFromSma(period, values, Sma(period, values));
    }

    // Standard deviation from the given SMA.
    public static double[] StdFromSma(int period, double[] values, double[] sma) {
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

    // ProjectionOscillator calculates the Projection Oscillator (PO). The PO
    // uses the linear regression slope, along with highs and lows.
    //
    // Period defines the moving window to calculates the PO, and the smooth
    // period defines the moving windows to take EMA of PO.
    //
    // PL = Min(period, (high + MLS(period, x, high)))
    // PU = Max(period, (low + MLS(period, x, low)))
    // PO = 100 * (Closing - PL) / (PU - PL)
    // SPO = EMA(smooth, PO)
    //
    // Returns po, spo.
    public static Pair<double[], double[]> ProjectionOscillator(int period, int smooth, double[] high, double[] low, double[] closing) {
        double[] x = generateNumbers(0, closing.length, 1);
        double[] mHigh = Regression.MovingLeastSquare(period, x, high).getLeft();
        double[] mLow = Regression.MovingLeastSquare(period, x, low).getLeft();

        double[] vHigh = add(high, multiply(mHigh, x));
        double[] vLow = add(low, multiply(mLow, x));

        double[] pu = TrendIndicators.Max(period, vHigh);
        double[] pl = TrendIndicators.Min(period, vLow);

        double[] po = divide(multiplyBy(subtract(closing, pl), 100), subtract(pu, pl));
        double[] spo = Ema(smooth, po);

        return Pair.of(po, spo);
    }

    // The Ulcer Index (UI) measures downside risk. The index increases in value
    // as the price moves farther away from a recent high and falls as the price
    // rises to new highs.
    //
    // High Closings = Max(period, Closings)
    // Percentage Drawdown = 100 * ((Closings - High Closings) / High Closings)
    // Squared Average = Sma(period, Percent Drawdown * Percent Drawdown)
    // Ulcer Index = Sqrt(Squared Average)
    //
    // Returns ui.
    public static double[] UlcerIndex(int period, double[] closing) {
        double[] highClosing = TrendIndicators.Max(period, closing);
        double[] percentageDrawdown = multiplyBy(divide(subtract(closing, highClosing), highClosing), 100);
        double[] squaredAverage = Sma(period, multiply(percentageDrawdown, percentageDrawdown));
        double[] ui = sqrt(squaredAverage);

        return ui;
    }

    // The default ulcer index with the default period of 14.
    public static double[] DefaultUlcerIndex(double[] closing) {
        return UlcerIndex(14, closing);
    }

    // The Donchian Channel (DC) calculates three lines generated by moving average
    // calculations that comprise an indicator formed by upper and lower bands
    // around a midrange or median band. The upper band marks the highest
    // price of an asset while the lower band marks the lowest price of
    // an asset, and the area between the upper and lower bands
    // represents the Donchian Channel.
    //
    // Upper Channel = Mmax(period, closings)
    // Lower Channel = Mmin(period, closings)
    // Middle Channel = (Upper Channel + Lower Channel) / 2
    //
    // Returns upperChannel, middleChannel, lowerChannel.
    public static Triple<double[], double[], double[]> DonchianChannel(int period, double[] closing) {
        double[] upperChannel = TrendIndicators.Max(period, closing);
        double[] lowerChannel = TrendIndicators.Min(period, closing);
        double[] middleChannel = divideBy(add(upperChannel, lowerChannel), 2);

        return Triple.of(upperChannel, middleChannel, lowerChannel);
    }

    // The Keltner Channel (KC) provides volatility-based bands that are placed
    // on either side of an asset's price and can aid in determining the
    // direction of a trend.
    //
    // Middle Line = EMA(period, closings)
    // Upper Band = EMA(period, closings) + 2 * ATR(period, highs, lows, closings)
    // Lower Band = EMA(period, closings) - 2 * ATR(period, highs, lows, closings)
    //
    // Returns upperBand, middleLine, lowerBand.
    public static Triple<double[], double[], double[]> KeltnerChannel(int period, double[] high, double[] low, double[] closing) {
        double[] atr = Atr(period, high, low, closing).getRight();
        double[] atr2 = multiplyBy(atr, 2);

        double[] middleLine = Ema(period, closing);
        double[] upperBand = add(middleLine, atr2);
        double[] lowerBand = subtract(middleLine, atr2);

        return Triple.of(upperBand, middleLine, lowerBand);
    }

    // The default keltner channel with the default period of 20.
    public static Triple<double[], double[], double[]> DefaultKeltnerChannel(double[] high, double[] low, double[] closing) {
        return KeltnerChannel(20, high, low, closing);
    }

}
