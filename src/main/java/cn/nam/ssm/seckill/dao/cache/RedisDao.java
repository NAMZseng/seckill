package cn.nam.ssm.seckill.dao.cache;

import cn.nam.ssm.seckill.entiy.Seckill;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Radis缓存数据访问DAO
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class RedisDao {

    private final JedisPool jedisPool;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);

    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    /**
     * 从redis缓存中获取seckillId对象
     * 使用protostuff工具进行自定义反序列化
     *
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(long seckillId) {

        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                // redis并没有实现内部序列化操作
                // get-> byte[] -> 反序列化 ->Object(Seckill)
                // 采用自定义序列化 protostuff
                byte[] bytes = jedis.get(key.getBytes());
                //缓存中获取到bytes
                if (bytes != null) {
                    // 此时的seckill还仍然为空对象
                    Seckill seckill = schema.newMessage();
                    // 此时seckill将被反序列化
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将seckill对象写入缓存
     * 通过protostuff工具进行自定义序列化
     *
     * @param seckill
     * @return 写入成功返回OK, 否则返回错误信息
     */
    public String putSeckill(Seckill seckill) {
        // set Object(Seckill) -> 序列化 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                // 使用protostuff工具序列化seckill对象
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 设置超时缓存为1小时
                int timeout = 60 * 60;
                String result = jedis.setex(key.getBytes(), timeout, bytes);

                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
