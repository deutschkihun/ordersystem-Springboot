package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            // 아이디가 null 이라는 말은 객체가 완전 새로 생성된것
            em.persist(item); //신규등록
        } else {
            em.merge(item);
            // else 의 경우는 즉 item.getId가 이미 있다 = 이미 저장되어있다 = 이미 식별자가 있다.
            // 이미 식별자가 있는 경우를 업데이트 한다면 이때의 상태는 준영속 상태가 된다.
        }
    }
    public Item findOne(Long id) {
        return em.find(Item.class,id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }

    public List<Book> findBookAll(){
        return em.createQuery("select b from Book b",Book.class)
                .getResultList();
    }

    public List<Movie> findMovieAll(){
        return em.createQuery("select m from Movie m",Movie.class)
                .getResultList();
    }

    public List<Album> findAlbumAll() {
        return em.createQuery("select a from Album a",Album.class)
                .getResultList();
    }
}
