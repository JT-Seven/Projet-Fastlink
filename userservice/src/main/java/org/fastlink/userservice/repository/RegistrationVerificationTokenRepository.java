package org.fastlink.userservice.repository;

import org.fastlink.userservice.model.RegistrationVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegistrationVerificationTokenRepository extends JpaRepository<RegistrationVerificationToken, Long>
{
    RegistrationVerificationToken findByToken(String token);
}