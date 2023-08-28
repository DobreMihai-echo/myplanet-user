package com.myplanet.userservice.service.impl;

import com.myplanet.userservice.domain.ConfirmationToken;
import com.myplanet.userservice.repository.ConfirmationTokenRepository;
import com.myplanet.userservice.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository repository;


    public ConfirmationToken getToken(String token) {
        return repository.findByToken(token).orElseThrow();
    }

    public void generateConfirmationToken(ConfirmationToken token) {
        repository.save(token);
    }

    @Override
    public void setConfirmedAt(String token) {
        ConfirmationToken tokenToConfirm = repository.findByToken(token).orElseThrow();
        tokenToConfirm.setConfirmedAt(LocalDateTime.now());
        repository.save(tokenToConfirm);
    }
}
