package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImpTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, multiplicationResultAttemptRepository, userRepository);
    }

    @Test
    public void createRandomMultiplicationTest() {
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("herbtea");
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(userRepository.findByAlias("herbtea")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(resultAttempt);

        //assert
        assertThat(attemptResult).isTrue();
        verify(multiplicationResultAttemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest(){
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("herbtea");
        MultiplicationResultAttempt resultAttempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        given(userRepository.findByAlias("herbtea")).willReturn(Optional.empty());

        //when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(resultAttempt);

        //assert
        assertThat(attemptResult).isFalse();
        verify(multiplicationResultAttemptRepository).save(resultAttempt);
    }

    @Test
    public void retrieveStatsTest() {
        //given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("herbtea");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> lastesAttemps = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("herbtea")).willReturn(Optional.empty());
        given(multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc("herbtea")).willReturn(lastesAttemps);

        //when
        List<MultiplicationResultAttempt> lastestAttemptResult = multiplicationServiceImpl.getStatsForUser("herbtea");

        //then
        assertThat(lastestAttemptResult).isEqualTo(lastesAttemps);
    }
}
