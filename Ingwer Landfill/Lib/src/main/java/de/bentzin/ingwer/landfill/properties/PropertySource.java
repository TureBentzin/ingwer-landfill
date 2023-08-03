package de.bentzin.ingwer.landfill.properties;

import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

//Maybe generic?!

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public interface PropertySource {

    @Nullable String set(@NotNull String key, @NotNull String value);
    @Nullable String get(@NotNull String key);

    @NotNull Set<String> keySet();
    @NotNull List<String> values();
    @NotNull Map<String,String> toMap();

    @NotNull Stream<Map.Entry<String,String>> stream();

    void clear();

    void dump(@NotNull Logger destination);
    void dump(@NotNull StringBuilder destination);
}
