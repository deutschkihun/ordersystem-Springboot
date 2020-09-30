package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    /*@PersistenceContext
    private EntityManager em;*/

    /*public MemberRepository(EntityManager em) {
        this.em = em;
    }*/

    // member save
    public void save(Member member){
        em.persist(member);
    }

    // fine specific member => search id(primary key)
    // void x / Member o
    public Member findOne(Long id){
        return em.find(Member.class,id);
        // 회원 찾기
    }

    public List<Member> findAll(){
       return em.createQuery("select m from Member m",Member.class)
               .getResultList();
    }

    // 위의 createQuery와 달리 이름으로 찾는다.
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }

    // Advance : 아이디로 찾기 !!
    public List<Member> findById(Long id){
        return em.createQuery("select m from Member m.id = m.id",Member.class)
                .setParameter("id",id)
                .getResultList();
    }

    public List<Member> findByAddress(Address address){
        return em.createQuery("select m from Member m where m.address =:address",Member.class)
                .setParameter("address",address)
                .getResultList();
    }


}
