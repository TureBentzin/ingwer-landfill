package de.bentzin.ingwer.landfill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-04
 */
class DisplayableTest implements Displayable{

    private static final @NotNull Logger logger = LogManager.getLogger();

    public int i =  3;
    public @NotNull String s = "Hello, World!";
    public boolean bool = true;
    public @Nullable Double nullValue = null;
    public @NotNull List<String> stringList = List.of("Test", "String", "INGWER");


    @Test
    void testReceiveFieldsTest() {
        logger.info("testing for fields...");
        Assertions.assertArrayEquals(receiveFields().toArray(), this.getClass().getFields());
    }

    @Test
    void testToBoxedString() {
        logger.info(System.lineSeparator() +  toBoxedString());
    }

    @Test
    void testToDynamicBoxedString() {
        logger.info(toDynamicBoxedString());
    }

    @Test
    void testLogEmptyLine() {
        logEmptyLine();
    }
}