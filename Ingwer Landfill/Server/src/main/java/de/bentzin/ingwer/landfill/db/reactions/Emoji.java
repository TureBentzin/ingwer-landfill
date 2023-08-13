package de.bentzin.ingwer.landfill.db.reactions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
@Entity
@Table
public class Emoji {

    @Id
    @Pattern("^:.+:$")
    private @NotNull String id;

    @Column(nullable = false)
    private @NotNull URL asset;

    public Emoji(@NotNull String id, @NotNull URL asset) {
        this.id = id;
        this.asset = asset;
    }

    public Emoji() {
    }

    public @NotNull String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public @NotNull URL getAsset() {
        return asset;
    }

    public void setAsset(@NotNull URL asset) {
        this.asset = asset;
    }

}
