package cn.nam.ssm.seckill.dao;

import cn.nam.ssm.seckill.entiy.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * 秒杀成功明细表DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface SuccessKilledDao {
    /**
     * 插入购买明细,可过滤用户对某一商品的重复操作
     *
     * @param seckillId 秒杀的商品ID
     * @param userPhone 用户手机号
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据秒杀商品的id查询SuccessKilled秒杀成功明细对象,该对象携带了Seckill秒杀产品对象
     *
     * @param seckillId 秒杀的商品ID
     * @param userPhone 用户手机号
     * @return SuccessKilled秒杀成功明细对象
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
