package model;

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
    public double[] high;
    public double[] low;
    public double[] close;
    public long[] volume;

    public ChartBar(int size) {
        this.datetime = new String[size];
        this.open = new double[size];
        this.high = new double[size];
        this.low = new double[size];
        this.close = new double[size];
        this.volume = new long[size];
    }

    // date,ohlcv
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getDatetime().length; i++) {
            sb.append(this.getDatetime()[i]).append(",")
                    .append(this.getOpen()[i]).append(",")
                    .append(this.getHigh()[i]).append(",")
                    .append(this.getLow()[i]).append(",")
                    .append(this.getClose()[i]).append(",")
                    .append(this.getVolume()[i]).append("\n");
        }
        return sb.toString();
    }
}
