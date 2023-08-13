package de.bentzin.ingwer.landfill.db.reactions;

import de.bentzin.ingwer.landfill.Displayable;
import de.bentzin.ingwer.landfill.db.guild.Guild;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
@Entity
@Table
public class GuildEmoji implements Displayable {

    @Id
    @ManyToOne
    private @NotNull Emoji emoji;

    @ManyToOne
    private @NotNull Guild guild;

    public GuildEmoji(@NotNull Emoji emoji, @NotNull Guild guild) {
        this.emoji = emoji;
        this.guild = guild;
    }

    public GuildEmoji() {

    }

    public @NotNull Emoji getEmoji() {
        return emoji;
    }

    public void setEmoji(@NotNull Emoji emoji) {
        this.emoji = emoji;
    }

    public @NotNull Guild getGuild() {
        return guild;
    }

    public void setGuild(@NotNull Guild guild) {
        this.guild = guild;
    }
}
