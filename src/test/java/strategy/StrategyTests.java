package strategy;

import model.Action;
import model.ChartBar;
import org.junit.Test;

import java.util.Random;

/**
 * @author jinfeng.hu  @Date 2022/10/8
 **/
public class StrategyTests {
    private static final Random RANDOM = new Random();

    public static double nextDouble(final double startInclusive, final double endExclusive) {
        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + ((endExclusive - startInclusive) * RANDOM.nextDouble());
    }

    @Test
    public void testStrategy() {
        Strategy strategy = AllStrategy.create(
                TrendStrategies::ChandeForecastOscillatorStrategy,
                TrendStrategies::MacdStrategy,
                TrendStrategies.MakeKdjStrategy(9, 3, 3));

        ChartBar chartBar = new ChartBar();
        System.out.println(chartBar.title());
        int max = 30;
        chartBar.datetime = new String[max];
        chartBar.open = new double[max];
        chartBar.close = new double[max];
        chartBar.high = new double[max];
        chartBar.low = new double[max];
        chartBar.volume = new long[max];
        for (int i = 0; i < max; i++) {
            chartBar.datetime[i] = "2022-01-" + String.format("%02d", i + 1);
            chartBar.open[i] = nextDouble(100, 120);
            chartBar.high[i] = chartBar.open[i] + 10;
            chartBar.low[i] = chartBar.open[i] - 5;
            chartBar.close[i] = nextDouble(chartBar.low[i], chartBar.high[i]);
            chartBar.volume[i] = (long) nextDouble(100000, 3000000);
            System.out.println(chartBar.row(i));
        }

        Action[] actions = strategy.run(chartBar);
        System.out.println(actions);
    }


    @Test
    public void testStrategy2() {
//        String json = "{\"datetime\":[\"2022-01-01\",\"2022-01-02\",\"2022-01-03\",\"2022-01-04\",\"2022-01-05\",\"2022-01-06\",\"2022-01-07\",\"2022-01-08\",\"2022-01-09\",\"2022-01-10\",\"2022-01-11\",\"2022-01-12\",\"2022-01-13\",\"2022-01-14\",\"2022-01-15\",\"2022-01-16\",\"2022-01-17\",\"2022-01-18\",\"2022-01-19\",\"2022-01-20\",\"2022-01-21\",\"2022-01-22\",\"2022-01-23\",\"2022-01-24\",\"2022-01-25\",\"2022-01-26\",\"2022-01-27\",\"2022-01-28\",\"2022-01-29\",\"2022-01-30\"],\"open\":[104.46296531386332,116.2304130899332,109.54454735930982,109.05510750683365,117.91192018180764,100.68174654703279,110.99923076638964,118.87920347802003,106.99040254126552,104.7231755371609,107.41080499354429,117.84283867304454,118.72904602371153,110.58166185019363,119.91472663706105,118.23073648420254,119.38863202563329,106.46490598618031,117.15449297574133,119.54950886534851,108.49510930420615,105.13627862830296,105.01662994828258,119.56609586053675,104.78709267926564,117.26646295616919,105.78029507275589,109.48295192556817,112.9326522823871,111.3141196485157],\"close\":[112.69977384959901,113.26848653452764,112.63050189019232,118.28724556458387,127.14661513662547,98.96134938153644,117.36839302285411,124.66847591514384,105.5582893001721,100.90356912329861,111.72968664776202,126.88859247079026,117.51955565532798,118.80942970154311,123.78738878167033,121.09223223454909,121.51182237207914,113.51670413187787,112.82096807128183,121.38495835234916,116.13044882092137,114.39723900182233,111.54534161556175,121.13374127910372,108.28272925836649,116.4867563585213,115.21746945217653,116.73941124293235,114.77540112077178,114.03446079856265],\"high\":[114.46296531386332,126.2304130899332,119.54454735930982,119.05510750683365,127.91192018180764,110.68174654703279,120.99923076638964,128.87920347802003,116.99040254126552,114.7231755371609,117.41080499354429,127.84283867304454,128.72904602371153,120.58166185019363,129.91472663706105,128.23073648420254,129.38863202563329,116.46490598618031,127.15449297574133,129.5495088653485,118.49510930420615,115.13627862830296,115.01662994828258,129.56609586053673,114.78709267926564,127.26646295616919,115.78029507275589,119.48295192556817,122.9326522823871,121.3141196485157],\"low\":[99.46296531386332,111.2304130899332,104.54454735930982,104.05510750683365,112.91192018180764,95.68174654703279,105.99923076638964,113.87920347802003,101.99040254126552,99.7231755371609,102.41080499354429,112.84283867304454,113.72904602371153,105.58166185019363,114.91472663706105,113.23073648420254,114.38863202563329,101.46490598618031,112.15449297574133,114.54950886534851,103.49510930420615,100.13627862830296,100.01662994828258,114.56609586053675,99.78709267926564,112.26646295616919,100.78029507275589,104.48295192556817,107.9326522823871,106.3141196485157],\"volume\":[2776372,269028,339354,2381164,687289,1756844,479292,1000183,1766866,1945031,2578813,292292,723440,2651601,2889189,2047711,2721967,938765,2784762,2855935,2691855,2008594,777053,1458007,741426,2476770,1711456,1916952,2671183,1472766]}";
//        ChartBar chartBar = gson.fromJson(json, ChartBar.class);
//
//        Strategy strategy = AllStrategy.create(
//                TrendStrategies::ChandeForecastOscillatorStrategy,
//                TrendStrategies::MacdStrategy,
//                TrendStrategies.MakeKdjStrategy(9, 3, 3));
//        Action[] actions = strategy.run(chartBar);
//        System.out.println(actions);
    }
}
