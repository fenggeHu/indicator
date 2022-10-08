package indicator;

/**
 * @author jinfeng.hu  @Date 2022-10-07
 **/
public class Helper {

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
    public static double[] divideBy(double[] values, double divider) {
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
    public static double[] add(double[] values1, double[] values2) {
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

    // subtract values2 from values1.
    public static double[] subtract(double[] values1, double[] values2) {
        double[] subtract = multiplyBy(values2, -1);
        return add(values1, subtract);
    }

    // Difference between current and before values.
    public static double[] diff(double[] values, int before) {
        return subtract(values, shiftRight(before, values));
    }

    // Percent difference between current and before values.
    public static double[] percentDiff(double[] values, int before) {
        double[] result = new double[values.length];

        for (int i = before; i < values.length; i++) {
            result[i] = (values[i] - values[i - before]) / values[i - before];
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

    // Round value to digits.
    public static double roundDigits(double value, int digits) {
        double n = Math.pow(10, digits);

        return Math.round(value * n) / n;
    }

    // Round values to digits.
    public static double[] roundDigitsAll(double[] values, int digits) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = roundDigits(values[i], digits);
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

        for (int i = 0; i < result.length; i++) {
            result[i] = new Double(values[i]);
        }

        return result;
    }

    // Calculate power of base with exponent.
    public static double[] pow(double[] base, double exponent) {
        double[] result = new double[base.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = Math.pow(base[i], exponent);
        }

        return result;
    }

    // Extact sign.
    public static double[] extractSign(double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < result.length; i++) {
            if (values[i] >= 0) {
                result[i] = 1;
            } else {
                result[i] = -1;
            }
        }

        return result;
    }

    // Keep positives.
    public static double[] keepPositives(double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                result[i] = values[i];
            } else {
                result[i] = 0;
            }
        }

        return result;
    }

    // Keep negatives.
    public static double[] keepNegatives(double[] values) {
        double[] result = new double[values.length];

        for (int i = 0; i < values.length; i++) {
            if (values[i] < 0) {
                result[i] = values[i];
            } else {
                result[i] = 0;
            }
        }

        return result;
    }

    // Sqrt of given values.
    public static double[] sqrt(double[] values) {
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
