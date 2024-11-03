package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy = {}", memberService.getClass());
        memberService.hello("helloA"); // "helloA" 문자열이 memberService의 hello 메서드에 전달됨
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        // logArgs1: ProceedingJoinPoint를 사용하여 호출된 메서드의 첫 번째 매개변수에 접근
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0]; // 매개변수 배열을 가져오고 첫 번째 요소를 추출
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed(); // 원래 메서드 호출
        }

        // logArgs2: args() 포인트컷 표현식을 사용하여 매개변수를 처리
        // args(arg,..): 포인트컷 표현식에서 arg는 호출된 메서드의 매개변수 중 첫 번째 매개변수를 나타내며,
        // 해당 매개변수를 'arg'라는 이름으로 어드바이스의 인자로 받음. 모든 타입의 매개변수를 처리 가능.
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg); // 매개변수 가져옴
            return joinPoint.proceed(); // 원래 메서드 호출
        }

        // logArgs3: @Before 어드바이스로 매개변수에 접근하며, String 타입의 매개변수만 처리
        // args(arg,..): 이 포인트컷 표현식은 매개변수 중 첫 번째 매개변수를 arg라는 이름으로 사용하고,
        // logArgs3 메서드의 매개변수는 String 타입으로 지정되어, String 타입의 매개변수만 처리하게 됨.
        @Before("allMember() && args(arg,..)")// arg는 String 타입의 매개변수를 나타냄
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg={}", arg); // 매개변수 가져옴
        }

        // thisArgs: 'this' 포인트컷 지시자를 사용해 프록시 객체를 가져옴
        // this(obj): obj는 현재 AOP 프록시 객체를 나타내며, MemberService 타입으로 강제 형변환되어 사용됨.
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[thisArgs]{}, obj={}", joinPoint.getSignature(), obj.getClass()); // 현재 객체의 인스턴스 가져옴
        }

        // targetArgs: 'target' 포인트컷 지시자를 사용해 실제 대상 객체를 가져옴
        // target(obj): obj는 실제 구현체를 나타내며, MemberService 타입으로 강제 형변환되어 사용됨.
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[targetArgs]{}, obj={}", joinPoint.getSignature(), obj.getClass());
            // 현재 메서드의 시그니처와 실제 대상 객체의 클래스를 로그에 기록
        }

        // atTarget: @target 지시자를 사용해 애노테이션 정보를 가져옴
        // @target(annotation): annotation은 클래스에 적용된 애노테이션을 나타내며,
        // 해당 애노테이션의 타입을 인자로 사용하여 이 어드바이스가 호출됨.
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[atTarget]{}, obj={}", joinPoint.getSignature(), annotation);
            // 현재 메서드의 시그니처와 클래스 애노테이션 정보를 로그에 기록
        }

        // atWithin: @within 지시자를 사용해 클래스 내부의 애노테이션 정보를 가져옴
        // @within(annotation): annotation은 해당 메서드가 속한 클래스의 애노테이션을 나타내며,
        // 이 애노테이션의 타입을 인자로 사용하여 이 어드바이스가 호출됨.
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            // 클래스 내부의 애노테이션 정보 가져옴
            log.info("[atWithin]{}, obj={}", joinPoint.getSignature(), annotation);
            // 현재 메서드의 시그니처와 클래스 내부 애노테이션 정보를 로그에 기록
        }

        // atAnnotation: @annotation 지시자를 사용해 메서드의 애노테이션 정보를 가져옴
        // @annotation(annotation): annotation은 메서드에 적용된 애노테이션을 나타내며,
        // 이 애노테이션의 타입을 인자로 사용하여 이 어드바이스가 호출됨.
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            // 메서드의 애노테이션 값 가져옴
            log.info("[atAnnotation]{}, annotationValue={}", joinPoint.getSignature(), annotation.value());
            // 현재 메서드의 시그니처와 메서드 애노테이션 값을 로그에 기록
        }
    }
}
