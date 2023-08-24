package de.bentzin.ingwer.landfill.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link AuthorizedDumptruckOperator} is the landfill designation for a user that has privileges in within the landfill protocol
 * @author Ture Bentzin
 * @since 2023-08-24
 */
@Table
@Entity
public class AuthorizedDumptruckOperator {

    @Id
    private @NotNull String designation;

    @OneToMany
    private @NotNull List<WorkerID> workers;

    private @NotNull String public_key;


}
