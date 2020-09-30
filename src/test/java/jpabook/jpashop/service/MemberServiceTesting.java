package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional  // default : rollback = true, insert query 가 안보임, 볼려면 false 로 변경
public class MemberServiceTesting {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    /*
    * given when then
    * 이렇게 하면(given), 이렇게 되었을때(when), 이렇게 된다(then).*/

    @Test
    @Rollback(value = false)
    public void MemberJoin() throws Exception{
        // given
        Member member = new Member();
        member.setName("Kihun");
        // when
        Long saveId = memberService.join(member);

        // then
        em.flush();
        assertEquals(member,memberRepository.findOne(saveId));
    }


    @Test(expected = IllegalStateException.class) // try catch 대체 annotation
    public void DuplicationIdException() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("deutschkihun");
        Member member2 = new Member();
        member2.setName("deutschkihun");
        // when
        memberService.join(member1);
        System.out.println("======");
        memberService.join(member2);

       /* try{
            memberService.join(member2);
        } catch (IllegalStateException e){
            return;
        }*/

        //then
        fail("Exception happened");
    }
}