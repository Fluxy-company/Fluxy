package school.sptech.iefcbackend.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    private static final String[] SWAGGER_ROUTES = {
            "/v3/api-docs", "/v3/api-docs/**",
            "/swagger-ui/**", "/swagger-ui.html",
            "/swagger-resources/**", "/webjars/**"
    };

    private static final String[] PUBLIC_GET_ROUTES = {
            "/api/v1/usuarios/**",
            "/api/v1/empresas/**",
            "/api/v1/projetos/**",
            "/api/v1/projetos/nome/**",
            "/api/v1/projetos/dataInicio/**",
            "/api/v1/projetos/status/**"
    };

    private static final String[] PUBLIC_WRITE_ROUTES = {
            "/api/v1/usuarios/**",
            "/api/v1/empresas/**",
            "/api/v1/projetos/**",
            "/api/v1/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_WRITE_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.PUT, PUBLIC_WRITE_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_WRITE_ROUTES).permitAll()

                        .anyRequest().authenticated())
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();

        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
