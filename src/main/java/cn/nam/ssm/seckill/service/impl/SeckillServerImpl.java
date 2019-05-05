package cn.nam.ssm.seckill.service.impl;

import cn.nam.ssm.seckill.dao.SeckillDao;
import cn.nam.ssm.seckill.dao.SuccessKilledDao;
import cn.nam.ssm.seckill.dto.Exposer;
import cn.nam.ssm.seckill.dto.SeckillExecution;
import cn.nam.ssm.seckill.entiy.Seckill;
import cn.nam.ssm.seckill.entiy.SuccessKilled;
import cn.nam.ssm.seckill.enums.SeckillStateEnum;
import cn.nam.ssm.seckill.exception.RepeatKillException;
import cn.nam.ssm.seckill.exception.SeckillCloseException;
import cn.nam.ssm.seckill.exception.SeckillException;
import cn.nam.ssm.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * 实现SeckillService接口，完善service层业务逻辑
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
@Service
public class SeckillServerImpl implements SeckillService {

    /**
     * slf4f日志对象
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * MD5的盐值字符串，用于混淆md5
     */
    private final String salt = "alsdkhglk07@™😀IVY";

    /**
     * 注入Service依赖
     */
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public List<Seckill> getSeckillList() {
        // 系统初始时只插入了4条秒杀商品记录
        // TODO 后期优化，动态确定秒杀商品总类型数
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        // 秒杀未开始
        if (startTime.getTime() > nowTime.getTime()
                || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(),
                    startTime.getTime(), endTime.getTime());
        }

        // 秒杀开始
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional(rollbackFor = SeckillException.class)
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        /*
         * 使用注解控制事务方法的优点:
         * 1.开发团队达成一致约定，明确标注事务方法的编程风格
         * 2.保证事务方法的执行时间尽可能短，
         *   不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
         * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
         */

        if (md5 == null || !getMd5(seckillId).equals(md5)) {
            // 秒杀数据被重写
            throw new SeckillException("seckill data rewrited");
        }

        //执行秒杀逻辑:减库存+增加购买明细

        int insetCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);

        try {
            if (insetCount <= 0) {
                // 用户重复秒杀同一商品
                throw new RepeatKillException("seckill was repeated");
            } else {
                Date nowTime = new Date();
                // 用户秒杀成功,更新秒杀明细记录表,减库存
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新库存记录，说明秒杀结束 rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
                    SuccessKilled successKilled =
                            successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,
                                    successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 将编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }
    }

    /**
     * 获取md5加密字符串
     *
     * @param seckillId
     * @return md5加密字符串
     */
    private String getMd5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
