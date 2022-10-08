package indicator;

import static indicator.Helper.*;
import static indicator.TrendIndicators.*;

/**
 * @author fengge.hu  @Date 2022/10/8
 **/
public class VolumeIndicators {
    private static int NVI_STARTING_VALUE = 1000;
    private static int CMF_DEFAULT_PERIOD = 20;

    // Accumulation/Distribution Indicator (A/D). Cumulative indicator
    // that uses volume and price to assess whether a stock is
    // being accumulated or distributed.
    //
    // MFM = ((Closing - Low) - (High - Closing)) / (High - Low)
    // MFV = MFM * Period Volume
    // AD = Previous AD + CMFV
    //
    // Returns ad.
    public static double[] accumulationDistribution(double[] high, double[] low, double[] closing, long[] volume) {
        checkSameSize(high, low, closing);

        double[] ad = new double[closing.length];
        for (int i = 0; i < ad.length; i++) {
            if (i > 0) {
                ad[i] = ad[i - 1];
            }
            ad[i] += volume[i] * (((closing[i] - low[i]) - (high[i] - closing[i])) / (high[i] - low[i]));
        }

        return ad;
    }

    // On-Balance Volume (OBV). It is a technical trading momentum indicator that
    // uses volume flow to predict changes in stock price.
    //
    //                   volume, if Closing > Closing-Prev
    // OBV = OBV-Prev +       0, if Closing = Closing-Prev
    //                  -volume, if Closing < Closing-Prev
    //
    // Returns obv
    public static long[] obv(double[] closing, long[] volume) {
        if (closing.length != volume.length) {
            throw new RuntimeException("not all same size");
        }

        long[] obv = new long[volume.length];

        for (int i = 1; i < obv.length; i++) {
            obv[i] = obv[i - 1];

            if (closing[i] > closing[i - 1]) {
                obv[i] += volume[i];
            } else if (closing[i] < closing[i - 1]) {
                obv[i] -= volume[i];
            }
        }

        return obv;
    }

    // The Money Flow Index (MFI) analyzes both the closing price and the volume
    // to measure to identify overbought and oversold states. It is similar to
    // the Relative Strength Index (RSI), but it also uses the volume.
    //
    // Raw Money Flow = Typical Price * Volume
    // Money Ratio = Positive Money Flow / Negative Money Flow
    // Money Flow Index = 100 - (100 / (1 + Money Ratio))
    //
    // Retruns money flow index values.
    public static double[] moneyFlowIndex(int period, double[] high, double[] low, double[] closing, long[] volume) {
        double[] typicalPrice = typicalPrice(low, high, closing).getLeft();
        double[] rawMoneyFlow = multiply(typicalPrice, asDouble(volume));

        double[] signs = extractSign(diff(rawMoneyFlow, 1));
        double[] moneyFlow = multiply(signs, rawMoneyFlow);

        double[] positiveMoneyFlow = keepPositives(moneyFlow);
        double[] negativeMoneyFlow = keepNegatives(moneyFlow);

        double[] moneyRatio = divide(
                sum(period, positiveMoneyFlow),
                sum(period, multiplyBy(negativeMoneyFlow, -1)));

        double[] moneyFlowIndex = addBy(multiplyBy(pow(addBy(moneyRatio, 1), -1), -100), 100);

        return moneyFlowIndex;
    }

    // Default money flow index with period 14.
    public static double[] DefaultMoneyFlowIndex(double[] high, double[] low, double[] closing, long[] volume) {
        return moneyFlowIndex(14, high, low, closing, volume);
    }

    // The Force Index (FI) uses the closing price and the volume to assess
    // the power behind a move and identify turning points.
    //
    // Force Index = EMA(period, (Current - Previous) * Volume)
    //
    // Returns force index.
    public static double[] forceIndex(int period, double[] closing, long[] volume) {
        return ema(period, multiply(diff(closing, 1), asDouble(volume)));
    }

    // The default Force Index (FI) with window size of 13.
    public static double[] defaultForceIndex(double[] closing, long[] volume) {
        return forceIndex(13, closing, volume);
    }

    // The Ease of Movement (EMV) is a volume based oscillator measuring
    // the ease of price movement.
    //
    // Distance Moved = ((High + Low) / 2) - ((Priod High + Prior Low) /2)
    // Box Ratio = ((Volume / 100000000) / (High - Low))
    // EMV(1) = Distance Moved / Box Ratio
    // EMV(14) = SMA(14, EMV(1))
    //
    // Returns ease of movement values.
    public static double[] easeOfMovement(int period, double[] high, double[] low, long[] volume) {
        double[] distanceMoved = diff(divideBy(add(high, low), 2), 1);
        double[] boxRatio = divide(divideBy(asDouble(volume), 100000000), subtract(high, low));
        double[] emv = sma(period, divide(distanceMoved, boxRatio));
        return emv;
    }

    // The default Ease of Movement with the default period of 14.
    public static double[] defaultEaseOfMovement(double[] high, double[] low, long[] volume) {
        return easeOfMovement(14, high, low, volume);
    }

    // The Volume Price Trend (VPT) provides a correlation between the
    // volume and the price.
    //
    // VPT = Previous VPT + (Volume * (Current Closing - Previous Closing) / Previous Closing)
    //
    // Returns volume price trend values.
    public static double[] volumePriceTrend(double[] closing, long[] volume) {
        double[] previousClosing = shiftRightAndFillBy(1, closing[0], closing);
        double[] vpt = multiply(asDouble(volume), divide(subtract(closing, previousClosing), previousClosing));
        return sum(vpt.length, vpt);
    }

    // The Volume Weighted Average Price (VWAP) provides the average price
    // the asset has traded.
    //
    // VWAP = Sum(Closing * Volume) / Sum(Volume)
    //
    // Returns vwap values.
    public static double[] volumeWeightedAveragePrice(int period, double[] closing, long[] volume) {
        double[] v = asDouble(volume);
        return divide(sum(period, multiply(closing, v)), sum(period, v));
    }

    // Default volume weighted average price with period of 14.
    public static double[] defaultVolumeWeightedAveragePrice(double[] closing, long[] volume) {
        return volumeWeightedAveragePrice(14, closing, volume);
    }

    // The Negative Volume Index (NVI) is a cumulative indicator using
    // the change in volume to decide when the smart money is active.
    //
    // If Volume is greather than Previous Volume:
    //
    //     NVI = Previous NVI
    //
    // Otherwise:
    //
    //     NVI = Previous NVI + (((Closing - Previous Closing) / Previous Closing) * Previous NVI)
    //
    // Returns nvi values.
    public static double[] negativeVolumeIndex(double[] closing, long[] volume) {
        if (closing.length != volume.length) {
            throw new RuntimeException("not all same size");
        }

        double[] nvi = new double[closing.length];

        for (int i = 0; i < nvi.length; i++) {
            if (i == 0) {
                nvi[i] = NVI_STARTING_VALUE;
            } else if (volume[i - 1] < volume[i]) {
                nvi[i] = nvi[i - 1];
            } else {
                nvi[i] = nvi[i - 1] + (((closing[i] - closing[i - 1]) / closing[i - 1]) * nvi[i - 1]);
            }
        }

        return nvi;
    }

    // The Chaikin Money Flow (CMF) measures the amount of money flow volume
    // over a given period.
    //
    // Money Flow Multiplier = ((Closing - Low) - (High - Closing)) / (High - Low)
    // Money Flow Volume = Money Flow Multiplier * Volume
    // Chaikin Money Flow = Sum(20, Money Flow Volume) / Sum(20, Volume)
    //
    public static double[] chaikinMoneyFlow(double[] high, double[] low, double[] closing, long[] volume) {
        double[] moneyFlowMultiplier = divide(
                subtract(subtract(closing, low), subtract(high, closing)),
                subtract(high, low));

        double[] moneyFlowVolume = Helper.multiply(moneyFlowMultiplier, asDouble(volume));

        double[] cmf = divide(
                sum(CMF_DEFAULT_PERIOD, moneyFlowVolume),
                sum(CMF_DEFAULT_PERIOD, asDouble(volume)));

        return cmf;
    }

}
