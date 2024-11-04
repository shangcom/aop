package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2_1 {

    private final ObjectProvider<CallServiceV2_1> callServiceProvider;

    public CallServiceV2_1(ObjectProvider<CallServiceV2_1> callServiceProvider) {
        this.callServiceProvider = callServiceProvider;
    }


    public void external() {
        log.info("call external");
        CallServiceV2_1 callServiceV2_1 = callServiceProvider.getObject();
        callServiceV2_1.internal(); // target 객체가 아닌 프록시 객체
    }

    public void internal() {
        log.info("call internal");
    }

}
