package hello.aop.proxyvs.code;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // JDK 동적 프록시 활성화
@Import(ProxyDIAspect.class)
public class JdkProxyDITest {

    @Autowired
    MemberService memberService;

    /*
     현재 MemberService 인터페이스 기반의 JDK 동적 프록시만 존재한다.
     MemberServiceImpl 타입의 프록시는 존재하지 않는다.
     현재 프록시는 MemberService를 구현했을뿐, MemberServiceImpl과는 무관하며, MemberServiceImpl 타입으로 인식되지 않는다.
     아래 코드에 의해 프록시가 MemberServiceImpl 대신 주입되어야 하는데, 위의 이유로 타입 불일치가 발생한다.
     UnsatisfiedDependencyException 발생!
     */
//    @Autowired
//    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}", memberService.getClass());
//        log.info("memberServiceImpl class = {}",memberServiceImpl.getClass());
//        memberServiceImpl.hello("hello");
    }
}
