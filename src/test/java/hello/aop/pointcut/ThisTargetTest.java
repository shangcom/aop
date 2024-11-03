package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * application.properties에
 * spring.aop.proxy-target-class=false
 * true :  CGLIB 기반 프록시 생성 (기본값)
 * false : 클래스 기반은 CGLIB로, 인터페이스 기반은 JDK 동적 프록시로 생성.
 */
@Slf4j
// CGLIB 프록시 생성 끔. 인터페이스 기반은 JDK 동적 프록시로 생성됨.
@SpringBootTest(properties = "spring.aop.proxy-target-class=false")
@Import(ThisTargetTest.ThisTargetAspect.class)
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    /**
     * this-impl은 출력 안된다.
     */
    @Test
    void success() {
        log.info("memberService Proxy = {}",memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect {

        // this : 부모타입 허용.
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // target : 부모타입 허용.
        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // this : 부모타입 허용.
        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        // target : 부모타입 허용.
        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
