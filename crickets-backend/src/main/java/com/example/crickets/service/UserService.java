package com.example.crickets.service;

import com.example.crickets.data.*;
import com.example.crickets.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.util.*;

@ApplicationScoped
public class UserService {

    private static final Set<String> VALID_API_KEYS = new HashSet<>() {{
        add("fi4thee4kieyahhei3Chahth3iek6eib");
        add("eeGix6Ooceew4booVeele6VeeTa1ahWu");
        add("Ue4Aeghei4hagei1Tai4axoothooJam3");
    }};

    @Inject
    private UserRepository userRepository;

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    // API-Key-Validierung
    public boolean checkApiKey(String apiKey) {
        return VALID_API_KEYS.contains(apiKey);
    }

}
