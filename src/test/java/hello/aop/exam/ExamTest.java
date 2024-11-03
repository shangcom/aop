package hello.aop.exam;

import hello.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(TraceAspect.class)
class ExamTest {

    @Autowired
    ExamService examService;


    @Test
    void request() {
        // Arrange
        // TODO: Initialize test data
        for (int i = 0; i < 5; i++) {
            log.info("client request count = {}", i + 1);
            examService.request("data" + i);
        }

        // Act
        // TODO: Call the method to be tested

        // Assert
        // TODO: Verify the results
    }
}