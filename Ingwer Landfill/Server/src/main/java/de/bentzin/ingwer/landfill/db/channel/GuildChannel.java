package de.bentzin.ingwer.landfill.db.channel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-09
 */
@Entity
public class GuildChannel {

    @Id
    @OneToOne(optional = true)
    private @NotNull Channel channel;

    private @NotNull String topic;

    public GuildChannel(@NotNull Channel channel, @NotNull String topic) {
        this.channel = channel;
        this.topic = topic;
    }

    public GuildChannel() {
    }

    public @NotNull Channel getChannel() {
        return channel;
    }

    public void setChannel(@NotNull Channel channel) {
        this.channel = channel;
    }

    public @NotNull String getTopic() {
        return topic;
    }

    public void setTopic(@NotNull String topic) {
        this.topic = topic;
    }


}
