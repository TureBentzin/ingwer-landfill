package de.bentzin.ingwer.landfill.netty;

import de.bentzin.ingwer.landfill.Displayable;
import io.netty5.buffer.Buffer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public interface Packet extends Displayable {

    void encode(@NotNull Buffer buffer);
}
