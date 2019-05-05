package cn.nam.ssm.seckill.dto;

import cn.nam.ssm.seckill.entiy.SuccessKilled;
import cn.nam.ssm.seckill.enums.SeckillStateEnum;

/**
 * service层与web层的数据传输对象
 * 封装执行秒杀后的结果:是否秒杀成功
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class SeckillExecution {
    /**
     * 秒杀商品ID
     */
    private long seckillId;
    /**
     * 秒杀情况状态标识
     */
    private int state;
    /**
     * 秒杀情况状态信息
     */
    private String stateInfo;
    /**
     * 当秒杀成功时，需要传递回秒杀成功的对象
     */
    private SuccessKilled successKilled;

    /**
     * 秒杀成功,返回所有信息
     *
     * @param seckillId
     * @param stateEnum 秒杀状态枚举对象
     * @param successKilled
     */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getInfo();
        this.successKilled = successKilled;
    }

    /**
     * 秒杀失败，仅返回状态信息
     *
     * @param seckillId
     * @param stateEnum
     */
    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getInfo();
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
