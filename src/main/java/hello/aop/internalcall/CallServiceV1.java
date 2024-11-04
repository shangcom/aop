package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    /**
     * Setter 주입 사용 이유:
     * - 생성자 주입을 사용할 경우 순환 참조 오류가 발생함.
     * - 생성자 주입은 빈을 생성하기 전에 의존성을 주입하려고 시도함.
     *   따라서, 자기 자신을 주입받아야 하는 상황에서는 해당 빈이 아직 생성되지 않아 오류가 발생함.
     * - 반면, 필드 주입이나 Setter 주입은 빈을 먼저 생성한 후에 의존성을 주입하기 때문에 순환 참조가 발생하지 않음.
     * - 여기서는 Setter 주입을 통해 자기 자신을 주입받아 프록시 객체가 적용될 수 있도록 함.
     * 같은 이유로 setter 메서드에는 프록시 적용되지 않는다. 내부 호출이기 때문.
     */
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        log.info("callServiceV1 setter = {}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // target 객체가 아닌 프록시 객체
    }

    public void internal() {
        log.info("call internal");
    }
}
