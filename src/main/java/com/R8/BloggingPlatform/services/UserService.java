package com.R8.BloggingPlatform.services;


import com.R8.BloggingPlatform.domain.entites.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
