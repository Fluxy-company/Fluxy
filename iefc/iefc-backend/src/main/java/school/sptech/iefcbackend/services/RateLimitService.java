package school.sptech.iefcbackend.services;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private final LettuceBasedProxyManager<String> proxyManager;

    public RateLimitService(LettuceBasedProxyManager <String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Bucket resolveBucket(String key){
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(io.github.bucket4j.Bandwidth.simple(10, java.time.Duration.ofMinutes(1)))
                .build();

        return proxyManager.getProxy(key, () -> configuration);
    }


}