package strategy;

import static indicator.TrendIndicators.ema;
import static indicator.VolumeIndicators.*;

/**
 * @author fengge.hu  @Date 2022/10/8
 **/
public class VolumeStrategies {

    // Money flow index strategy.
    public static Action[] MoneyFlowIndexStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] moneyFlowIndex = DefaultMoneyFlowIndex(
                asset.high,
                asset.low,
                asset.close,
                asset.volume);

        for (int i = 0; i < actions.length; i++) {
            if (moneyFlowIndex[i] >= 80) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.BUY;
            }
        }

        return actions;
    }

    // Force index strategy public static Action[]tion.
    public static Action[] ForceIndexStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] forceIndex = defaultForceIndex(asset.close, asset.volume);

        for (int i = 0; i < actions.length; i++) {
            if (forceIndex[i] > 0) {
                actions[i] = Action.BUY;
            } else if (forceIndex[i] < 0) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Ease of movement strategy.
    public static Action[] EaseOfMovementStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] emv = defaultEaseOfMovement(asset.high, asset.low, asset.volume);

        for (int i = 0; i < actions.length; i++) {
            if (emv[i] > 0) {
                actions[i] = Action.BUY;
            } else if (emv[i] < 0) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Volume weighted average price strategy public static Action[]tion.
    public static Action[] VolumeWeightedAveragePriceStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] vwap = defaultVolumeWeightedAveragePrice(asset.close, asset.volume);

        for (int i = 0; i < actions.length; i++) {
            if (vwap[i] > asset.close[i]) {
                actions[i] = Action.BUY;
            } else if (vwap[i] < asset.close[i]) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Negative volume index strategy.
    public static Action[] NegativeVolumeIndexStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] nvi = negativeVolumeIndex(asset.close, asset.volume);
        double[] nvi255 = ema(255, nvi);

        for (int i = 0; i < actions.length; i++) {
            if (nvi[i] < nvi255[i]) {
                actions[i] = Action.BUY;
            } else if (nvi[i] > nvi255[i]) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Chaikin money flow strategy.
    public static Action[] ChaikinMoneyFlowStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] cmf = chaikinMoneyFlow(
                asset.high,
                asset.low,
                asset.close,
                asset.volume);

        for (int i = 0; i < actions.length; i++) {
            if (cmf[i] < 0) {
                actions[i] = Action.BUY;
            } else if (cmf[i] > 0) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

}
