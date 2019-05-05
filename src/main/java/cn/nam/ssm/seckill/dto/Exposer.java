package cn.nam.ssm.seckill.dto;

/**
 * service层与web层的数据传输对象
 * 暴露秒杀地址(接口）,当秒杀未开始时，返回系统时间和秒杀时间
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class Exposer {

    /**
     * 是否开启秒杀
     */
    private boolean exposed;
    /**
     * 采用md5加密措施
     */
    private String md5;
    /**
     * 秒杀商品ID
     */
    private long seckillId;
    /**
     * 系统当前时间(ms)
     */
    private long nowTime;
    /**
     * 秒杀开始时间(ms)
     */
    private long startTime;
    /**
     * 秒杀结束时间(ms)
     */
    private long endTime;

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    /**
     * 秒杀为开始，返回系统时间和秒杀时间
     *
     * @param exposed 未开启秒杀
     * @param seckillId 商品ID
     * @param nowTime 系统时间
     * @param startTime 秒杀开始时间
     * @param endTime 秒杀结束时间
     */
    public Exposer(boolean exposed, long seckillId, long nowTime, long startTime, long endTime) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.nowTime = nowTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", nowTime=" + nowTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
