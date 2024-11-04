package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callServiceV0;

    /**
     * CallLogAspect에서 internalcall과 하위 모든 패키지에 포함된 메서드에 프록시 적용했음.
     * callServiceV0의 external()에는 작동함.
     * 그러나 external() 내부에서 호출한 internal()에는 적용되지 않음
     * internal()을 호출하면 적용되지만, external()에서 내부호출한 internal()은 프록시가 적용되지 않는다.
     * 이유 : target 객체에서 내부 호출은 this.가 생략되어 있음.
     * 프록시 객체의 internal()이 아니라 target 객체 자신의 internal()을 호출함.
     */
    @Test
    void external() {

        log.info("target = {}", callServiceV0.getClass()); // 현재 callServiceV0는 프록시 걸려있음.


        callServiceV0.external();
        callServiceV0.internal();
    }

}