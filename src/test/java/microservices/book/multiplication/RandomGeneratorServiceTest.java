package microservices.book.multiplication;

import microservices.book.multiplication.service.RandomGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RandomGeneratorServiceTest {

    @Autowired
    private RandomGeneratorService randomGeneratorService;

    @Test
    void generateRandomFactorIsBetweenExpectedLimits() throws Exception{
        //무작위 숫자를 생성
        List<Integer> randomFactors = IntStream.range(0, 1000)
        .map(i -> randomGeneratorService.generateRandomFactor())
        .boxed()
        .collect(Collectors.toList());

        //적당히 어려운 계산을 만들기위해
        //생성한 인수가 11 ~ 99 범위에 있는지 확인
        assertThat(randomFactors)
                .containsOnlyElementsOf(IntStream.range(11, 100)
                        .boxed()
                        .collect(Collectors.toList()));
    }

}
