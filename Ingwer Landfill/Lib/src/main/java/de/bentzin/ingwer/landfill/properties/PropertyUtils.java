package de.bentzin.ingwer.landfill.properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Hashtable;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class PropertyUtils {

    public static final @NotNull PropertyUtils SYSTEM = new PropertyUtils(System.getProperties());

    private static final @NotNull Logger logger = LogManager.getLogger();
    private final @NotNull Properties properties;

    public PropertyUtils(@NotNull Properties properties) {
        this.properties = properties;
    }

    public boolean checkForProperty(@NotNull String property) {
        logger.debug("checking for property: " + property);
        return properties.containsKey(property);
    }

    public @NotNull Optional<Object> setProperty(@NotNull String key) {
        return Optional.ofNullable(properties.put(key, ""));
    }

    public @NotNull Optional<Object> setProperty(@NotNull String key, @NotNull Object value) {
        return Optional.ofNullable(properties.put(key, value));
    }

    public void dumpTable(@NotNull Level level, @NotNull Logger logger) {
        StringBuilder builder = new StringBuilder();
        builder.append("Properties contents:\n");
        builder.append("------------------------------\n");
        for (Object key : properties.keySet()) {
            builder.append(String.format("| %-20s | %-20s |\n", key.toString(), properties.get(key).toString()));
        }
        builder.append("------------------------------");

        logger.log(level, builder.toString());
    }

    public void dumpTable(@NotNull Level level) {
        dumpTable(level, logger);
    }

    public void dumpTable() {
        dumpTable(Level.DEBUG, logger);
    }

    @TestOnly
    protected @NotNull Hashtable<Object, Object> raw() {
        return properties;
    }
}
