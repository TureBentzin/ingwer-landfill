package de.bentzin.ingwer.landfill.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author Ture Bentzin
 * @since 2023-08-24
 */
@Table
@Entity
public class WorkerID {

    @Id
    private @NotNull UUID workerID;

    public WorkerID() {
    }

    public WorkerID(@NotNull UUID workerID, @NotNull AuthorizedDumptruckOperator operator) {
        this.workerID = workerID;
        this.operator = operator;
    }

    public @NotNull UUID getWorkerID() {
        return workerID;
    }

    public void setWorkerID(@NotNull UUID workerID) {
        this.workerID = workerID;
    }

    @ManyToOne(optional = false)
    private @NotNull AuthorizedDumptruckOperator operator;

    public @NotNull AuthorizedDumptruckOperator getOperator() {
        return operator;
    }

    public void setOperator(@NotNull AuthorizedDumptruckOperator operator) {
        this.operator = operator;
    }
}
