package talib;

/**
 * 重写indicator，调整参数位置和返回值
 *
 * @author jinfeng.hu  @Date 2022/10/31
 **/
public class Trend {

    // Simple Moving Average (SMA).
    public static Double[] Sma(double[] values, int period) {
        Double[] result = new Double[values.length];
        double sum = 0.00;

        for (int i = 0; i < values.length; i++) {
            sum += values[i];

            if (i >= period) {
                sum -= values[i - period];
            }
            if (i >= period - 1) {
                result[i] = sum / period;
            }
        }

        return result;
    }

}
