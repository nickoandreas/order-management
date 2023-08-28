package nandreas.ordermanagement.config;

import lombok.AllArgsConstructor;
import nandreas.ordermanagement.interceptor.RateLimitInterceptor;
import nandreas.ordermanagement.resolver.UserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@AllArgsConstructor
@Configuration
public class WebConfiguration implements WebMvcConfigurer
{
    private UserArgumentResolver userArgumentResolver;

    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers)
    {
        resolvers.add(this.userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(this.rateLimitInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/assets/image/**", "/temp/**")
                .addResourceLocations("file:assets/image/product/", "file:assets/temp/");
    }
}
