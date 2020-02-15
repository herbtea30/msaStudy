package microservices.book.multiplication.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 사용자 정보를 저장하는 클래스
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class User {
    private final String alias;

    //JSon (역)직렬화를 위한 빈 생성자
    protected User() {
        alias = null;
    }
}
