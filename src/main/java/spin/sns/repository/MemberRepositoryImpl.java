package spin.sns.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import spin.sns.domain.member.EditPasswordParam;
import spin.sns.domain.member.Member;
import spin.sns.domain.member.QMember;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@Transactional
public class MemberRepositoryImpl implements MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public void deleteMember(Member member) {
        em.remove(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        QMember member = QMember.member;
        return Optional.ofNullable(
                query
                .select(member)
                .from(member)
                .where(member.email.equalsIgnoreCase(email))
                .fetchOne());
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        QMember member = QMember.member;
        return Optional.ofNullable(
                query
                .select(member)
                .from(member)
                .where(member.nickname.eq(nickname))
                .fetchOne());
    }

    @Override
    public void editPassword(EditPasswordParam editPasswordParam, String nickname) {
        QMember member = QMember.member;

        query
            .update(member)
            .set(member.password, editPasswordParam.getEditPassword())
            .where(member.nickname.eq(nickname))
            .execute();
    }
}
