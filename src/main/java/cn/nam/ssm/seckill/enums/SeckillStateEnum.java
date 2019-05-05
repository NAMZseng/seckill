package cn.nam.ssm.seckill.enums;

/**
 * 封装秒杀动态常量的枚举类
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public enum SeckillStateEnum {

    /**
     * 用户秒杀成功
     */
    SUCCESS(1, "秒杀成功"),
    /**
     * 当到达秒杀结束时间或商品已经售完
     */
    END(0, "秒杀结束"),
    /**
     * 当同一用户对同一商品多次秒杀
     */
    REPEAT_KILL(-1, "重复秒杀"),
    /**
     *
     */
    INNER_ERROR(-2, "系统异常"),
    /**
     * 当用户篡改mp5加密标签时
     */
    DATA_REWRITE(3, "数据异常");

    private int state;
    private String info;

    SeckillStateEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }
}
