package cn.ningmo;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
    id = "servercommand",
    name = "ServerCommand",
    version = "1.0",
    authors = {"ningmo"}
)
public class ServerCommand {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private ConfigurationNode config;

    @Inject
    public ServerCommand(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // 加载配置
        loadConfig();
        
        // 注册命令
        server.getCommandManager().register("sc", new ServerCommandExecutor(server, config));
        server.getCommandManager().register("hub", new HubCommandExecutor(server, config.getNode("hub-server").getString("hub")));
        
        logger.info("ServerCommand 插件已加载");
    }

    private void loadConfig() {
        try {
            if (!Files.exists(dataDirectory)) {
                Files.createDirectory(dataDirectory);
            }

            File configFile = new File(dataDirectory.toFile(), "config.yml");
            if (!configFile.exists()) {
                try (InputStream is = getClass().getResourceAsStream("/config.yml")) {
                    Files.copy(is, configFile.toPath());
                }
            }

            config = YAMLConfigurationLoader.builder()
                .setPath(configFile.toPath())
                .build()
                .load();

        } catch (IOException e) {
            logger.error("无法加载配置文件", e);
        }
    }
} 