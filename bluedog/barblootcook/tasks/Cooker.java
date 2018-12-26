package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.*;

public class Cooker extends Task {

    private boolean hasUncookedFood() {
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

        if (ctx.players.local().tile().distanceTo(fire.tile()) > 5) {
            ctx.movement.step(fire);
        }

        Item food = ctx.inventory.select().id(RAW_SALMON, RAW_TROUT).poll();
        int itemInventoryCount = ctx.inventory.select().id(food.id()).count();
        System.out.println("Inventory: "+ food.name() + " ("+itemInventoryCount+")");

        food.interact("Use");
        fire.interact("Use", "Fire");
        Condition.wait(() -> ctx.widgets.component(270, 14).visible(), 1000, 5);
        ctx.widgets.component(270, 14).click();
        Condition.wait(() -> ctx.inventory.select().id(food.id()).count() == 0 || ctx.chat.canContinue(), 2500, itemInventoryCount);
    }
}
