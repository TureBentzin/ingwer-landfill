package de.bentzin.ingwer.landfill.properties;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class RealProperties implements PropertySource{

    private final @NotNull Properties properties;

    public RealProperties(@NotNull Properties properties) {

        this.properties = properties;
    }

    @Override
    public @Nullable String set(@NotNull String key, @NotNull String value) {
        return properties.setProperty(key,value);
    }

    @Override
    public @Nullable String get(@NotNull String key) {
        return null;
    }

    @Override
    public @NotNull Set<String> keySet() {
        return null;
    }

    @Override
    public @NotNull List<String> values() {
        return null;
    }

    @Override
    public @NotNull Map<String, String> toMap() {
        return null;
    }

    @Override
    public @NotNull Stream<Map.Entry<String, String>> stream() {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void dump(@NotNull Logger destination) {

    }

    @Override
    public void dump(@NotNull StringBuilder destination) {

    }
}
