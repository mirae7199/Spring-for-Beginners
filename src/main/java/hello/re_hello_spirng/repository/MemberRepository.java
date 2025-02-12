package hello.re_hello_spirng.repository;

import hello.re_hello_spirng.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    // Optional<T> 클래스를 사용해 NullPointException를 방지할 수 있다.
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
