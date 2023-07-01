package spin.sns.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import spin.sns.domain.image.Image;

import javax.persistence.EntityManager;

@Repository
public class ImageRepositoryImpl implements ImageRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ImageRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Image save(Image image) {
        em.persist(image);
        return image;
    }
}
