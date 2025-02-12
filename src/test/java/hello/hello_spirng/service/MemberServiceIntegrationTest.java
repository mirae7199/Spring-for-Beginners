package hello.hello_spirng.service;

import hello.re_hello_spirng.domain.Member;
import hello.re_hello_spirng.repository.MemberRepository;
import hello.re_hello_spirng.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // Given
        Member member = new Member();
        member.setName("김미래");

        // when
        Long memberId = memberService.join(member);

        // then
        Assertions.assertEquals(member.getName(), memberRepository.findById(memberId).get().getName());


    }

}
