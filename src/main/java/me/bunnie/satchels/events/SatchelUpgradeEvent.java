package me.bunnie.satchels.events;

import lombok.Getter;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.satchel.upgrade.UpgradeType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class SatchelUpgradeEvent extends Event  {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Satchel satchel;
    private final UpgradeType upgrade;

    public SatchelUpgradeEvent(Player player, Satchel satchel, UpgradeType upgrade) {
        this.player = player;
        this.satchel = satchel;
        this.upgrade = upgrade;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }


    public static HandlerList getHandlerList() {
        return handlers;
    }

}
