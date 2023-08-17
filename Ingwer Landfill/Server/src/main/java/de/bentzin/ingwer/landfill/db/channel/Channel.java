package de.bentzin.ingwer.landfill.db.channel;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.DisplaynameField;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@Table
public class Channel implements Displayable {

    /*
    Speacial tables for:
    DMs
    |-Groups?
    Threads
    GuildChannels
    https://discord.com/developers/docs/resources/channel#channels-resource
     */
    /**
     * Channel ID
     */
    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    private @NotNull ChannelType type;

    @DisplaynameField
    private @NotNull String name;

    public Channel(long id, @NotNull ChannelType type, @NotNull String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public Channel() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull ChannelType getType() {
        return type;
    }

    public void setType(@NotNull ChannelType type) {
        this.type = type;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }
}
