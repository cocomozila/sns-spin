package spin.sns.domain.alarm;

import lombok.NoArgsConstructor;
import spin.sns.domain.user.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    private String targetId;

    @Enumerated(EnumType.STRING)
    private AlarmType type;
    private String read;
    private LocalDateTime createTime;
}
