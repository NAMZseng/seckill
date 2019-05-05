package cn.nam.ssm.seckill.exception;

/**
 * 用户重复秒杀异常
 *
 * @author Nanrong Zeng
 * @version 1.0
 */
public class RepeatKillException extends SeckillException{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
