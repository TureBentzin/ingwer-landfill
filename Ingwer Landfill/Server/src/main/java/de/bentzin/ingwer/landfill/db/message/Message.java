package de.bentzin.ingwer.landfill.db.message;

import de.bentzin.ingwer.landfill.db.channel.Channel;
import de.bentzin.ingwer.landfill.db.user.Account;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ture Bentzin
 * @since 2023-08-09
 */
@Entity
@Table
public class Message {

    //META
    @Id
    private long id;

    @ManyToOne
    private @NotNull Channel channel;
    
    @ManyToOne
    private @NotNull Account sender;

    @Column(nullable = false)
    private @NotNull Date timestamp;

    //Content
    private @NotNull String message;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull Channel getChannel() {
        return channel;
    }

    public void setChannel(@NotNull Channel channel) {
        this.channel = channel;
    }

    public @NotNull Account getSender() {
        return sender;
    }

    public void setSender(@NotNull Account sender) {
        this.sender = sender;
    }

    public @NotNull Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Date when) {
        this.timestamp = when;
    }

    public @NotNull String getMessage() {
        return message;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }

    public Message(long id, @NotNull Channel channel, @NotNull Account sender, @NotNull Date timestamp, @NotNull String message) {
        this.id = id;
        this.channel = channel;
        this.sender = sender;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Message() {
    }

    public static @NotNull Message byLink(@NotNull String discordMessageLink) throws IllegalArgumentException {
        String regex = "^https:\\/\\/discord\\.com\\/channels\\/(\\d+|@me)\\/(\\d+|@me)\\/(\\d+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(discordMessageLink);

        if (matcher.matches()) {
            String[] ids = new String[2];
            final String
                    guildID = matcher.group(1),
                    channelID = matcher.group(2),
                    messageID = matcher.group(3);

            throw new RuntimeException("TODO");

        } else {
            throw new IllegalArgumentException("Malformed Discord link: " + discordMessageLink);
        }
    }


}
