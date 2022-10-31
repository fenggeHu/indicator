package talib;

/**
 * @author jinfeng.hu  @Date 2022/10/31
 **/
public class Trend {

    // 重写 - 并调换参数位置
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
