package cn.nam.ssm.seckill.dao;

import cn.nam.ssm.seckill.entiy.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 秒杀库存表的DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface SeckillDao {

    /**
     * 减库存操作
     *
     * @param seckillId 秒杀的商品ID
     * @param killTime  用户秒杀时间
     * @return 更新成功返回1, 否则返回0（即sql操作影响的行数）
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据秒杀的商品ID查询商品信息
     *
     * @param seckillId 秒杀的商品ID
     * @return 商品对象Seckill
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     *
     * @param offset 偏移量
     * @param limit  返回记录行的最大数目
     * @return 商品对象Seckil的List集合
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
