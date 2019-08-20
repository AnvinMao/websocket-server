package com.codetc.chat.server;

import com.codetc.chat.server.netty.WebsocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableAsync
public class WebsocketServerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsocketServerApplication.class, args);
        WebsocketServer server = context.getBean(WebsocketServer.class);
        server.run();
    }

    @Bean
    public AsyncTaskExecutor saveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setKeepAliveSeconds(3000);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("Save-Service-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
