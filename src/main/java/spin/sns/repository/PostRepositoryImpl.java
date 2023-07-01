package spin.sns.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spin.sns.domain.post.Post;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class PostRepositoryImpl implements PostRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PostRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Post uploadPost(Post post) {
        em.persist(post);
        return post;
    }
}
