package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

public class Looter extends Task {

    public int fishLooted = 0;

    private GroundItem getGroundLoot(){
        return ctx.groundItems.select().id(RAW_SALMON, RAW_TROUT).nearest().poll();
    }

    public Looter(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !hasFullInventory();
    }

    @Override
    public void execute() {
        tooFarFromLocation();
        toggleRun();

        GroundItem loot = getGroundLoot();
        if(!loot.inViewport()){
            ctx.camera.turnTo(loot);
        }
        if(ctx.players.local().tile().distanceTo(loot.tile()) > 8){
            ctx.movement.step(loot);
        }
        loot.interact("Take", loot.name());
        fishLooted++;
        Condition.sleep(getRand(100,600));

    }
}
