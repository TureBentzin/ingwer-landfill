package de.bentzin.ingwer.landfill.backend.priviliges;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.intellij.lang.annotations.Pattern;
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
    @Pattern("^[A-Z_]+$")
    private @NotNull String identifier;

    public Privilege() {
    }

    //I have no idea how @Subst works time to create a scratch file i guess
    public Privilege(@Subst("ERROR_PRIVILEGE") @Pattern("^[A-Z_]+$") @NotNull String identifier) {
        this.identifier = identifier;
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotNull String identifier) {
        this.identifier = identifier;
    }
}
