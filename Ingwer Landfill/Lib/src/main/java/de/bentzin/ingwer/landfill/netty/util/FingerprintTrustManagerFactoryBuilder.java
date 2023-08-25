package de.bentzin.ingwer.landfill.netty.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.netty5.util.internal.ObjectUtil.checkNotNullWithIAE;
import static java.util.Objects.requireNonNull;

/**
 * A builder for creating {@link FingerprintTrustManagerFactory}.
 */
@SuppressWarnings("NullabilityAnnotations")
public final class FingerprintTrustManagerFactoryBuilder {

    /**
     * A hash algorithm for fingerprints.
     */
    private final String algorithm;

    /**
     * A list of fingerprints.
     */
    private final List<String> fingerprints = new ArrayList<>();

    /**
     * Creates a builder.
     *
     * @param algorithm a hash algorithm
     */
    FingerprintTrustManagerFactoryBuilder(String algorithm) {
        this.algorithm = requireNonNull(algorithm, "algorithm");
    }

    /**
     * Adds fingerprints.
     *
     * @param fingerprints a number of fingerprints
     * @return the same builder
     */
    public FingerprintTrustManagerFactoryBuilder fingerprints(CharSequence... fingerprints) {
        return fingerprints(Arrays.asList(requireNonNull(fingerprints, "fingerprints")));
    }

    /**
     * Adds fingerprints.
     *
     * @param fingerprints a number of fingerprints
     * @return the same builder
     */
    public FingerprintTrustManagerFactoryBuilder fingerprints(Iterable<? extends CharSequence> fingerprints) {
        requireNonNull(fingerprints, "fingerprints");
        for (CharSequence fingerprint : fingerprints) {
            checkNotNullWithIAE(fingerprint, "fingerprint");
            this.fingerprints.add(fingerprint.toString());
        }
        return this;
    }

    /**
     * Creates a {@link FingerprintTrustManagerFactory}.
     *
     * @return a new {@link FingerprintTrustManagerFactory}
     */
    public FingerprintTrustManagerFactory build() {
        if (fingerprints.isEmpty()) {
            throw new IllegalStateException("No fingerprints provided");
        }
        byte[][] fingerprints = FingerprintTrustManagerFactory.toFingerprintArray(this.fingerprints);
        return new FingerprintTrustManagerFactory(algorithm, fingerprints);
    }
}