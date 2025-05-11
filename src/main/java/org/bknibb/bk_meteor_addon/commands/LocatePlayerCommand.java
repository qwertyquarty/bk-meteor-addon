package org.bknibb.bk_meteor_addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.entity.Target;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.NametagUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec2f;
import org.bknibb.bk_meteor_addon.MineplayUtils;
import org.bknibb.bk_meteor_addon.modules.PlayerTracers;
import org.joml.Vector2f;
import org.joml.Vector3d;

import java.time.Duration;
import java.time.Instant;

public class LocatePlayerCommand extends Command {
    public LocatePlayerCommand() {
        super("locatePlayer", "Will temporarily show a tracer to the player for 5 seconds.");
    }

    private PlayerEntity targetPlayer;
    private boolean running = false;
    private Instant startTimer;
    private final Color playerColor = new Color(205, 205, 205, 127);
    private final long showTime = 5;
    private final Target target = Target.Body;
    private final boolean stem = true;

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerArgumentType.create())).executes(context -> {
            PlayerEntity player = PlayerArgumentType.get(context);
            targetPlayer = player;
            startTimer = Instant.now();
            if (!running) {
                running = true;
                MeteorClient.EVENT_BUS.subscribe(this);
            }
            return SINGLE_SUCCESS;
        });
    }
    @EventHandler
    private void onRender(Render3DEvent event) {
        if (Instant.now().isAfter(startTimer.plusSeconds(showTime))) {
            running = false;
            MeteorClient.EVENT_BUS.unsubscribe(this);
            return;
        }
        if (mc.options.hudHidden) return;

        Color color = PlayerUtils.getPlayerColor((targetPlayer), playerColor);

        double x = targetPlayer.prevX + (targetPlayer.getX() - targetPlayer.prevX) * event.tickDelta;
        double y = targetPlayer.prevY + (targetPlayer.getY() - targetPlayer.prevY) * event.tickDelta;
        double z = targetPlayer.prevZ + (targetPlayer.getZ() - targetPlayer.prevZ) * event.tickDelta;

        double height = targetPlayer.getBoundingBox().maxY - targetPlayer.getBoundingBox().minY;
        if (target == Target.Head) y += height;
        else if (target == Target.Body) y += height / 2;

        event.renderer.line(RenderUtils.center.x, RenderUtils.center.y, RenderUtils.center.z, x, y, z, color);
        if (stem) event.renderer.line(x, targetPlayer.getY(), z, x, targetPlayer.getY() + height, z, color);
    }
}
