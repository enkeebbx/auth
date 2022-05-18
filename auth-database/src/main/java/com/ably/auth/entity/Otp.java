package com.ably.auth.entity;

import com.ably.auth.model.YesNo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "otp")
@SuperBuilder
@NoArgsConstructor
public class Otp extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    private Long id;

    @Column
    private String phone;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "success_yn")
    @Enumerated(EnumType.STRING)
    private YesNo successYn;

    @Column
    private LocalDateTime expireAt;

    public static Otp of(String phone, String otpCode, int ttlMin) {
        return Otp.builder()
                .phone(phone)
                .otpCode(otpCode)
                .successYn(YesNo.N)
                .expireAt(LocalDateTime.now().plusMinutes(ttlMin))
                .build();
    }

    public void success() {
        successYn = YesNo.Y;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireAt);
    }

    public boolean isFail() {
        return successYn == YesNo.N;
    }
}
