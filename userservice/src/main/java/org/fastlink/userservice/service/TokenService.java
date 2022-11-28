package org.fastlink.userservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastlink.userservice.model.RegistrationVerificationToken;
import org.fastlink.userservice.repository.RegistrationVerificationTokenRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TokenService
{
    private final RegistrationVerificationTokenRepository tokenRepository;

    public RegistrationVerificationToken findToken(String verifToken)
    {
        log.info("fetching verification token - {}", verifToken);
        return tokenRepository.findByToken(verifToken);
    }

    public void deleteToken(RegistrationVerificationToken token)
    {
        log.info("Deleting verification token - {}", token.getToken());
        tokenRepository.delete(token);
    }

    public RegistrationVerificationToken save(RegistrationVerificationToken token)
    {
       return tokenRepository.save(token);
    }
}
