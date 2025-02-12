package hello.re_hello_spirng.repository;

import hello.re_hello_spirng.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository {
    /**
     * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
     */

    private static Long sequence = 0L;

                     // key,  value
    private static Map<Long, Member> store = new HashMap<>();

    // 회원 등록
    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    // id로 회원 검색
    @Override
    public Optional<Member> findById(Long id) {
        // Optional객체의 생성
        // of()메소드나 ofNullable()메소드를 사용해서 Optional객체를 생성할 수 있음.
        // null 발생 가능성이 있는 값이라면 ofNullable()을 통해 생성해야 NullPointException(NPE)를 발생시키지 않는다.
        return Optional.ofNullable(store.get(id));
    }

    // 이름으로 회원 검색
    @Override
    public Optional<Member> findByName(String name) {
        // store.values() 맵의 모든 값들을 반환
        // store.values().stream() 이값들을 스트림(stream)으로 변환. 스트림은 데이터의 연속적인 흐름을 처리하는데 사용
        // .filter() 메서드는 스트림의 각 요소를 검사하여 주어진 조건을 만족하는 요소만 남긴다.
        // 람다식 member -> member.getName().equals(name) boolean 값 반환 // Predicate<T> 인터페이스에 대해서 더 공부할 것
        // .findAny() 메서드는 스트림에서 조건을 만족하는 임의의 요소를 반환한다. 조건을 만족하는 요소가 없으면 빈 Optional 객체를 반환
        return store.values().stream()
                .filter(member -> member.getName().equals(name)) // member의 이름이 같은 것만 남기기
                .findAny(); // 위에 조건에 맞는 것만 반환


    }

    // 모든 회원정보 조회
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    // 초기화
    public void clearStore() {
        store.clear();
    }
}
