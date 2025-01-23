package cn.ningmo;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Optional;

public class HubCommandExecutor implements SimpleCommand {
    private final ProxyServer server;
    private final String hubServerName;

    public HubCommandExecutor(ProxyServer server, String hubServerName) {
        this.server = server;
        this.hubServerName = hubServerName;
    }

    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("此命令只能由玩家执行！").color(NamedTextColor.RED));
            return;
        }

        Player player = (Player) invocation.source();
        Optional<RegisteredServer> hubServer = server.getServer(hubServerName);

        if (hubServer.isEmpty()) {
            player.sendMessage(Component.text("找不到大厅服务器！").color(NamedTextColor.RED));
            return;
        }

        player.createConnectionRequest(hubServer.get()).fireAndForget();
        player.sendMessage(Component.text("正在将您传送至大厅...").color(NamedTextColor.GREEN));
    }
} 