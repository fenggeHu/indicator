package strategy;

import static indicator.MomentumIndicators.*;

/**
 * @author fengge.hu  @Date 2022/10/8
 **/
public class MomentumStrategies {

    // Awesome oscillator strategy function.
    public static Action[] AwesomeOscillatorStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] ao = awesomeOscillator(asset.low, asset.high);

        for (int i = 0; i < actions.length; i++) {
            if (ao[i] > 0) {
                actions[i] = Action.BUY;
            } else if (ao[i] < 0) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // RSI strategy. Sells above sell at, buys below buy at.
    public static Action[] RsiStrategy(final ChartBar asset, double sellAt, double buyAt) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] rsi = rsi(asset.close).getRight();
        for (int i = 0; i < actions.length; i++) {
            if (rsi[i] <= buyAt) {
                actions[i] = Action.BUY;
            } else if (rsi[i] >= sellAt) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Default RSI strategy function. It buys
    // below 30 and sells above 70.
    public static Action[] DefaultRsiStrategy(final ChartBar asset) {
        return RsiStrategy(asset, 70, 30);
    }

    // Make RSI strategy function.
    public static Strategy MakeRsiStrategy(double sellAt, double buyAt) {
        return asset -> RsiStrategy(asset, sellAt, buyAt);
    }

    // RSI 2 strategy. When 2-period RSI moves below 10, it is considered deeply oversold,
    // and the other way around when moves above 90.
    public static Action[] Rsi2Strategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] rsi = rsi2(asset.close).getRight();

        for (int i = 0; i < actions.length; i++) {
            if (rsi[i] < 10) {
                actions[i] = Action.BUY;
            } else if (rsi[i] > 90) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }

    // Williams R strategy function.
    public static Action[] WilliamsRStrategy(final ChartBar asset) {
        Action[] actions = new Action[asset.getDatetime().length];

        double[] wr = williamsR(asset.low, asset.high, asset.close);

        for (int i = 0; i < actions.length; i++) {
            if (wr[i] < -20) {
                actions[i] = Action.SELL;
            } else if (wr[i] > -80) {
                actions[i] = Action.BUY;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }
}
