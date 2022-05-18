package com.ably.auth.repository;

import com.ably.auth.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Otp findTopByPhoneAndOtpCode(String phone, String otpCode);

    Otp findTopByPhoneOrderByIdDesc(String phone);
}
