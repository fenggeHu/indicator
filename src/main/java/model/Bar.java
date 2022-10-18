package model;

import lombok.Data;

/**
 * @author jinfeng.hu  @Date 2022/10/10
 **/
@Data
public class Bar {
    public String datetime;
    public double open;
    public double close;
    public double high;
    public double low;
    public long volume;

    public String title() {
        return "datetime,open,high,low,close,volume";
    }
}
