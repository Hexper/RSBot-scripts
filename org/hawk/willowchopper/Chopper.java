package org.jasper.willowchopper;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

/**
 * Created with IntelliJ IDEA.
 * User: JasperPC
 * Date: 8/4/13
 * Time: 5:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Manifest(authors = {"Hawk"}, name = "Draynor Willow Chopper", description = "Chops willow trees in Draynor village and banks the logs.")
public class Chopper extends PollingScript {

    private final int[] willow = {38616, 38627, 58006};
    private final int[] hatchet = {1359};

    @Override
    public void start() {

    }

    @Override
    public int poll() {
        final Player local = ctx.players.local();
        if (local.getAnimation() == -1 && ctx.backpack.select().count() < 28) {
            if (!ctx.objects.select().id(willow).isEmpty()) {
                for (final GameObject object : ctx.objects.nearest().first()) {
                    if(object.getLocation().distanceTo(local) > 7) {
                        if(ctx.movement.stepTowards(object)) {
                            return 500;
                        }
                    } else if (object.isValid()) {
                        if (object.interact("Chop")) {
                            return 1000;
                        }
                    } else {
                        ctx.camera.turnTo(object);
                        return 400;

                    }
                }
            }
        } else if (ctx.backpack.count() == 28) {
            final Tile bank = ctx.bank.getNearest().getLocation();
            if (bank.distanceTo(local) > 6) {
                if (ctx.movement.stepTowards(bank)) {
                    return 500;
                }
            } else {
                if (ctx.bank.open()) {
                    if (ctx.backpack.select().id(hatchet).isEmpty()) {
                        if (ctx.bank.depositInventory()) {
                            if (ctx.bank.close()) {
                                return 500;
                            }
                        }
                    } else {
                        main:
                        for (final Item item : ctx.backpack.getAllItems()) {
                            for(final Item hatchet : ctx.backpack) {
                                if(item.equals(hatchet)) {
                                    continue main;
                                }
                            }
                            if(ctx.bank.deposit(item.getId(), 0)) {
                                ctx.game.sleep(250, 500);
                            }
                        }
                    }
                }
            }
        }

        return 250;
    }
}
