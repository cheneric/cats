package chen.eric.cats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class Application {
	private static final Log log = LogFactory.getLog(Application.class);

	@Bean
	public Executor taskExecutor(
		@Value("${cats.taskExecutor.corePoolSize}") int taskExecutorCorePoolSize,
		@Value("${cats.taskExecutor.maxPoolSize}") int taskExecutorMaxPoolSize,
		@Value("${cats.taskExecutor.queueCapacity}") int taskExecutorQueueCapacity)
	{
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(taskExecutorCorePoolSize);
		executor.setMaxPoolSize(taskExecutorMaxPoolSize);
		executor.setQueueCapacity(taskExecutorQueueCapacity);
		executor.setThreadNamePrefix("Cats-");
		executor.initialize();
		return executor;
	}

	public static void main(String[] args) {
		final ApplicationContext context = SpringApplication.run(Application.class, args);
		log.debug("Let's inspect the beans provided by Spring Boot:");
		final String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (final String beanName : beanNames) {
			log.trace(beanName);
		}
	}
}
