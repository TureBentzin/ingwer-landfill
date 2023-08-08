package de.bentzin.ingwer.landfill.db.channel;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ture Bentzin
 * @since 2023-08-08
 */
@Entity
@Table
public class Channel {

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
    private ChannelType type;

    private String name;

}
