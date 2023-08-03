package me.bunnie.satchels.events;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SatchelCollectEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Material material;
    private final Event event;

    public SatchelCollectEvent(Player player, Material material, Event event) {
        this.player = player;
        this.material = material;
        this.event = event;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

}
