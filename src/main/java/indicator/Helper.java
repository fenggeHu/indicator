package indicator;

/**
 * @author jinfeng.hu  @Date 2022-10-07
 **/
public class Helper {

    // subtract values2 from values1.
    public static double[] subtract(double[] values1, double[] values2) {
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

}
