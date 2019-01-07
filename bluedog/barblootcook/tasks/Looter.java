package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

public class Looter extends Task {

    private GroundItem getGroundLoot() {
        return ctx.groundItems.select().id(RAW_SALMON, RAW_TROUT).nearest().poll();
    }

    private void turnCameraToInterest(GroundItem loot) {
        if (!loot.inViewport()) {
            ctx.camera.turnTo(loot);
        }
    }

    private void seeIfFishAvailable(GroundItem loot) {
        if (!loot.valid()) {
            System.out.println("No dropped fish in sight");
            return;
        }
    }

    private void moveToItem(GroundItem loot) {
        if (ctx.players.local().tile().distanceTo(loot.tile()) > 2) {
            ctx.movement.step(loot);
            Condition.wait(() -> !ctx.players.local().inMotion(), 750, 3);
        }
    }

    public Looter(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !hasFullInventory() && !ctx.players.local().inMotion();
    }

    @Override
    public void execute() {
        tooFarFromLocation();
        toggleRun();

        GroundItem loot = getGroundLoot();
        seeIfFishAvailable(loot);
        turnCameraToInterest(loot);
        moveToItem(loot);
        loot.interact("Take", loot.name());
    }

}
