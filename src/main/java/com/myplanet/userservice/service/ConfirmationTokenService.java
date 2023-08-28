package com.myplanet.userservice.service;

import com.myplanet.userservice.domain.ConfirmationToken;

public interface ConfirmationTokenService {

    ConfirmationToken getToken(String token);
    void generateConfirmationToken(ConfirmationToken token);

    void setConfirmedAt(String token);
}
