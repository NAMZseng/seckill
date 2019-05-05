package cn.nam.ssm.seckill.service;

import cn.nam.ssm.seckill.dto.Exposer;
import cn.nam.ssm.seckill.dto.SeckillExecution;
import cn.nam.ssm.seckill.entiy.Seckill;
import cn.nam.ssm.seckill.exception.RepeatKillException;
import cn.nam.ssm.seckill.exception.SeckillCloseException;
import cn.nam.ssm.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口:站在使用者(程序员)的角度设计接口
 * 三个方面:
 *  1.方法定义粒度，方法定义的要非常清楚
 *  2.参数，要越简练越好
 *  3.返回类型(return 类型一定要友好/或者return异常，我们允许的异常)
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public interface SeckillService {

    /**
     * 查询全部的秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     *
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作，有可能失败，有可能成功，根据情况抛出自定义的异常
     *
     * @param seckillId
     * @param userPhone
     * @param md5 秒杀入口url的加密密文
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;
}
