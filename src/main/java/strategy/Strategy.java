package strategy;

/**
 * 策略接口
 *
 * @author jinfeng.hu  @Date 2022-10-06
 **/
public interface Strategy {
    // run strategy
    Action[] run(final ChartBar chartBar);
}
