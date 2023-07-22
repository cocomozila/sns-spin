package spin.sns.repository;

import spin.sns.domain.member.EditPasswordParam;

public interface MemberCustomRepository {

    void editPassword(EditPasswordParam editPasswordParam, String nickname);
}
