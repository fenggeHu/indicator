package indicator;

/**
 * 趋势方向
 * @author jinfeng.hu  @Date 2022-10-07
 **/
public enum TrendEnum {
    Falling(-1), Rising(1);

    private int value;

    public int value() {
        return value;
    }

    TrendEnum(int i) {
        this.value = i;
    }
}
