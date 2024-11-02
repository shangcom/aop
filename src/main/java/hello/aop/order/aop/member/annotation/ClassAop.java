package hello.aop.order.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // ClassAop 애노테이션의 적용 대상 : 타입(클래스, 인터페이스, 열거형 등) (메서드, 필드 불가)
@Retention(RetentionPolicy.RUNTIME) // 유지 정책 : 런타임까지 유지. 런타임에 리플렉션을 통해 애노테이션 정보 접근 가능.
public @interface ClassAop {
}
