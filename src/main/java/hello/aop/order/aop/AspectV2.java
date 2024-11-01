package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 포인트컷 표현식을 어드바이스와 분리하여 하나의 메서드로 작성.
 * -> PointCut Signature
 * 포인트컷 재사용 가능.
 */
@Slf4j
@Aspect
public class AspectV2 {

    /*
    주문과 관련된 모든 기능을 대상으로 하는 포인트컷을 메서드로 작성.
     */
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() { } // 포인트컷 시그니쳐


    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }
}
