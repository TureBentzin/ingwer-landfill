package de.bentzin.ingwer.landfill.backend.priviliges;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-24
 */
@Entity
@Table
public class Privilege {

    @Id
    //@Pattern(regexp = "^[A-Z_]+$", message = "The identifier must be valid UPPER_SNAKE_CASE")
    private @NotNull String identifier;
    private boolean enabled;

    public Privilege() {
        enabled = true;
    }

    //I have no idea how @Subst works time to create a scratch file, I guess
    //ok its wierd
    public Privilege(@NotNull String identifier, boolean enabled) {
        this.identifier = identifier;
        this.enabled = enabled;
    }

    public Privilege(@NotNull String identifier) {
        this.identifier = identifier;
        this.enabled = true;
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotNull String identifier) {
        this.identifier = identifier;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
