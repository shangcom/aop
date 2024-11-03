package hello.aop.pointcut;

import hello.aop.member.annotation.Check;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Slf4j
@SpringBootTest
@Import(AtArgsTest.AtArgsAspect.class)
public class AtArgsTest {

    @Autowired
    CheckService checkService;

    @Test
    void success() {
        checkService.processCheckData("with check");
        checkService.processData("without check");
    }

    @Configuration
    static class checkConfig {
        @Bean
        public CheckService checkService() {
            return new CheckService();
        }
    }

    @Slf4j
    @Service
    static class CheckService {

        public void processCheckData(@Check String data) {
            log.info("Processing data = {}", data);
        }

        public void processData(String data) {
            log.info("Processing data = {}", data);
        }
    }

    @Aspect
    @Slf4j
    static class AtArgsAspect {
        @Around("execution(* hello.aop.pointcut.(..)) && @args(hello.aop.member.annotation.Check)")
        Object checkAnnotationOnArgument(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@args] A method with @Check on its parameter is being called: {} ", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

}
