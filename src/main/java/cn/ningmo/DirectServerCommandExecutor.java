package cn.ningmo;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class DirectServerCommandExecutor implements SimpleCommand {
    private final ProxyServer server;
    private final String targetServer;

    public DirectServerCommandExecutor(ProxyServer server, String targetServer) {
        this.server = server;
        this.targetServer = targetServer;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("此命令只能由玩家执行！").color(NamedTextColor.RED));
            return;
        }

        Player player = (Player) invocation.source();
        Optional<RegisteredServer> server = this.server.getServer(targetServer);

        if (server.isEmpty()) {
            player.sendMessage(Component.text("找不到目标服务器！").color(NamedTextColor.RED));
            return;
        }

        player.createConnectionRequest(server.get()).fireAndForget();
        player.sendMessage(Component.text("正在传送中...").color(NamedTextColor.GREEN));
    }
} 