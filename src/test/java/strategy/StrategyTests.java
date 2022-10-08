package strategy;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

/**
 * @author fengge.hu  @Date 2022/10/8
 **/
public class StrategyTests {

    @Test
    public void testAllStrategy() {
        Strategy strategy = AllStrategy.create(
                TrendStrategies::chandeForecastOscillatorStrategy,
                TrendStrategies::macdStrategy,
                TrendStrategies.makeKdjStrategy(9,3,3));

        ChartBar chartBar = new ChartBar();
        int max = 30;
        chartBar.datetime = new String[max];
        chartBar.open = new double[max];
        chartBar.close = new double[max];
        chartBar.high = new double[max];
        chartBar.low = new double[max];
        chartBar.volume = new long[max];
        for (int i = 0; i < max; i++) {
            chartBar.datetime[i] = "2022-01-" + String.format("%02d", i + 1);
            chartBar.open[i] = RandomUtils.nextDouble(100, 120);
            chartBar.high[i] = chartBar.open[i] + 10;
            chartBar.low[i] = chartBar.open[i] - 5;
            chartBar.close[i] = RandomUtils.nextDouble(chartBar.low[i], chartBar.high[i]);
            chartBar.volume[i] = RandomUtils.nextLong(100000, 3000000);
        }

        Action[] actions = strategy.run(chartBar);
        System.out.println(actions);
    }
}
