package indicator;

/**
 * @author jinfeng.hu  @Date 2022-10-07
 **/
public class Helper {

    // subtract values2 from values1.
    protected static double[] subtract(double[] values1, double[] values2) {
        double[] subtract = multiplyBy(values2, -1);
        return add(values1, subtract);
    }


    // Check values same size.
    public static void checkSameSize(double[]... values) {
        if (values.length < 2) {
            return;
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i].length != values[0].length) {
                throw new RuntimeException("not all same size");
            }
        }
    }

    // Multiply values by multipler.
    public static double[] multiplyBy(double[] values, double multiplier) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] * multiplier;
        }

        return result;
    }

    // Multiply values1 and values2.
    public static double[] multiply(double[] values1, double[] values2) {
        checkSameSize(values1, values2);

        double[] result = new double[values1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values1[i] * values2[i];
        }

        return result;
    }

    // Divide values by divider.
    protected static double[] divideBy(double[] values, double divider) {
        return multiplyBy(values, 1 / divider);
    }

    // Divide values1 by values2.
    public static double[] divide(double[] values1, double[] values2) {
        checkSameSize(values1, values2);

        double[] result = new double[values1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values1[i] / values2[i];
        }

        return result;
    }

    // Add values1 and values2.
    protected static double[] add(double[] values1, double[] values2) {
        checkSameSize(values1, values2);

        double[] result = new double[values1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values1[i] + values2[i];
        }

        return result;
    }

    // Add addition to values.
    public static double[] addBy(double[] values, double addition) {
        double[] result = new double[values.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values[i] + addition;
        }

        return result;
    }

    // Generate numbers.
    public static double[] generateNumbers(double begin, double end, double step) {
        int n = (int) Math.round((end - begin) / step);

        double[] numbers = new double[n];

        for (int i = 0; i < n; i++) {
            numbers[i] = begin + (step * i);
        }

        return numbers;
    }

    // Convets the []int64 to []float64.
    public static double[] asDouble(long[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = new Double(values[i]);
        }

        return result;
    }

    // Shift right for period and fills with value.
    public static double[] shiftRightAndFillBy(int period, double fill, double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < result.length; i++) {
            if (i < period) {
                result[i] = fill;
            } else {
                result[i] = values[i - period];
            }
        }

        return result;
    }

    // Shift right for period.
    public static double[] shiftRight(int period, double[] values) {
        return shiftRightAndFillBy(period, 0, values);
    }


    // Sqrt of given values.
    protected static double[] sqrt(double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = Math.sqrt(values[i]);
        }

        return result;
    }

    // Abs of given values.
    public static double[] abs(double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = Math.abs(values[i]);
        }

        return result;
    }

}
