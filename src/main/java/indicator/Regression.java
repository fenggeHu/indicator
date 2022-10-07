package indicator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import static indicator.Helper.checkSameSize;

/**
 * @author jinfeng.hu  @Date 2022-10-07
 **/
@Slf4j
public class Regression {

    // Least square.
    //
    // y = mx + b
    // b = y-intercept
    // y = slope
    //
    // m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX)
    // b = (sumY - m * sumX) / n
    public static Pair<Double, Double> leastSquare(double[] x, double[] y) {
        checkSameSize(x, y);

        double sumX = 0, sumX2 = 0, sumY = 0, sumXY = 0;
        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumX2 += x[i] * x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
        }

        int n = x.length;
        double m = ((n * sumXY) - (sumX * sumY)) / ((n * sumX2) - (sumX * sumX));
        double b = (sumY - (m * sumX)) / n;

        return Pair.of(m, b);
    }

    // Moving least square over a period.
    //
    // y = mx + b
    // b = y-intercept
    // y = slope
    //
    // m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX)
    // b = (sumY - m * sumX) / n
    public static Pair<double[], double[]> movingLeastSquare(int period, double[] x, double[] y) {
        checkSameSize(x, y);
        double[] m = new double[x.length];
        double[] b = new double[x.length];

        double sumX = 0, sumX2 = 0, sumY = 0, sumXY = 0;
        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumX2 += x[i] * x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];

            int n = i + 1;
            if (i >= period) {
                sumX -= x[i - period];
                sumX2 -= x[i - period] * x[i - period];
                sumY -= y[i - period];
                sumXY -= x[i - period] * y[i - period];
                n = period;
            }

            m[i] = ((n * sumXY) - (sumX * sumY)) / ((n * sumX2) - (sumX * sumX));
            b[i] = (sumY - (m[i] * sumX)) / n;
        }

        return Pair.of(m, b);
    }

    // Linear regression using least square method.
    //
    // y = mx + b
    public static double[] linearRegressionUsingLeastSquare(double[] x, double[] y) {
        Pair<Double, Double> pair = leastSquare(x, y);
        double m = pair.getLeft();
        double b = pair.getRight();

        double[] r = new double[x.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (m * x[i]) + b;
        }

        return r;
    }

    // Moving linear regression using least square.
    //
    // y = mx + b
    public static double[] movingLinearRegressionUsingLeastSquare(int period, double[] x, double[] y) {
        Pair<double[], double[]> pair = movingLeastSquare(period, x, y);
        double[] m = pair.getLeft();
        double[] b = pair.getRight();

        double[] r = new double[x.length];
        log.info("{}", m);
        log.info("{}", b);

        for (int i = 0; i < r.length; i++) {
            r[i] = (m[i] * x[i]) + b[i];
        }

        return r;
    }

}
