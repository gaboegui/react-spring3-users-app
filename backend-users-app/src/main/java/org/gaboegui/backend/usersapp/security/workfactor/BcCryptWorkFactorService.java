package org.gaboegui.backend.usersapp.security.workfactor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Calculates the optimal workload for BcCrypt in the hardware
 * running under this computer
 *
 * Source: <a href="https://github.com/thombergs/code-examples/tree/master/spring-boot/password-encoding/src/main/java/io/reflectoring/passwordencoding/workfactor">...</a>
 * Modified by: Gabriel Eguiguren P.
 */
@Component
public class BcCryptWorkFactorService {

    private static final String TEST_PASSWORD = "my password";
    private static final int GOAL_MILLISECONDS_PER_PASSWORD = 1000;
    private static final int MIN_STRENGTH = 4;
    private static final int MAX_STRENGTH = 31;

    /**
     * Calculates the strength (a.k.a. log rounds) for the BCrypt Algorithm, so that password encoding
     * takes about 1s. This method uses the divide-and-conquer algorithm
     */
    public BcryptWorkFactor calculateStrengthDivideAndConquer() {
        return calculateStrengthDivideAndConquer(
                new BcryptWorkFactor(MIN_STRENGTH, Integer.MIN_VALUE),
                new BcryptWorkFactor(MAX_STRENGTH, Integer.MAX_VALUE));
    }

    private BcryptWorkFactor calculateStrengthDivideAndConquer(
            BcryptWorkFactor smallFactor, BcryptWorkFactor bigFactor) {
        if (bigFactor.getStrength() - smallFactor.getStrength() == 1) {
            return getClosestStrength(smallFactor, bigFactor);
        }
        int midStrength =
                (bigFactor.getStrength() - smallFactor.getStrength()) / 2 + smallFactor.getStrength();
        long duration = calculateDuration(midStrength);
        BcryptWorkFactor midFactor = new BcryptWorkFactor(midStrength, duration);
        if (duration < GOAL_MILLISECONDS_PER_PASSWORD) {
            return calculateStrengthDivideAndConquer(midFactor, bigFactor);
        }
        return calculateStrengthDivideAndConquer(smallFactor, midFactor);
    }

    private BcryptWorkFactor getClosestStrength(
            BcryptWorkFactor smallFactor, BcryptWorkFactor bigFactor) {
        if (isPreviousDurationCloserToGoal(smallFactor.getDuration(), bigFactor.getDuration())) {
            return smallFactor;
        }
        return bigFactor;
    }

    private long calculateDuration(int strength) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength);

        Instant before = Instant.now();
        bCryptPasswordEncoder.encode(TEST_PASSWORD);
        Instant after = Instant.now();
        return Duration.between(before, after).toMillis();
    }

    /**
     * Calculates the strength (a.k.a. log rounds) for the BCrypt Algorithm, so that password encoding
     * takes about 1s. This method iterates over strength from 4 to 31 and calculates the duration of
     * password encoding for every value of strength. It returns the first strength, that takes more
     * than 1s
     */
    public int calculateStrength() {
        for (int strength = MIN_STRENGTH; strength <= MAX_STRENGTH; strength++) {

            long duration = calculateDuration(strength);
            if (duration >= GOAL_MILLISECONDS_PER_PASSWORD) {
                return strength;
            }
        }
        throw new RuntimeException(
                String.format(
                        "Could not find suitable round number for bcrypt encoding. The encoding with %d rounds"
                                + " takes less than %d ms.",
                        MAX_STRENGTH, GOAL_MILLISECONDS_PER_PASSWORD));
    }

    /**
     * @param previousDuration duration from previous iteration
     * @param currentDuration duration of current iteration
     * @param strength current strength
     * @return return the current strength, if current duration is closer to
     *     GOAL_MILLISECONDS_PER_PASSWORD, otherwise current strength-1.
     */
    int getStrength(long previousDuration, long currentDuration, int strength) {
        if (isPreviousDurationCloserToGoal(previousDuration, currentDuration)) {
            return strength - 1;
        } else {
            return strength;
        }
    }

    /**
     * return true, if previousDuration is closer to the goal than currentDuration, false otherwise.
     */
    boolean isPreviousDurationCloserToGoal(long previousDuration, long currentDuration) {
        return Math.abs(GOAL_MILLISECONDS_PER_PASSWORD - previousDuration)
                < Math.abs(GOAL_MILLISECONDS_PER_PASSWORD - currentDuration);
    }
}


