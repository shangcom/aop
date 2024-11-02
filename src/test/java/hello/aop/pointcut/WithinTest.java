package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * within은 타입(경로)가 정확히 일치해야만 매칭된다.
 * 부모 타입 클래스, 인터페이스로 자식의 메서드에 매칭 못한다.
 */
@Slf4j
public class WithinTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.aop.member.*Service*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(hello.aop..*)");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
    within은 표현식에서 부모 타입으로 자식 타입 매칭 못함.
    인터페이스 지정하면 아무런 매칭도 안됨
    부모 클래스로 지정하면 부모 클래스 자신의 메서드에는 적용되나 자식 클래스의 메서드에는 매칭 안됨.
     */
    @Test
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(hello.aop.member.MemberService)"); // MemberService 인터페이스
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    @DisplayName("execution은 타입 기반, 인터페이스 선정 가능.")
    void executionSuperTypeTrue() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); // MemberService 인터페이스
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();

    }
}
