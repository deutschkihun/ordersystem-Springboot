package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository; // 바꿀일이 없기 때문에 final 권장

    /*@Autowired
    private MemberRepository memberRepository;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/
    // 필드 혹은 set메소드 injection보다는 생성자 injection이 훨씬 유연하다.



    /*@Autowired  // 생락가능
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/

    //  이렇게 하면 좋은 이유가 MemberService객체를 생성할떄 memberRepository를 의존하고 있기에 인자를 MemberRepository의 것으로 안주면 에러
    //  절대 까먹지 않고 사용할수있따.

    // 그런데 그것보더 더 간편한 것은 RequiredArgsConstructor이다, 이거하면 필요한 생성자를 자동으로 설정해준다.

    @Transactional
    public Long join(Member member) {
        // 회원가입
        NameDuplication(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void NameDuplication(Member member) {
        List<Member> findMember = memberRepository.findByName(member.getName());
        if (!findMember.isEmpty()){
            throw new IllegalStateException("already exited username please set different username thank you !!");
        }
    }

    // 읽기 전용 예를 들어 조회 같은거에  @Transactional(readOnly = true) 넣으면 성능이 최적화 됨.
    // 일기 전용이 아니면 절대 저거 쓰면 안됨.

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId); // 단건조회
    }



    // 변경 감지
    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);

    }
}
