package strategy;

import model.Action;
import model.ChartBar;

/**
 * The SeparateStrategies function takes a buy strategy and a sell strategy.
 * <p>
 * It returns a BUY action if the buy strategy returns a BUY action and
 * the the sell strategy returns a HOLD action.
 * <p>
 * It returns a SELL action if the sell strategy returns a SELL action
 * and the buy strategy returns a HOLD action.
 * <p>
 * It returns HOLD otherwise.
 *
 * @author jinfeng.hu  @Date 2022/10/14
 **/
public class SeparateStrategy implements Strategy {
    Strategy buyStrategy;
    Strategy sellStrategy;

    public SeparateStrategy(Strategy buyStrategy, Strategy sellStrategy) {
        this.buyStrategy = buyStrategy;
        this.sellStrategy = sellStrategy;
    }

    @Override
    public Action[] run(ChartBar asset) {
        if (null == buyStrategy || null == sellStrategy) {
            return null;
        }
        Action[] actions = new Action[asset.getDatetime().length];
        Action[] buyActions = buyStrategy.run(asset);
        Action[] sellActions = sellStrategy.run(asset);

        for (int i = 0; i < actions.length; i++) {
            if (buyActions[i] == Action.BUY && sellActions[i] == Action.HOLD) {
                actions[i] = Action.BUY;
            } else if (sellActions[i] == Action.SELL && buyActions[i] == Action.HOLD) {
                actions[i] = Action.SELL;
            } else {
                actions[i] = Action.HOLD;
            }
        }

        return actions;
    }
}
