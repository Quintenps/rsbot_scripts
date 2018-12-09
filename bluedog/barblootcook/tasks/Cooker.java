package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.*;

public class Cooker extends Task {

    private boolean hasUncookedFood(){
        return ctx.inventory.select().id(RAW_TROUT, RAW_SALMON).count() > 0;
    }

    public Cooker(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return hasFullInventory() && hasUncookedFood();
    }

    @Override
    public void execute() {
        GameObject fire = ctx.objects.select().name("Fire").nearest().poll();
        ctx.camera.turnTo(fire);

        if(ctx.players.local().tile().distanceTo(fire.tile()) > 5){
            ctx.movement.step(fire);
        }

        Item food = ctx.inventory.select().id(RAW_SALMON, RAW_TROUT).poll();

        food.interact("Use");
        fire.interact("Use", "Fire");
        Condition.sleep(getRand(400,800));
        ctx.widgets.component(270, 14).click();
        Condition.sleep(getRand(500,1200));

        Condition.wait(() -> ctx.players.local().animation() != 897, 5000, 10);

    }
}
