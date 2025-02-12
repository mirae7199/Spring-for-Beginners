package hello.re_hello_spirng;

import hello.re_hello_spirng.controller.MemberController;
import hello.re_hello_spirng.repository.JdbcMemberRepository;
import hello.re_hello_spirng.repository.JpaMemberRespository;
import hello.re_hello_spirng.repository.MemberRepository;
import hello.re_hello_spirng.repository.MemoryMemberRepository;
import hello.re_hello_spirng.service.MemberService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// 자바코드로 직접 스프링 빈 등록하기!
@Configuration
public class SpringConfig {

    // DataSource는 데이터베이스 커넥션을 획득할 때 사용하는 객체이다.
    // 스프링 부트는 데이터베이스 커넥션 정보를 바탕으로 DataSource를 생성하고 스프링 빈으로 만들어둔다. 그래서 DI를 받을 수 있다.
    private final DataSource dataSource;
    private EntityManager em;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        //return new MemoryMemberRepository();
        //return new JdbcMemberRepository(dataSource);
        return new JpaMemberRespository(em);
    }


}
