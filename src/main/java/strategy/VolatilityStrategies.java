package strategy;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import static indicator.VolatilityIndicators.bollingerBands;
import static indicator.VolatilityIndicators.projectionOscillator;

/**
 * @author fengge.hu  @Date 2022/10/8
 **/
public class VolatilityStrategies {

    // Bollinger bands strategy public static Action[]tion.
    public static Action[] BollingerBandsStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];
        Triple<double[], double[], double[]> triple = bollingerBands(asset.close);
        double[] upperBand = triple.getMiddle();
        double[] lowerBand = triple.getRight();

        for (int i = 0; i < actions.length; i++) {
            if (asset.close[i] > upperBand[i]) {
                actions[i] = Action.SELL;
            } else if (asset.close[i] < lowerBand[i]) {
                actions[i] = Action.BUY;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Projection oscillator strategy public static Action[]tion.
    public static Action[] ProjectionOscillatorStrategy(int period, int smooth, final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        Pair<double[], double[]> pair = projectionOscillator(
                period,
                smooth,
                asset.high,
                asset.low,
                asset.close);
        double[] po = pair.getLeft();
        double[] spo = pair.getRight();

        for (int i = 0; i < actions.length; i++) {
            if (po[i] > spo[i]) {
                actions[i] = Action.BUY;
            } else if (po[i] < spo[i]) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Make projection oscillator strategy.
    public static Strategy MakeProjectionOscillatorStrategy(int period, int smooth) {
        return asset -> ProjectionOscillatorStrategy(period, smooth, asset);
    }

}
