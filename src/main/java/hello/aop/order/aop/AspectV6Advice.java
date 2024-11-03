package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    /**
     * 반드시 joinPoint.proceed()를 호출해야 한다.
     * 안하면 뒤를 이어 실행되어야할 체인이 전혀 실행되지 않아 멈춘다.
     * 따라서 @Around 뿐만 아니라, 다른 종류이 advice들이 필요하다.
     */
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // @Before 조인 포인트 실행 이전
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            // @AfterReturning  조인 포인트 정상 완료 후
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
            // @AfterThrowing 대상 메서드가 예외를 던진 경우. 어드바이스 내부에서 추가한 로직은 대상 아님.
        } catch (Exception e) {
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
            // @After 조인 포인트의 정상/예외 여부 관계없이 실행
        } finally {
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    /**
     * '@Around'에서는 ProceedingJoinPoint 사용.
     * 나머지 어드바이스는 @param joinPoint 사용.
     * proceed() 안해도 어드바이스 어노테이션으로 지정한 때에 자동으로 다음 target 호출.
     * JointPoint에는 proceed() 메서드 자체가 없음.
     */
    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    /**
     * returning = "result"
     * 대상 메서드의 반환 타입이 void일지라도 returning은 지정해 줘야함.
     * returning = "result"에서 result는 단지 어드바이스에서 사용할 파라미터 이름과 매핑하기 위한 것임.
     * 원본 메서드의 실제 변수명이나 반환타입과는 관련 없다.
     * Object result로 설정했기 때문에, 원본 메서드의 반환값이 어떤 타입이든 result라는 파라미터로 어드바이스에 전달됨.
     * 만약 원본 메서드가 void라면 result로 null이 들어온다.
     *
     * @param result 타입이 대상 메서드의 return 타입과 일치해야 어드바이스가 실행된다.
     *               만약 대상 메서드가 null을 반환하는데 advice에서 Object가 아닌 특정 타입을 지정했다면
     *               advice 실행되지 않는다(타입 불일치).
     *               null은 객체로 간주되지만, 컴파일러가 특정 타입으로 인식할 수 없기 때문에
     *               String이나 Integer 같은 특정 타입으로는 직접 매핑되지 않음.
     */
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return = {}", joinPoint.getSignature(), result);
    }

    /**
     * 포인트컷 표현식에서 지정한 대상 메서드의 반환타입과 무관하게,
     * 대상 메서드 실행 중 발생하는 예외 중 어떤 타입의 예외를 받을 지를 매개변수로 지정할 수 있다.
     *
     * @param ex 여기에서 지정한 타입의 예외가 대상 메서드에서 발생했을 시에만 어드바이스가 실행된다.
     */
    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message = {}", joinPoint.getSignature(), ex.getMessage());
    }

    /**
     * 리소스 해제시 사용.
     */
    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
