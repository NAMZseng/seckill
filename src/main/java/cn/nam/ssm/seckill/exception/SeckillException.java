package cn.nam.ssm.seckill.exception;

/**
 * 秒杀系统最高层的运行异常类,处理秒杀相关的所有业务异常
 *  Mysql只支持运行期异常的回滚操作,所以需要继承RuntimeException
 * @author Nanrong Zeng
 * @version 1.0
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
