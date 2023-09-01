package de.bentzin.ingwer.landfill.netty.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author Ture Bentzin
 * @since 2023-08-29
 */
public interface FingerprintHolder{

    @NotNull Function<FingerprintHolder, byte[]> mapFingerprints = FingerprintHolder::fingerprint;

    static @NotNull List<byte[]> map(@NotNull Collection<? extends FingerprintHolder> fingerprintHolders) {
        return fingerprintHolders.stream().map(mapFingerprints).toList();
    }

     default byte @NotNull [] fingerprint() {
         return DumpTruckOperatorTrustManagerFactory.toFingerprint(humanReadableFingerprint());
     }

     @NotNull CharSequence humanReadableFingerprint();
}
