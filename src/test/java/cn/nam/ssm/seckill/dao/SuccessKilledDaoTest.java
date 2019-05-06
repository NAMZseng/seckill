package cn.nam.ssm.seckill.dao;

import cn.nam.ssm.seckill.entiy.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 秒杀明细表操作单元测试
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testInsertSuccessKilled() {

        long seckillId = 1001L;
        long userPhone = 13476191877L;
        int insertCount = successKilledDao
                .insertSuccessKilled(seckillId, userPhone);
        System.out.println("insertCount=" + insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill() {
        long seckillId = 1001L;
        long userPhone = 13476191877L;
        SuccessKilled successKilled = successKilledDao
                .queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());

    }


}