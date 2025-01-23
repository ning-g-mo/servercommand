package cn.ningmo;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.Optional;

public class ServerCommandExecutor implements SimpleCommand {
    private final ProxyServer server;
    private final ConfigurationNode config;

    public ServerCommandExecutor(ProxyServer server, ConfigurationNode config) {
        this.server = server;
        this.config = config;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("此命令只能由玩家执行！").color(NamedTextColor.RED));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length != 1) {
            invocation.source().sendMessage(Component.text("用法: /sc <服务器>").color(NamedTextColor.RED));
            return;
        }

        Player player = (Player) invocation.source();
        String inputServer = args[0].toLowerCase();
        
        // 检查是否有别名
        String serverName = config.getNode("server-aliases", inputServer).getString(inputServer);
        Optional<RegisteredServer> targetServer = server.getServer(serverName);

        if (targetServer.isEmpty()) {
            player.sendMessage(Component.text("找不到目标服务器！").color(NamedTextColor.RED));
            return;
        }

        player.createConnectionRequest(targetServer.get()).fireAndForget();
        player.sendMessage(Component.text("正在将您传送至 " + inputServer + " 服务器...").color(NamedTextColor.GREEN));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("servercommand.use");
    }
} 