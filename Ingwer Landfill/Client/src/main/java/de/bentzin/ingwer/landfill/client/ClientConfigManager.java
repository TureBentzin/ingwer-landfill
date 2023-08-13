package de.bentzin.ingwer.landfill.client;

import de.bentzin.ingwer.landfill.properties.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Ture Bentzin
 * @since 2023-08-13
 */
public class ClientConfigManager {

    private static final @NotNull Logger logger = LogManager.getLogger();
    private final @NotNull Properties clientDefaults = new Properties();
    private final @NotNull PropertyUtils propertyUtils;
    private final @NotNull Properties clientConf;
    public ClientConfigManager() {
        try {
            populateDefaults();
            ensureFile();
            clientConf = (Properties) clientDefaults.clone();
            propertyUtils = new PropertyUtils(clientConf);
            Properties properties = new Properties();
            properties.load(new FileInputStream(configFile()));
            new PropertyUtils(properties).dumpTable();
            for (Map.Entry<Object, Object> objectObjectEntry : properties.entrySet()) {
                clientConf.setProperty(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
            }
            save();
            propertyUtils.dumpTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull File configFile() {
        String property = System.getProperty("landfill.client.config", "client.properties");
        return new File(property);
    }

    private void populateDefaults() {
        clientDefaults.put("hostname", "localhost");
        clientDefaults.put("port", "2222");
        clientDefaults.put("privateKeyFile", "private.key");
        clientDefaults.put("certFile", "cert.crt");
        clientDefaults.put("workerID", UUID.randomUUID().toString());
        logger.debug("defaults have been populated: " + clientDefaults);
    }

    private void ensureFile() {
        try {
            File file = configFile();
            if (file.createNewFile()) {
                logger.info("Config created: " + file.getName());
                ((Properties) clientDefaults.clone()).store(new FileOutputStream(configFile()), "Please adjust all values to your preference!");
            }
            logger.info("config file is ready: " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("An error occurred while ensuring persistence of the config: " + e.getMessage());
        }
    }

    public void save() throws IOException {
        logger.info("saving configuration...");
        getClientConf().store(new FileOutputStream(configFile()),
                "Ingwer-landfill-client-configuration : NEVER SHARE YOUR PRIVATE KEY FILE!!");
    }

    public @NotNull Properties getClientConf() {
        return clientConf;
    }

    public @NotNull String getHostname() {
        return propertyUtils.get("hostname").orElseThrow();
    }

    public int getPort() {
        return Integer.parseInt(propertyUtils.get("port").orElseThrow());
    }

    public @NotNull File getPrivateKey() {
        return new File(propertyUtils.get("privateKeyFile").orElseThrow());
    }

    public @NotNull File getCert() {
        return new File(propertyUtils.get("certFile").orElseThrow());
    }

    public @NotNull String getWorkerID() {
        return propertyUtils.get("workerID").orElseThrow();
    }


}
