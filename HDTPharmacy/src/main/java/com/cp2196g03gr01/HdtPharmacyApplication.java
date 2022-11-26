package com.cp2196g03gr01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.github.messenger4j.Messenger;

@EnableAsync
@SpringBootApplication
public class HdtPharmacyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HdtPharmacyApplication.class, args);
	}

	@Bean
	public Messenger messenger(@Value("${messenger4j.pageAccessToken}") String pageAccessToken,
			@Value("${messenger4j.appSecret}") final String appSecret,
			@Value("${messenger4j.verifyToken}") final String verifyToken) {
		return Messenger.create(pageAccessToken, appSecret, verifyToken);
	}

	@Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
