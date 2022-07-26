package chen.eric.cats;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.Executor;

import static java.util.Optional.*;

@SpringBootApplication
@EnableAsync
public class Application {
	private Log log;

	protected void setLog(Log log) {
		this.log = log;
	}

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

	@Bean
	@Scope("prototype")
	// https://medium.com/simars/inject-loggers-in-spring-java-or-kotlin-87162d02d068
	public Log provideLog(final InjectionPoint injectionPoint) {
		return LogFactory.getLog(
			of(injectionPoint.getMethodParameter())
				.<Class>map(MethodParameter::getContainingClass)
				.orElseGet(() ->
					ofNullable(injectionPoint.getField())
						.map(Field::getDeclaringClass)
						.orElseThrow(IllegalArgumentException::new)
			)
		);
	}

	public static void main(String[] args) {
		final ApplicationContext context = SpringApplication.run(Application.class, args);
		final Log log = LogFactory.getLog(Application.class);
		log.debug("Let's inspect the beans provided by Spring Boot:");
		final String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (final String beanName : beanNames) {
			log.trace(beanName);
		}
	}
}
