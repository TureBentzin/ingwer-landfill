package de.bentzin.ingwer.landfill.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    public WorkerID(@NotNull UUID workerID) {
        this.workerID = workerID;
    }

    public @NotNull UUID getWorkerID() {
        return workerID;
    }

    public void setWorkerID(@NotNull UUID workerID) {
        this.workerID = workerID;
    }
}
