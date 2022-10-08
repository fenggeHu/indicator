package strategy;

import indicator.Regression;
import org.apache.commons.lang3.tuple.Pair;
import strategy.Action;
import strategy.ChartBar;

import static indicator.Helper.*;
import static indicator.TrendIndicators.macd;

/**
 * @author fengge.hu  @Date 2022/10/8
 **/
public class TrendStrategies {

    // Chande forecast oscillator strategy.
    public static Action[] chandeForecastOscillatorStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] cfo = chandeForecastOscillator(asset.getClose());
        for (int i = 0; i < actions.length; i++) {
            if (cfo[i] < 0) {
                actions[i] = Action.BUY;
            } else if (cfo[i] > 0) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // MACD strategy.
    public static Action[] macdStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];
        Pair<double[], double[]> pair = macd(asset.close);
        double[] macd = pair.getLeft();
        double[] signal = pair.getRight();

        for (int i = 0; i < actions.length; i++) {
            if (macd[i] > signal[i]) {
                actions[i] = Action.BUY;
            } else if (macd[i] < signal[i]) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // The Chande Forecast Oscillator developed by Tushar Chande The Forecast
    // Oscillator plots the percentage difference between the closing price and
    // the n-period linear regression forecasted price. The oscillator is above
    // zero when the forecast price is greater than the closing price and less
    // than zero if it is below.
    //
    // R = Linreg(Closing)
    // CFO = ((Closing - R) / Closing) * 100
    //
    // Returns cfo.
    public static double[] chandeForecastOscillator(double[] closing) {
        double[] x = generateNumbers(0, closing.length, 1);
        double[] r = Regression.linearRegressionUsingLeastSquare(x, closing);

        double[] cfo = multiplyBy(divide(subtract(closing, r), closing), 100);

        return cfo;
    }
}
