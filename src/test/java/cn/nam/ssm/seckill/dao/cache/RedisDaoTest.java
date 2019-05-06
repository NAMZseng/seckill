package cn.nam.ssm.seckill.dao.cache;

import cn.nam.ssm.seckill.dao.SeckillDao;
import cn.nam.ssm.seckill.entiy.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * RedisDao测试类
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {

    private long seckillId = 1001;
    @Resource
    private RedisDao redisDao;
    @Resource
    private SeckillDao seckillDao;

    /**
     * 测试ReisDao类的putSeckill和putSeckill方法
     */
    @Test
    public void testSeckill() {
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // 若redeis缓存没有，则从数据库中获取
            seckill = seckillDao.queryById(seckillId);

            if (seckill != null) {
                // 当数据库存在id对应的商品，则存入redis缓存
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill = redisDao.getSeckill(seckillId);
            }
        }
        System.out.println(seckill);
    }
}