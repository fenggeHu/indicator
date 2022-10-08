package strategy;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: chart bar
 *
 * @author jinfeng.hu  @Date 2022-10-06
 **/
@Data
@NoArgsConstructor
public class ChartBar {
    public String[] datetime;
    public double[] open;
    public double[] close;
    public double[] high;
    public double[] low;
    // 成交量
    public long[] volume;
    // 成交额
    public long[] turnover;

    public ChartBar(int size) {
        this.datetime = new String[size];
        this.open = new double[size];
        this.close = new double[size];
        this.high = new double[size];
        this.low = new double[size];
        this.volume = new long[size];
        this.turnover = new long[size];
    }
}
