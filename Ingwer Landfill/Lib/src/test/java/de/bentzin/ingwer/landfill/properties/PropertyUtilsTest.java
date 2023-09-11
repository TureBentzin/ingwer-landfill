package de.bentzin.ingwer.landfill.properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyUtilsTest {

    private @NotNull PropertyUtils propertyUtils;

    private @NotNull Set<Object> keySet() {
        return propertyUtils.raw().keySet();
    }

    private @NotNull Object randomKey(boolean existing) {
        if(existing)
            return keySet().stream().skip(new Random().nextInt(keySet().size())).findFirst().orElseThrow();
        else
        {
            return "null-key";
        }
    }

    private @NotNull Random random;

    @BeforeEach
    public void setup() {
        random = new Random();
        Properties properties = generateRandomProperties(random.nextInt(10));
        properties.remove("null-key"); //insurance
        propertyUtils = new PropertyUtils(properties);
    }

    // Rest of the test methods and helper methods
    // ...

    // Helper method for generating random data
    private @NotNull Properties generateRandomProperties(int numProperties) {
        Properties properties = new Properties();
        for (int i = 0; i < numProperties; i++) {
            String key = getRandomKey(properties);
            String value = getRandomValue();
            properties.setProperty(key, value);
        }
        return properties;
    }

    private @NotNull String getRandomKey(@NotNull Properties properties) {
        String key;
        do {
            key = "key_" + getRandomString(5); // Generates random keys in the format "key_XXXXX"
        } while (properties.containsKey(key));
        return key;
    }

    private @NotNull String getRandomValue() {
        return getRandomString(10); // Generates a random string of length 10
    }

    private @NotNull String getRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    @Test
    public void testCheckForProperty_existingProperty_true() {
        assertTrue(propertyUtils.checkForProperty(randomKey(true).toString()));
    }

    @Test
    public void testCheckForProperty_nonExistingProperty_false() {
        assertFalse(propertyUtils.checkForProperty(randomKey(false).toString()));
    }

    @Test
    public void testDumpTable() {
        // Test if the dumpTable() method can be executed without throwing an exception
        propertyUtils.dumpTable();
    }

    @Test
    public void testDumpTable_withLevel() {
        Logger testLogger = LogManager.getLogger("TestLogger");
        // Test if the dumpTable() method with level can be executed without throwing an exception
        propertyUtils.dumpTable(Level.INFO, testLogger);
    }

    @Test
    public void testSetProperty_withKeyOnly() {
        Optional<Object> result = propertyUtils.setProperty("null-key");
        assertTrue(result.isEmpty());
        assertThrows(NoSuchElementException.class, result::get);
        assertTrue(propertyUtils.checkForProperty("null-key"));
    }

    @Test
    public void testSetProperty_withKeyAndValue() {
        String key = "null-key";
        String value = "value2";
        Optional<Object> result = propertyUtils.setProperty(key, value);
        assertTrue(result.isEmpty());
        assertThrows(NoSuchElementException.class, result::get);
        assertTrue(propertyUtils.checkForProperty(key));
        assertEquals(value, propertyUtils.raw().get(key));
    }

}
