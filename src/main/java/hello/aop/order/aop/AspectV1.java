package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    /**
     * hello.aop.order 패키지의 모든 메서드에 대해 적용.
     * '@Around' 대상 메서드의 실행 여부와 실행 시점을 완전히 제어.
     * @param joinPoint :  @Around 애드바이스 내에서만 사용.
     * 조인 포인트에 대한 정보(메서드 이름, 매개변수 등)를 제공.
     * 대상 메서드를 직접 실행하는 proceed() 메서드를 제공.
     */
    @Around("execution(* hello.aop.order..*(..))") // 포인트컷
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // 어드바이스. 대상 메서드의 시그니처 정보를 로그로 출력.
        return joinPoint.proceed(); // Spring AOP에서는 MethodInvocation.proceed()
    }
}
