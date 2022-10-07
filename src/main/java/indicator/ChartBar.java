package indicator;

import lombok.Data;

/**
 * Description: chart bar
 *
 * @author Jinfeng.hu  @Date 2022-10-06
 **/
@Data
public class ChartBar {
    String[] datetime;
    double[] open;
    double[] close;
    double[] High;
    double[] Low;
    double[] Volume;
}
