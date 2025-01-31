# 회원 관리 예제 - 백엔드 개발

## 비즈니스 요구 상황 정리
- 데이터: 회원 ID, 이름
- 기능: 회원 등록, 조회
- 아직 데이터 저장소가 선정되지 않음(가상의 시나리오)

## 일반적인 웹 애플리케이션 계층 구조

![[Pasted image 20250104200454.png]]
- 컨트롤러: 웹 MVC의 컨트롤러 역할
- 서비스: 핵심 비즈니스 로직 구현
- 리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨.

## 클래스 의존관계
![[Pasted image 20250104200805.png]]
- 아직 데이터 저장소가 선정되지 않아서, 인터페이스로 구현 클래스를 변경할 수 있도록 설계.
- 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용.


## 회원 도메인과 리포지토리 만들기
### 회원 객체
**/hello_spring/domain/Member**
~~~ java
public class Member {  
    private String name;  
    private Long id;  
  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public Long getId() {  
        return id;  
    }  
    public void setId(Long id) {  
        this.id = id;  
    }
}
~~~

### 회원 리포지토리 인터페이스
**/hello_spring/repository/MemberRepository**
~~~ java
public interface MemberRepository {  
    Member save(Member member);  
    Optional<Member> findById(Long id);  
    Optional<Member> findByName(String name);  
    List<Member> findAll();  
}
~~~

### 회원 리포지토리 구현체
**/hello_spring/repository/MemoryMemberRepository**
~~~ java
public class MemoryMemberRepository implements MemberRepository{  
    private static Map<Long, Member> store = new HashMap<>();  
    private static long sequence = 0L;  
  
    @Override  
    public Member save(Member member) {  
        member.setId(++sequence);  
        store.put(member.getId(), member);  
        return member;  
    }
    
    // 아이디로 회원 조회하기  
    @Override  
    public Optional<Member> findById(Long id) {  
        return Optional.ofNullable(store.get(id));  
    }
    
    // 이름으로 회원 조회하기  
    @Override  
    public Optional<Member> findByName(String name) {  
        // stream은 배열이나 컬렉션 같은 데이터 소스를 처리할 수 있는 API이다.  
        return store.values().stream() // 모든 values들을 가져와서  
                .filter(member -> member.getName().equals(name)) // 회원의 이름이 같은 것을 걸러서  
                .findAny(); // 요소를 하나라도 찾으면, 그 요소를 담고 있는 Optional<T(요소의 타입)>를 반환합니다.  
                           // 요소가 없다면 빈 Optional 반환  
    }  
  
    @Override  
    public List<Member> findAll() {  
        return new ArrayList<>(store.values());  
    }
    
    public void clearStore() {  
        store.clear();  
    }
}
~~~

### 회원 리포지토리 테스트
웹 애플리케이션의 컨트롤러를 통해 반복 실행하고 검증하기엔 오래 걸리고 여러 테스트를 한번에 실행하기에는 어렵다는 단점이 있다. 자바는 JUnit이라는 프레임워크로 테스트를 실행해서 이러한 문제를 해결한다.

**/test/repository/MemoryMemberRespositoryTest**
~~~ java
public class MemoryMemberRepositoryTest {  
  
    MemoryMemberRepository repository = new MemoryMemberRepository();  
  
    @AfterEach  
    public void afterEach() {  
        repository.clearStore();  
    }  
  
    @Test  
    @DisplayName("회원 가입")  
    public void save() {  
        Member member = new Member();  
        member.setName("김미래");  
        repository.save(member);  
  
        assertThat(repository.findByName("김미래").get()).isEqualTo(member);  
    } 
   
    @Test  
    @DisplayName("아이디로 회원 조회하기")  
    public void findById() {  
        // given  
        Member member = new Member();  
        member.setName("김미래");  
  
        // when  
        repository.save(member);  
  
        // then  
	        assertThat(repository.findById(member.getId()).get()).isEqualTo(member);  
  
    }  
  
    @Test  
    @DisplayName("이름으로 회원 조회하기")  
    public void findByName() {  
        // given  
        Member member1 = new Member();  
        member1.setName("spring1");  
        repository.save(member1);  
  
        Member member2 = new Member();  
        member2.setName("spring2");  
        repository.save(member2);  
  
        // when  
        Member result = repository.findByName("spring1").get();  
  
        // then  
        assertThat(result).isEqualTo(member1);  
    } 
   
    @Test  
    @DisplayName("모든 회원 조회하기")  
    public void findAll() {  
        // given  
        Member member = new Member();  
        member.setName("김미래");  
        repository.save(member);  
  
        Member member1 = new Member();  
        member1.setName("김노아");  
        repository.save(member1);  
  
        Member member2 = new Member();  
        member2.setName("Noah Kim");  
        repository.save(member2);  
  
        // when  
        List<Member> allMemberList = repository.findAll();  
  
        // then  
        assertThat(allMemberList.size()).isEqualTo(3);  
    }
}
~~~
- @AfterEach: 여러 테스트를 진행하면 DB에 직전 테스트 결과가 남을 수 있어 테스트가 실패할 가능성이 있다. @AfterEach를 선언하면 각 테스트가 종료될 때 마다 기능이 실행된다. 여기서는 메모리 DB에 저장된 데이터를 삭제한다.
- 각 테스트는 독립적으로 실행되어야 한다. 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니다.

### 회원 서비스 개발

**/hello_spring/service/MemberService**
~~~ java
public class MemberService {  
    private final MemberRepository memberRepository;  
    private Member member;  
  
    public MemberService(MemoryMemberRepository memberRepository) {  
        this.memberRepository = memberRepository;  
    }  
  
  
    // 회원 가입  
    public Long join(Member member) {  
  
        validateDuplicateMember(member); // 중복 회원 검증  
        memberRepository.save(member);  
        return member.getId();  
    }  
  
    private void validateDuplicateMember(Member member) {  
        memberRepository.findByName(member.getName())  
                // ifPresent 내부에 Optional 객체가 있을 시 내부 연산 실행  
                .ifPresent(m -> {  
                    throw new IllegalStateException("이미 존재하는 회원입니다.");  
                });  
    }  

	// 전체 회원 조회
    public List<Member> findMembers() {  
        return memberRepository.findAll();  
    }
    
    public Optional<Member> findOne(Long memberId) {  
        return memberRepository.findById(memberId);  
    }  
}
~~~

기존에는 회원 서비스가 리포지토리를 의존하고 있었다.
~~~ java
private final MemberRepository memberRespository =					
							new MemoryMemberRespository();

~~~

회원 리포지토리의 코드가
회원서비스 코드를 DI 가능하게 변경한다.
~~~ java
public class MemberService() {
	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository){
		this.memberRepository = memberRepository;	
	}
}
~~~

### 회원 서비스 테스트

**/tset/service/MemberServiceTest**
~~~ java
public class MemberServiceTest {  
    MemberService memberService;  
    MemoryMemberRepository memberRepository;  
  
    @BeforeEach  
    public void BeforeEach() {  
        memberRepository = new MemoryMemberRepository();  
        memberService = new MemberService(memberRepository);  
    }  
  
    @AfterEach  
    public void afterEach() {  
        memberRepository.clearStore();  
    }  
  
    @Test  
    public void 회원가입() {  
        // given  
        Member member = new Member();  
        member.setName("김미래");  
  
        // when  
        Long saveId = memberService.join(member);  
  
  
        // then  
        Member findMember = memberRepository.findById(saveId).get();  
        assertEquals(member.getName(), findMember.getName());  
  
    }  
  
    @Test  
    public void 전체_회원_조회(){  
        // given  
        Member member = new Member();  
        member.setName("spring1");  
  
        Member member2 = new Member();  
        member2.setName("spring2");  
  
        Member member3 = new Member();  
        member3.setName("spring3");  
        // when  
        memberService.join(member);  
        memberService.join(member2);  
        memberService.join(member3);  
  
        // then  
        assertThat(memberService.findMembers().size()).isEqualTo(3);  
    }  
  
    @Test  
    public void 회원_조회() {  
        // given  
        Member member = new Member();  
        member.setName("김미래");  
  
        // when  
        Long memberId = memberService.join(member);  
  
        // then  
        assertThat(memberService.findOne(memberId).get()).isEqualTo(member);
    }
    
    @Test  
    public void 중복_회원_예외() throws Exception {  
        // given  
        Member member1 = new Member();  
        member1.setName("김미래");  
  
        Member member2 = new Member();  
        member2.setName("김미래");  
  
        // when  
        memberService.join(member1);  
        IllegalStateException e = assertThrows(IllegalStateException.class,  
                () -> memberService.join(member2));  
  
        // then  
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");  
  
        /** Junit  
         *  assertThrows()         *  메서드가 예외를 던지는지 확인할 때 사용한다.  
         *  메서드가 던질 것으로 예상되는 예외를 지정하고, 테스트된 메서드를 지정한다.  
         *  assertThrows()메서드는 테스트 메서드를 호출하고 예상된 예외를 던지는지 검증한다.  
         */    }  
}
~~~

- @BeForeEach(): 각 테스트가 실행되기전에 매번 실행된다. 여기서는 MemoryMemberRepository DB를 쓰기 위해서 서비스에 주입했다.
- assertThrows(): 메서드가 예외를 던지는지 확인할 때 사용한다. 메서드가 던질 것으로 예상되는 예외를 지정하고, 테스트된 메서드를 지정한다. 여기서는 중복 회원을 등록하고 IllegalStateException.class 예외가 발생할 것을 예상하고 검증하기 위해서 썼다.

