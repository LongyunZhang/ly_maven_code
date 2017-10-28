package com.demo;

import org.apache.log4j.Logger;


/**
 *
 * Log4j是目前应用最广泛的日志空间，它分如下几个日志级别，日志级别依次升高。级别高的level会屏蔽级别低的信息。

    TRACE→ DEBUG→ INFO→ WARNING→ ERROR→ FATAL→ OFF。

    比如设置INFO级别，TRACE，DEBUG就不会输出，如果设置WARNING级别，则TRACE，DEBUG，INFO都不会输出。

 */
public class Log4JDemo {

    public static Logger log = Logger.getLogger(Log4JDemo.class);

    /**
     * @功能描述： 定义一个输出日志的方法
     *
     * trace→ debug→ info→ warn→ error→ fatal→ off
     * 级别依次升高，级别高的level会屏蔽级别低的level。
     * </p>
     */
    public static void logTest()
    {
        User user = new User("xiaoming");

        log.info(user);//输出类的信息com.demo.User@1e157985，并不会输出object对象的具体信息

        log.trace("trace级别的日志输出");
        log.info("info级别的日志输出");
        log.debug("debug级别的日志输出");
        log.warn("warn级别的日志输出");
        log.error("error级别的日志输出");
        log.fatal("fatal级别的日志输出");
        try {
            System.out.println(9 / 0);
        }
        catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        logTest();
    }


}

class User{
    String name;
    public User(String name){
        this.name = name;
    }
}
