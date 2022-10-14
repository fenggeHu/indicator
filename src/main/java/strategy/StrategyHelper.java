package strategy;

import model.Action;
import model.ChartBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jinfeng.hu  @Date 2022/10/14
 **/
public class StrategyHelper {

    // takes one or more Strategy and returns the actions for each.
    public static List<Action[]> run(final ChartBar chartBar, Strategy... all) {
        List<Action[]> ret = new ArrayList<>(all.length);
        for (Strategy strategy : all) {
            ret.add(strategy.run(chartBar));
        }
        return ret;
    }

}
