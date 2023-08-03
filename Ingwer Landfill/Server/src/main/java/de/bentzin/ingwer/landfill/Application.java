package de.bentzin.ingwer.landfill;

import org.jetbrains.annotations.NotNull;

/**
 * Entrypoint of Application
 * @author Ture Bentzin
 * @since 2023-08-03
 */
public class Application {

    public static void main(String @NotNull [] args) {
        LandfillServer.start();
    }
}
