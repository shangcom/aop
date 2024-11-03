package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/*
1. @target과 @within은 특정 애노테이션을 기준으로 타겟팅할 수 있도록 하는 포인트컷.
@target(특정 애노테이션의 전체 경로)
@within(특정 애노테이션의 전체 경로)

2. Spring AOP에서 @target과 @within 포인트컷을 사용하여 상속 관계에서의 AOP 적용 여부를 테스트.
상속 관계에서 부모 클래스와 자식 클래스의 메서드가 각각 @target과 @within 포인트컷에 따라 AOP 적용을 받는지 확인.
- 자식 클래스(`Child`)의 메서드 `childMethod()`와 부모 클래스(`Parent`)의 메서드 `parentMethod()`가
  AOP 적용을 받는지 여부를 확인함.
 */
@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
public class AtTargetAtWithinTest {

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child Proxy = {}", child.getClass()); // child 프록시의 실제 클래스 타입 로그 출력
        child.childMethod(); // 자식 클래스의 메서드 호출, @target 및 @within 포인트컷 매칭 여부 확인
        child.parentMethod(); // 부모 클래스의 메서드 호출, @target 포인트컷만 매칭 예상
    }

    static class Config {
        @Bean
        public Parent parent() {
            return new Parent();
        }

        @Bean
        public Child child() {
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }

    static class Parent {
        public void parentMethod() {
        }
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {
        }
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {


        /*
        @target 포인트컷.
        - 실제 실행 객체의 타입을 기준으로, 특정 애노테이션이 달린 클래스의 모든 메서드를 대상으로 함.
        - @target(hello.aop.member.annotation.ClassAop):
          프록시가 `@ClassAop` 애노테이션이 달린 클래스(자식 클래스 `Child`)로 생성된 경우에만 포인트컷 매칭
        - 결과: `Child` 클래스의 인스턴스가 부모 클래스 `Parent`의 `parentMethod()`를 호출해도 적용
          이유: `@target`이 프록시의 타입을 기준으로 작동하기 때문
         */
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        /*
        @within 포인트컷
        - `@within(hello.aop.member.annotation.ClassAop)`:
          - 애노테이션이 선언된 클래스 내부의 메서드에만 적용
          - `Child` 클래스에 직접 정의된 메서드에만 적용, 상속된 `parentMethod()`에는 적용되지 않음
        - 상속받은 메서드에는 적용 X.
          이유: `@within`은 현재 클래스에 직접 정의된 메서드만 매칭
         */
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }
}
