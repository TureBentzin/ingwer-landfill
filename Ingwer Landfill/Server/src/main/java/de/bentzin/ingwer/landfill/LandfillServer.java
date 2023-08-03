package de.bentzin.ingwer.landfill;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class LandfillServer implements Runnable{

    @NotNull
    public static final LandfillServer LANDFILL_SERVER = new LandfillServer();

    public static @NotNull LandfillServer start() throws IllegalStateException{
        LANDFILL_SERVER.run();
        return LANDFILL_SERVER;
    }

    @Override
    public void run() throws IllegalStateException{
        if(System.getProperties().containsKey("landfill.started"))
            throw new IllegalStateException("Landfill seems to have already started in this VM!");
        System.setProperty("landfill.started", "1");
    }
}
