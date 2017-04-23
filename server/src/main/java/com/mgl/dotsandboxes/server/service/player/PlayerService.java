package com.mgl.dotsandboxes.server.service.player;

import com.mgl.dotsandboxes.server.repository.player.PlayerRepository;
import com.mgl.dotsandboxes.server.service.player.exception.PlayerNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerService {

    private final @NonNull PlayerRepository playerRepository;

    public void ensurePlayerIdExists(String playerId) {
        if (!playerRepository.playerExistsById(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }
    }

}
