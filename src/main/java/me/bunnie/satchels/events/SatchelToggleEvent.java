package me.bunnie.satchels.events;

import lombok.Getter;
import me.bunnie.satchels.satchel.Satchel;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class SatchelToggleEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Satchel satchel;

    public SatchelToggleEvent(Player player, Satchel satchel) {
        this.player = player;
        this.satchel = satchel;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }
}
