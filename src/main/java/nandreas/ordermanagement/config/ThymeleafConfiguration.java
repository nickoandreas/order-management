package nandreas.ordermanagement.config;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.TemplateEngine;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration
{
    @Bean
    public TemplateEngine templateEngine()
    {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(this.htmlTemplateResolver());

        return templateEngine;
    }

    @Bean
    public ClassLoaderTemplateResolver htmlTemplateResolver()
    {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }
}
