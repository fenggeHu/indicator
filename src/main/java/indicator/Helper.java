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

    // Add values1 and values2.
    public static double[] add(double[] values1, double[] values2) {
        checkSameSize(values1, values2);

        double[] result = new double[values1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = values1[i] + values2[i];
        }

        return result;
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
}
