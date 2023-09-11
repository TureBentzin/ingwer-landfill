package de.bentzin.ingwer.landfill.netty.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.netty5.util.internal.ObjectUtil.checkNotNullWithIAE;
import static java.util.Objects.requireNonNull;

/**
 * A builder for creating {@link DumpTruckOperatorTrustManagerFactory}.
 */
@SuppressWarnings("NullabilityAnnotations")
public final class DumpTruckOperatorTrustManagerFactoryBuilder<T extends FingerprintHolder> {

    /**
     * A hash algorithm for fingerprints.
     */
    private final String algorithm;

    /**
     * A list of fingerprints.
     */
    private final List<T> fingerprints = new ArrayList<>();

    /**
     * Creates a builder.
     *
     * @param algorithm a hash algorithm
     */
    DumpTruckOperatorTrustManagerFactoryBuilder(String algorithm) {
        this.algorithm = requireNonNull(algorithm, "algorithm");
    }

    /**
     * Adds fingerprints.
     *
     * @param fingerprints a number of fingerprints
     * @return the same builder
     */
    public DumpTruckOperatorTrustManagerFactoryBuilder<T> fingerprints(T... fingerprints) {
        return fingerprints(Arrays.asList(requireNonNull(fingerprints, "fingerprints")));
    }

    /**
     * Adds fingerprints.
     *
     * @param fingerprints a number of fingerprints
     * @return the same builder
     */
    public DumpTruckOperatorTrustManagerFactoryBuilder<T> fingerprints(Iterable<? extends T> fingerprints) {
        requireNonNull(fingerprints, "fingerprints");
        for (T fingerprint : fingerprints) {
            checkNotNullWithIAE(fingerprint.humanReadableFingerprint(), "fingerprint");
            this.fingerprints.add(fingerprint);
        }
        return this;
    }

    /**
     * Creates a {@link DumpTruckOperatorTrustManagerFactory}.
     *
     * @return a new {@link DumpTruckOperatorTrustManagerFactory}
     */
    public DumpTruckOperatorTrustManagerFactory<T> build() {
        if (fingerprints.isEmpty()) {
            throw new IllegalStateException("No fingerprints provided");
        }
        return new DumpTruckOperatorTrustManagerFactory<T>(algorithm, fingerprints);
    }
}