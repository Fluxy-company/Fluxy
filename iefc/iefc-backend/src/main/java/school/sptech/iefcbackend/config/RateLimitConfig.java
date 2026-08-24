package school.sptech.iefcbackend.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {
    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6379");
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, byte[]> bucket4jConnection(RedisClient redisClient) {
        return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
    }

    @Bean
    public LettuceBasedProxyManager<String> proxyManager(StatefulRedisConnection<String, byte[]> connection) {
        return Bucket4jLettuce
                .casBasedBuilder(connection)
                .expirationAfterWrite(ExpirationAfterWriteStrategy
                        .basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build();
    }
}