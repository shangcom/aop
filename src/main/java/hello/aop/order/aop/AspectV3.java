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
public class AspectV3 {

    /*
    주문과 관련된 모든 기능을 대상으로 하는 포인트컷을 메서드로 작성.
     */
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {
    } // 포인트컷 시그니쳐

    @Pointcut("execution(* *..*Service.*(..))")
    private void allService() {
    }


    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }

    // hello.aop.order 패키지와 하위 패키지 이면서(&&) 클래스 이름 패턴이 *Service
    @Around("allOrder() && allService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("리소스 릴리즈");
        }
    }
}
