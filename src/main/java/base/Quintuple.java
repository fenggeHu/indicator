package base;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回5个值
 *
 * @author fengge.hu  @Date 2022/10/8
 **/
@Data
public class Quintuple<A, B, C, D, E> implements Serializable {
    public final A one;
    public final B two;
    public final C three;
    public final D four;
    public final E five;

    public Quintuple(A one, B two, C three, D four, E five) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
    }

    public static <A, B, C, D, E> Quintuple<A, B, C, D, E> of(A one, B two, C three, D four, E five) {
        return new Quintuple<>(one, two, three, four, five);
    }
}
