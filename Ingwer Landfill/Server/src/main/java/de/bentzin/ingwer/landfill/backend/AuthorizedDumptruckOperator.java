package de.bentzin.ingwer.landfill.backend;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link AuthorizedDumptruckOperator} is the landfill designation for a user that has privileges in within the landfill protocol
 * @author Ture Bentzin
 * @since 2023-08-24
 */
@Table(name = "operator")
@Entity
public class AuthorizedDumptruckOperator {

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
}
