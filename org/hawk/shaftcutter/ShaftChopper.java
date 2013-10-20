package org.hawk.shaftcutter;

import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.methods.Hud;
import org.powerbot.script.wrappers.Component;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Item;
import org.powerbot.script.wrappers.Tile;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 9/27/13
 * Time: 5:41 AM
 * To change this template use File | Settings | File Templates.
 */
@Manifest(authors = {"Hawk"}, name = "Chop 'n Shaft", description = "Chops logs and turns them into arrow shafts", version=0.1)
public class ShaftChopper extends PollingScript {

    private final int[] tree = {9354, 9366, 11866, 9355, 38760, 38783, 38785, 38787, 47596, 47594, 47600, 47598, 47591};
    private final int log = 1511;

    private Tile start;

    @Override
    public void start() {
        start = ctx.players.local().getLocation();
    }

    @Override
    public int poll() {
        if (ctx.players.local().getAnimation() != -1) {
            return 50;
        }

        if(!ctx.hud.isVisible(Hud.Window.BACKPACK)) {
            ctx.hud.view(Hud.Window.BACKPACK);
        }

        if (ctx.backpack.select().count() == 28) {
            final Component knife = ctx.widgets.get(1179, 33);
            if(knife.isValid() && knife.isOnScreen()) {
                if(knife.click()) {
                    ctx.backpack.sleep(1000, 1200);
                    return 50;
                }
            }
            final Component fletch = ctx.widgets.get(1370, 38);
            if (fletch.isValid() && fletch.isOnScreen()) {
                if (fletch.click()) {
                    ctx.backpack.sleep(1000, 1200);
                    return 50;
                }
            } else {
                for (final Item item : ctx.backpack.id(log).first()) {
                    if (item.click()) {
                        ctx.backpack.sleep(1000, 122);
                        return 50;
                    }
                }
            }
        } else {
            if (!ctx.objects.select().id(tree).within(start, 10).isEmpty()) {
                for (final GameObject object : ctx.objects.nearest().first()) {
                    if (object.isOnScreen()) {
                        if (object.interact("Chop")) {
                            ctx.backpack.sleep(500);
                            while(ctx.players.local().isInMotion()) {
                                ctx.camera.sleep(500);
                            }
                            return 50;
                        }
                    } else {
                        ctx.movement.stepTowards(object);
                        ctx.camera.turnTo(object);
                        ctx.backpack.sleep(1000, 1200);
                    }
                }
            }
        }
        return 0;
    }
}
