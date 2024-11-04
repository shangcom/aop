package hello.aop.proxyvs.code;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) // CGLIB 프록시 활성화
@Import(ProxyDIAspect.class)
public class CglibProxyDITest {

    @Autowired
    MemberService memberService;

    /*
     * 현재 `spring.aop.proxy-target-class=true` 설정에 의해 CGLIB 프록시가 활성화된 상태이다.
     * CGLIB 프록시는 구현 클래스(`MemberServiceImpl`)를 상속하여 프록시를 생성하므로,
     * `MemberServiceImpl` 타입으로도 프록시 객체를 주입받을 수 있다.
     * JDK 동적 프록시와 달리 CGLIB 프록시는 구체 클래스 기반으로 프록시가 생성되므로
     * `memberServiceImpl`에 타입 불일치 없이 주입이 가능하다.
     * 결과적으로 `MemberServiceImpl` 타입으로 의존성 주입이 정상적으로 이루어지고,
     * `memberServiceImpl.hello("hello")`와 같은 메서드 호출이 가능하다.
     * 이 설정이 없다면 JDK 동적 프록시가 사용되며, JDK 동적 프록시는 인터페이스 기반이므로
     * `MemberServiceImpl` 타입 주입이 불가능하다.
     */
    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}", memberService.getClass());
        log.info("memberServiceImpl class = {}",memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }

}
