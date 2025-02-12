package hello.hello_spirng.repository;

import hello.re_hello_spirng.domain.Member;
import hello.re_hello_spirng.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


public class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    // 모든 테스트가 종료될 때 저장된 데이터 삭제
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    @DisplayName("회원 등록하기")
    public void save() {
        //given
        Member member = new Member();
        member.setName("김미래");
        //when
        repository.save(member);

        //then
        Member result = repository.findById(member.getId()).get(); // Member 객체
        assertThat(result).isEqualTo(member); // isEqualTo 두 대상의 내용이 같은지 비교
    }

    @Test
    @DisplayName("이름으로 회원 검색하기")
    public void findByName() {
        //given
        Member member = new Member();
        member.setName("김미래");
        //when
        repository.save(member);
        //then
        Member result = repository.findByName("김미래").get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("모든 회원 목록 조회하기")
    public void findAll() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member member3 = new Member();
        member3.setName("spring3");
        repository.save(member3);
        //when
        List<Member> memberList = repository.findAll();
        //then
        assertThat(memberList.size()).isEqualTo(3);

    }

}
