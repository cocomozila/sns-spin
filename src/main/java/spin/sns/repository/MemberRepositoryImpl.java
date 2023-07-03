package spin.sns.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spin.sns.domain.member.EditPasswordParam;

import javax.persistence.EntityManager;

import static spin.sns.domain.member.QMember.member;

@Repository
@Transactional
public class MemberRepositoryImpl implements MemberCustomRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public void editPassword(EditPasswordParam editPasswordParam, String nickname) {
        query
            .update(member)
            .set(member.password, editPasswordParam.getEditPassword())
            .where(member.nickname.eq(nickname))
            .execute();
    }
}
