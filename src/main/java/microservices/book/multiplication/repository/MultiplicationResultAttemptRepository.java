package microservices.book.multiplication.repository;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 답안을 저장하고 조회하기 위한 인터페이스
 */
public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {

    List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);

}
