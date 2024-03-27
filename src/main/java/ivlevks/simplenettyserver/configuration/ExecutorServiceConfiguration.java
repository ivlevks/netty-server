package ivlevks.simplenettyserver.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(NettyProperties.class)
public class ExecutorServiceConfiguration {

    private final NettyProperties nettyProperties;

    @Bean
    public ThreadPoolExecutor executor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(nettyProperties.getCountThreads());
    }
}
