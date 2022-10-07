package indicator;

import org.apache.commons.lang3.tuple.Pair;

import static indicator.Helper.subtract;

/**
 * @author jinfeng.hu  @Date 2022-10-07
 **/
public class TrendIndicators {

    // Simple Moving Average (SMA).
    public static double[] sma(int period, double[] values) {
        double[] result = new double[values.length];
        double sum = 0.00;

        for (int i = 0; i < values.length; i++) {
            int count = i + 1;
            sum += values[i];

            if (i >= period) {
                sum -= values[i - period];
                count = period;
            }

            result[i] = sum / count;
        }

        return result;
    }

    // Exponential Moving Average (EMA).
    public static double[] ema(int period, double[] values) {
        double[] result = new double[values.length];

        double k = 2.00 / (1 + period);
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                result[i] = (values[i] * k) + (result[i - 1] * (1 - k));
            } else {
                result[i] = values[i];
            }
        }

        return result;
    }

    // Moving Average Convergence Divergence (MACD).
    //
    // MACD = 12-Period EMA - 26-Period EMA.
    // Signal = 9-Period EMA of MACD.
    //
    // Returns MACD, signal.
    public static Pair<double[], double[]> macd(double[] close) {
        double[] ema12 = ema(12, close);
        double[] ema26 = ema(26, close);
        double[] macd = subtract(ema12, ema26);
        double[] signal = ema(9, macd);

        return Pair.of(macd, signal);
    }

}
