package de.bentzin.ingwer.landfill.db.cdn;

import de.bentzin.ingwer.landfill.db.message.Message;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author Ture Bentzin
 * @since 2023-08-09
 */
@Entity
@IdClass(References.ReferencesPK.class)
public class References {

    @Id
    @ManyToOne
    private @NotNull ExternalReference reference;

    @Id
    @ManyToOne
    private @NotNull Message message;


    public static class ReferencesPK implements Serializable {

        protected @NotNull ExternalReference reference;


        protected @NotNull Message message;

        public ReferencesPK(@NotNull ExternalReference reference, @NotNull Message message) {
            this.reference = reference;
            this.message = message;
        }

        public ReferencesPK() {
        }

        public @NotNull ExternalReference getReference() {
            return reference;
        }

        public void setReference(@NotNull ExternalReference reference) {
            this.reference = reference;
        }

        public @NotNull Message getMessage() {
            return message;
        }

        public void setMessage(@NotNull Message message) {
            this.message = message;
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (this == o) return true;
            if (!(o instanceof ReferencesPK that)) return false;

            if (!getReference().equals(that.getReference())) return false;
            return getMessage().equals(that.getMessage());
        }

        @Override
        public int hashCode() {
            int result = getReference().hashCode();
            result = 31 * result + getMessage().hashCode();
            return result;
        }
    }

}
