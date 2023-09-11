package de.bentzin.ingwer.landfill.netty;

import io.netty5.buffer.Buffer;
import io.netty5.buffer.BufferAllocator;
import io.netty5.buffer.BufferUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Ture Bentzin
 * @since 2023-08-14
 */
public class BufferUtilsTest {

    private static final @NotNull Logger logger = LogManager.getLogger();

    @Test
    void calculateChecksum() {
        BufferAllocator bufferAllocator = BufferAllocator.offHeapPooled();
        try (Buffer buffer = bufferAllocator.allocate(100)) {
            BufferUtils.encodeString(buffer, "This is a test String");
            String s = BufferUtil.hexDump(buffer);
            long l = BufferUtils.calculateChecksum(buffer);
            logger.info(s);
            logger.info("calculated checksum of: " + l);
            assertEquals(l, 643268830L);
        }
        bufferAllocator.close();
    }
}