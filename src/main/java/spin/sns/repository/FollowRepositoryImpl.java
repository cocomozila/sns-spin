package spin.sns.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class FollowRepositoryImpl implements FollowCustomRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FollowRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
}
