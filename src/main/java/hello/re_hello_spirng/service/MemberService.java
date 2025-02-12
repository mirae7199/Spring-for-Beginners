package hello.re_hello_spirng.service;

import hello.re_hello_spirng.domain.Member;
import hello.re_hello_spirng.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 의존관계 주입! DI
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member) {

        long start = System.currentTimeMillis();

        try {
            validateDuplicateMember(member); // 중복 회원 검증
            memberRepository.save(member);
            return member.getId();
        } finally{
            long finish = System.currentTimeMillis();
            long timeMs = finish - start; // (끝나는 시간 - 시작 시간)
            System.out.println("join " + timeMs + "ms");
        }

    }

    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        // .ifPresent() 메서드는 Optional 객체가 값을 포함하고 있을 때 특정 동작을 수행함.
        // 값이 존재할 때에만 실행하고, 값이 없을 때 아무 작업도 실행 하지 않음..
        memberRepository.findByName(member.getName()) // 여기서 이미 주어진 이름과 일치하는 Member 객체를 찾는다. 객체가 없다면 빈 Optional 객체를 반환
                .ifPresent(m -> { // 위에서 일치하는 Member 객체가 있다면 여기서 걸러진다.
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                    // IllegalStateException은 불법 상태를 나타내기 위한 예외
                    // 특정 객체가 요구되는 상태가 아닌 다른 상태에서 메서드가 호출될 때 예외 발생
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        long start = System.currentTimeMillis();

        try {
            return memberRepository.findAll();

        } finally{
            long finish = System.currentTimeMillis();
            long timeMs = finish - start; // (끝나는 시간 - 시작 시간)
            System.out.println("join " + timeMs + "ms");
        }
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }


}
