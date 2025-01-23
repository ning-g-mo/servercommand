package cn.ningmo;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import ninja.leaping.configurate.ConfigurationNode;
import com.velocitypowered.api.command.CommandSource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.ArrayList;

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
        String inputServer = args[0];  // 移除toLowerCase()以保持中文输入
        
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

    @Override
    public List<String> suggest(final Invocation invocation) {
        if (invocation.arguments().length == 0 || invocation.arguments().length == 1) {
            // 返回所有服务器名称和别名
            List<String> suggestions = new ArrayList<>();
            String input = invocation.arguments().length == 0 ? "" : invocation.arguments()[0].toLowerCase();
            
            // 添加所有配置的别名
            config.getNode("server-aliases").getChildrenMap().keySet().stream()
                .map(Object::toString)
                .filter(key -> key.toLowerCase().startsWith(input))
                .forEach(suggestions::add);
            
            // 添加所有实际服务器名称
            server.getAllServers().stream()
                .map(s -> s.getServerInfo().getName())
                .filter(name -> name.toLowerCase().startsWith(input))
                .forEach(suggestions::add);
            
            return suggestions;
        }
        return List.of();
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(suggest(invocation));
    }
} 