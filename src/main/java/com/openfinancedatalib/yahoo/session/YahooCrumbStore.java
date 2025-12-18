package com.openfinancedatalib.yahoo.session;

import java.time.Duration;
import java.time.Instant;

public final class YahooCrumbStore {

    private static String crumb;
    private static Instant expiresAt;

    private YahooCrumbStore() {
        // evita instanciação
    }

    public static synchronized String get() {
        if (crumb == null || isExpired()) {
            return null;
        }
        return crumb;
    }

    public static synchronized void put(String value, Duration ttl) {
        crumb = value;
        expiresAt = Instant.now().plus(ttl);
    }

    public static synchronized void clear() {
        crumb = null;
        expiresAt = null;
    }

    public static boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }
}
