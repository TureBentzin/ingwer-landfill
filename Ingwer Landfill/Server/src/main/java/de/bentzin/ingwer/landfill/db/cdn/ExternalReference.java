package de.bentzin.ingwer.landfill.db.cdn;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * This includes:
 * Images
 * Files
 * "Big Textes"
 * Audios
 * -> Not PBs
 *
 * --> Attachments
 *
 * @author Ture Bentzin
 * @since 2023-08-09
 */
@Entity
public class ExternalReference {

    @Id
    private @NotNull URL reference;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private @NotNull ReferenceType referenceType = ReferenceType.UNKNOWN;

    public ExternalReference(@NotNull URL reference, @NotNull ReferenceType referenceType) {
        this.reference = reference;
        this.referenceType = referenceType;
    }

    public ExternalReference(@NotNull URL reference) {
        this.reference = reference;
    }

    public ExternalReference() {
    }

    public @NotNull URL getReference() {
        return reference;
    }

    public void setReference(@NotNull URL reference) {
        this.reference = reference;
    }

    public @NotNull ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(@NotNull ReferenceType referenceType) {
        this.referenceType = referenceType;
    }
}
