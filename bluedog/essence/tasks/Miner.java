package bluedog.essence.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;


public class Miner extends Task {

    private final int ESSENCE_ID = 1436;


    private boolean hasFullInventory() {
        return ctx.inventory.select().id(ESSENCE_ID).count() == 28;
    }

    private boolean notMining() {
        return ctx.players.local().animation() != 624;
    }

    private boolean mineNearby() {
        GameObject essenceMine = ctx.objects.select().name("Rune Essence").nearest().poll();
        return essenceMine.tile().distanceTo(ctx.players.local().tile()) < 20;
    }

    public GameObject getNearbyMine() {
        return ctx.objects.select().name("Rune Essence").nearest().poll();
    }


    public Miner(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !hasFullInventory() && notMining() && mineNearby();
    }

    @Override
    public void execute() {
        GameObject essenceMine = getNearbyMine();
        ctx.camera.pitch(40);
        if (ctx.players.local().tile().distanceTo(essenceMine.tile()) > 5) {
            ctx.movement.step(essenceMine.tile());
        }
        if (!essenceMine.inViewport()) {
            ctx.camera.turnTo(essenceMine);
        }
        essenceMine.click();
        Condition.wait(() -> !notMining(), 200, 10);
    }
}
