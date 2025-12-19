package com.openfinancedatalib.yahoo.session;

import java.time.Duration;
import java.time.Instant;

/**
 * In-memory store for the Yahoo Finance crumb.
 *
 * <p>
 * This class is responsible for temporarily storing the Yahoo
 * <b>crumb</b> along with its expiration time (TTL).
 *
 * <p>
 * The crumb is a short-lived authentication token required
 * by Yahoo Finance to authorize API requests.
 *
 * <p>
 * IMPORTANT:
 * <ul>
 *   <li>This store is JVM-wide (static)</li>
 *   <li>It does NOT persist data</li>
 *   <li>It does NOT refresh crumbs</li>
 *   <li>It does NOT manage cookies</li>
 * </ul>
 *
 * <p>
 * All lifecycle decisions (when to generate, clear, or retry)
 * are handled by higher-level components such as
 * {@link com.openfinancedatalib.yahoo.YahooRequestCoordinator}.
 */
public final class YahooCrumbStore {

    /** Cached Yahoo crumb value */
    private static String crumb;

    /** Instant when the crumb expires */
    private static Instant expiresAt;

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This class is intended to be used only via static methods.
     */
    private YahooCrumbStore() {
        // prevents instantiation
    }

    /**
     * Returns the currently stored crumb if it is still valid.
     *
     * <p>
     * If the crumb is missing or expired, this method returns {@code null}.
     *
     * @return the cached crumb, or {@code null} if invalid or expired
     */
    public static synchronized String get() {
        if (crumb == null || !isValid()) {
            return null;
        }
        return crumb;
    }

    /**
     * Stores a new crumb with a given time-to-live (TTL).
     *
     * <p>
     * The expiration time is calculated based on the current instant
     * plus the provided TTL.
     *
     * <p>
     * This method overwrites any previously stored crumb.
     *
     * @param value the crumb value to store
     * @param ttl how long the crumb should remain valid
     */
    public static synchronized void put(String value, Duration ttl) {
        crumb = value;
        expiresAt = Instant.now().plus(ttl);
    }

    /**
     * Clears the stored crumb and its expiration time.
     *
     * <p>
     * After calling this method, the crumb will be considered invalid
     * and a new one must be obtained.
     *
     * <p>
     * This is typically called when Yahoo invalidates authentication
     * or when a request fails with an authorization error.
     */
    public static synchronized void clear() {
        crumb = null;
        expiresAt = null;
    }

    /**
     * Checks whether the currently stored crumb is still valid.
     *
     * <p>
     * A crumb is considered valid if:
     * <ul>
     *   <li>An expiration time exists</li>
     *   <li>The current time is before the expiration time</li>
     *   <li>The crumb value is not {@code null}</li>
     * </ul>
     *
     * @return {@code true} if the crumb is valid; {@code false} otherwise
     */
    public static boolean isValid() {
        return expiresAt != null
                && Instant.now().isBefore(expiresAt)
                && crumb != null;
    }
}
