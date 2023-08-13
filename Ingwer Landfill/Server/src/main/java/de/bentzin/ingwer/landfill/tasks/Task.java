package de.bentzin.ingwer.landfill.tasks;

import de.bentzin.ingwer.landfill.db.channel.Channel;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public interface Task {

    default int jobID() {
        return 0;
    }

    void execute(@NotNull Session session, @NotNull Channel channel);
}
