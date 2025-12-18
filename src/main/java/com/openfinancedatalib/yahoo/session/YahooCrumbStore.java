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
        if (crumb == null || !isValid()) {
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

    public static boolean isValid() {
        return expiresAt != null && Instant.now().isBefore(expiresAt) && crumb != null;
    }
}
