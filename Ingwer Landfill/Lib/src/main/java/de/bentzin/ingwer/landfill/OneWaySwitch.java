package de.bentzin.ingwer.landfill;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class OneWaySwitch {

    private boolean flipped = false;

    private static @NotNull Runnable flipSecureSwitch(@NotNull SecureOneWaySwitch secureOneWaySwitch) {
        return secureOneWaySwitch::secureFlip;
    }

    public static @NotNull OneWaySwitch onFuture(@NotNull CompletableFuture<?> future) {
        SecureOneWaySwitch secureOneWaySwitch = new SecureOneWaySwitch();
        future.thenRun(flipSecureSwitch(secureOneWaySwitch));
        return secureOneWaySwitch;
    }

    public static @NotNull OneWaySwitch onFutureAsync(@NotNull CompletableFuture<?> future) {
        SecureOneWaySwitch secureOneWaySwitch = new SecureOneWaySwitch();
        future.thenRunAsync(flipSecureSwitch(secureOneWaySwitch));
        return secureOneWaySwitch;
    }

    public static @NotNull OneWaySwitch flipped() {
        OneWaySwitch oneWaySwitch = new OneWaySwitch();
        oneWaySwitch.flip();
        return oneWaySwitch;
    }

    public static @NotNull Predicate<?> flipPredicate(@NotNull OneWaySwitch oneWaySwitch) {
        return o -> oneWaySwitch.flipped;
    }

    public static final @NotNull Predicate<OneWaySwitch> predicate = OneWaySwitch::isFlipped;

    public final boolean isFlipped() {
        return flipped;
    }

    public void flip() {
        if (!allowFlip())
            throw new UnsupportedOperationException("This switch cant be flipped by you. This can be determined by calling #allowFlip");
        protectedFlip();
    }

    protected void protectedFlip() {
        if (isFlipped()) throw new IllegalStateException("switch is already flipped");
        flipped = true;
    }

    public boolean allowFlip() {
        return true;
    }

    public int toInt() {
        return isFlipped()? 1 : 0;
    }

    public boolean ifFlipped(@NotNull Runnable runnable) {
        if(isFlipped()) runnable.run();
        return isFlipped();
    }

    public void ifFlippedOrElse(@NotNull Runnable runnable, @NotNull Runnable orElse) {
        if(!ifFlipped(runnable)) orElse.run();
    }

    private static final class SecureOneWaySwitch extends OneWaySwitch {

        public void secureFlip() {
            this.protectedFlip();
        }

        @Override
        public boolean allowFlip() {
            return false;
        }
    }
}
