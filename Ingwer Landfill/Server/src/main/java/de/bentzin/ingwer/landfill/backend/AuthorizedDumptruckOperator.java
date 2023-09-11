package de.bentzin.ingwer.landfill.backend;

import de.bentzin.ingwer.landfill.backend.priviliges.Privilege;
import de.bentzin.ingwer.landfill.netty.util.FingerprintHolder;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * {@link AuthorizedDumptruckOperator} is the landfill designation for a user that has privileges in within the landfill protocol
 * @author Ture Bentzin
 * @since 2023-08-24
 */
@Table(name = "operator")
@Entity
public class AuthorizedDumptruckOperator implements FingerprintHolder {

    public AuthorizedDumptruckOperator(@NotNull String designation, @NotNull List<WorkerID> workers, @NotNull String publicKey) {
        this.designation = designation;
        this.workers = workers;
        this.publicKey = publicKey;
    }

    public AuthorizedDumptruckOperator() {
    }

    @Id
    @Column(length = 32, nullable = false)
    private @NotNull String designation;

    @OneToMany(mappedBy = "operator")
    private @NotNull List<WorkerID> workers;

    @Column(nullable = false, unique = true)
    private @NotNull String publicKey;


    public @NotNull String getDesignation() {
        return designation;
    }

    public void setDesignation(@NotNull String designation) {
        this.designation = designation;
    }

    public @NotNull List<WorkerID> getWorkers() {
        return workers;
    }

    public void setWorkers(@NotNull List<WorkerID> workers) {
        this.workers = workers;
    }

    public @NotNull String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(@NotNull String publicKey) {
        this.publicKey = publicKey;
    }

    @ManyToMany(mappedBy = "privileged")
    private @NotNull Set<Privilege> privileges;

    public @NotNull Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(@NotNull Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public final @NotNull CharSequence humanReadableFingerprint() {
        return getPublicKey();
    }

    @Override
    public @NotNull String toString() {
        return designation;
    }
}
