package cn.nam.ssm.seckill.service;

import cn.nam.ssm.seckill.dto.Exposer;
import cn.nam.ssm.seckill.dto.SeckillExecution;
import cn.nam.ssm.seckill.entiy.Seckill;
import cn.nam.ssm.seckill.exception.RepeatKillException;
import cn.nam.ssm.seckill.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service业务逻辑单元测试
 *
 * @author Nanrong Zeng
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        // 使用占位符{}，seckillList内容可以在{}内显示
        logger.info("seckillList={}", seckillList);

        // seckillList=[Seckill{
        //        seckillId=1000,
        //        name='1000元秒杀iphoneX',
        //        number=99,
        //        startTime=Wed May 01 08:00:00 CST 2019,
        //        endTime=Sun May 05 08:00:00 CST 2019,
        //        createTime=Fri May 03 00:38:54 CST 2019
        //        }
        // .....
    }

    @Test
    public void testGetById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);

        /*
        11:01:03.636 [main] DEBUG o.m.s.t.SpringManagedTransaction - JDBC Connection
                            [com.mchange.v2.c3p0.impl.NewProxyConnection@61e94def] will not be managed by Spring
        11:01:03.647 [main] DEBUG c.n.s.s.dao.SeckillDao.queryById -
                            ==>  Preparing: SELECT * FROM seckill WHERE seckill_id=?
        11:01:03.660 [main] DEBUG c.n.s.s.dao.SeckillDao.queryById -
                           ==> Parameters: 1000(Long)
        11:01:03.692 [main] DEBUG c.n.s.s.dao.SeckillDao.queryById -
                           <==      Total: 1
        11:01:03.692 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Closing non transactional SqlSession
                          [org.apache.ibatis.session.defaults.DefaultSqlSession@c5dc4a2]
        11:01:03.692 [main] INFO  c.n.s.s.service.SeckillServiceTest -
                          seckill=Seckill{
                                 seckillId=1000,
                                 name='1000元秒杀iphoneX',
                                 number=99,
                                 startTime=Wed May 01 08:00:00 CST 2019,
                                 endTime=Sun May 05 08:00:00 CST 2019,
                                 createTime=Fri May 03 00:38:54 CST 2019

        */
    }

    /**
     * 测试exportSeckillUrl以及executeSeckil方法
     *
     * @throws Exception
     */
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);

        // 秒杀开始
        if (exposer.isExposed()) {
            logger.info("startExposer={}", exposer);


            long userPhone = 12345678912L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution =
                        seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("seckillExeResult={}", seckillExecution);

            } catch (RepeatKillException e1) {
                logger.error(e1.getMessage());
            } catch (SeckillCloseException e2) {
                logger.error(e2.getMessage());
            }
        } else {
            // 秒杀未开始
            logger.info("readIngExposer={}", exposer);
        }
    }
}