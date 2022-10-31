# indicator & strategy
指标计算来自：https://github.com/cinar/indicator
- 以首字母大写命名指标方法。（保留了大部分golang代码的命名）
- 把原golang代码转换成java代码
- 原代码的Asset改为ChartBar（为了与我其它项目保持一致）
- 使用可参考：StrategyTests.testStrategy()

# talib
- 尽可能保持与python talib一致或接近。如参数位置顺序保持一致

# example
```java
public class Demo{
    public static void main(String[] args) {
        // 使用多个复合策略
        Strategy strategy = AllStrategy.create(
                TrendStrategies::ChandeForecastOscillatorStrategy,
                TrendStrategies::MacdStrategy,
                TrendStrategies.MakeKdjStrategy(9, 3, 3));
        // 跑策略
        Action[] actions = strategy.run(chartBar);
    }
}
```