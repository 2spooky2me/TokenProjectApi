package com.jb.client;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ClientSession {
    private final UUID uuid;
    private final ClientType clientType;
    private long lastAccessMillis;

    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;

    private ClientSession(UUID employeeUuid, ClientType clientType) {
        this.uuid = employeeUuid;
        this.clientType = clientType;
        this.lastAccessMillis = System.currentTimeMillis();
    }

    public static ClientSession of(UUID clientUuid, ClientType clientType) {
        return new ClientSession(clientUuid, clientType);
    }

    public void access() {
        lastAccessMillis = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - lastAccessMillis > SESSION_TIMEOUT;
    }
}
