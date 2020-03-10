package com.xuwen.demo.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ${DESCRIPTION}
 *
 * @author XuWen
 * @created 2018-04-14 16:48.
 */
@Component
public class AsyncLog {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Async
    public void addLog(){
    }

}
