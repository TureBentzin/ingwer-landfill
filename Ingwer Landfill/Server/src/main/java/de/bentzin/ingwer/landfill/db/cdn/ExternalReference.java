package de.bentzin.ingwer.landfill.db.cdn;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Date;

/**
 * This includes:
 *  Images
 *  Files
 *  "Big Textes"
 *  Audios
 *  -> Not PBs
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

    @Column(updatable = false)
    private @NotNull Date timestamp = new Date();

    public ExternalReference(@NotNull URL reference, @NotNull ReferenceType referenceType, @NotNull Date timestamp) {
        this.reference = reference;
        this.referenceType = referenceType;
        this.timestamp = timestamp;
    }

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

    public @NotNull Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NotNull Date timestamp) {
        this.timestamp = timestamp;
    }
}
