package strategy;

import model.Action;
import model.ChartBar;

/**
 * 策略接口 - 这个接口的设计适合回测，但不适合实时交易的时间序列滚动处理
 *
 * @author jinfeng.hu  @Date 2022-10-06
 **/
public interface Strategy {
    // run strategy
    Action[] run(final ChartBar chartBar);
}
