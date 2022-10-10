package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jinfeng.hu  @Date 2022/10/10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateAction {
    String datetime;
    double price;
    Action action;
}
