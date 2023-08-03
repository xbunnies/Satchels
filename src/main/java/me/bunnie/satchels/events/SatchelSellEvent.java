package me.bunnie.satchels.events;

import lombok.Getter;
import me.bunnie.satchels.satchel.Satchel;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SatchelSellEvent extends Event  {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Satchel satchel;

    public SatchelSellEvent(Player player, Satchel satchel) {
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
