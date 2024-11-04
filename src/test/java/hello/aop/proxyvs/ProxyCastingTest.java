package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class ProxyCastingTest {

    /**
     * JDK 프록시를 구체 클래스로 캐스팅하지 못함으로써 발생하는 문제?
     * 프록시가 구현한 인터페이스에 정의되지 않은, 구현 클래스에 새로벡 선언된 메서드를 호출해야 하는 경우.
     * JDK 동적 프록시는 인터페이스에 정의된 메서드만을 프록시로 감쌀 수 있기 때문에,
     * 구현 클래스에 새로 선언된 메서드는 프록시 객체로 접근할 수 없음.
     * 해당 메서드를 사용하기 위해서는 캐스팅을 해야하는데, 캐스팅이 불가능함.
     */
    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // JDK 동적 프록시

        // JDK 동적 프록시를 인터페이스로 캐스팅 -> 성공.
        MemberService proxy = (MemberService) proxyFactory.getProxy();

        // JDK 동적 프록시를 구현 클래스로 캐스팅 -> 실패(ClassCastException)
//        MemberServiceImpl castingProxyToImpl = (MemberServiceImpl) proxy;

        assertThatThrownBy(() -> {
            MemberServiceImpl castingProxyToImpl = (MemberServiceImpl) proxy;
        }).isInstanceOf(ClassCastException.class);
    }

    /**
     * CGLIB은 인터페이스 유무에 관계 없이 구현 클래스를 상속해 프록시 객체를 생성함.
     * 인터페이스가 있다면, 부모 클래스가 인터페이스의 정보를 가지고 있으므로 자식 클래스인 프록시 객체는
     * 부모의 부모인 인터페이스로 캐스팅 가능.
     * 인터페이스가 없고 부모 클래스로 캐스팅 해야한다면? 당연히 가능(업캐스팅).
     * 당연히 부모 클래스의 메서드 다 사용 가능함.
     * 그래서 애초에 프록시는 자식 클래스이니 업캐스팅이 필요 없음.
     */
    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // CGLIB 프록시

        // CGLIB 프록시를 인터페이스로 캐스팅 -> 성공.
        MemberService proxy = (MemberService) proxyFactory.getProxy();

        // CGLIB 프록시를 구현 클래스로 캐스팅 -> 성공
        MemberServiceImpl castingProxyToImpl = (MemberServiceImpl) proxy;
        log.info("castingProxyToImpl = {}", castingProxyToImpl.getClass());
    }

}
