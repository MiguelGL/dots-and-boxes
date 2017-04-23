package com.mgl.dotsandboxes.server.repository.player;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.mgl.dotsandboxes.server.model.player.PlayerProfile;
import com.mgl.dotsandboxes.server.model.player.PlayerRegistration;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PlayerRepository {

    /**
     * A playerId <-> PlayerProfile store.
     */
    private final BiMap<String, PlayerProfile> playerProfiles
            = Maps.synchronizedBiMap(HashBiMap.create());

    private static String idFromProfile(PlayerProfile playerProfile) {
        // Just a random UUID, nothing fancy nor calculated from the profile.
        return UUID.randomUUID().toString();
    }

    public PlayerRegistration ensurePlayerRegistered(PlayerProfile playerProfile) {
        BiMap<PlayerProfile, String> inverseView = playerProfiles.inverse();
        synchronized (playerProfiles) {
            String playerId = inverseView.computeIfAbsent(
                    playerProfile, PlayerRepository::idFromProfile);
            return PlayerRegistration.ofPlayerId(playerId);
        }
    }

    public boolean playerExistsById(String playerId) {
        return playerProfiles.containsKey(playerId);
    }
}
