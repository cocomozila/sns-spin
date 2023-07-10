package spin.sns.domain.follow;

import lombok.Getter;

import java.util.List;

@Getter
public class Followers {

    private int followerNumber;
    private List<String> followerNicknames;

    public Followers(int followerNumber, List<String> followerNicknames) {
        this.followerNumber = followerNumber;
        this.followerNicknames = followerNicknames;
    }
}
