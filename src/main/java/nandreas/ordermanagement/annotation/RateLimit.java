package nandreas.ordermanagement.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit
{
    int value() default 100;

    int period() default 60; //in seconds
}
