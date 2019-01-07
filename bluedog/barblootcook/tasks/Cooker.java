package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

public class Cooker extends Task {

    private final Tile permanentFireTile = new Tile(3106, 3432, 0);

    private boolean hasUncookedFood() {
        return ctx.inventory.select().id(RAW_TROUT, RAW_SALMON).count() > 0;
    }

    public Cooker(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return hasFullInventory() && hasUncookedFood() && !ctx.players.local().inMotion();
    }

    @Override
    public void execute() {
        if (ctx.players.local().tile().distanceTo(permanentFireTile) > 3) {
            ctx.movement.step(permanentFireTile);
        }
        Condition.wait(() -> !ctx.players.local().inMotion(), getRand(250,750), 5);
        GameObject fire = ctx.objects.select().name("Fire").nearest().poll();
        ctx.camera.turnTo(fire);

        Item food = ctx.inventory.select().id(RAW_SALMON, RAW_TROUT).poll();
        int itemInventoryCount = ctx.inventory.select().id(food.id()).count();
        System.out.println("Inventory: " + food.name() + " (" + itemInventoryCount + ")");

        food.interact("Use");
        fire.interact("Use", "Fire");
        Condition.wait(() -> ctx.widgets.component(270, 14).visible(), 1000, 5);
        ctx.widgets.component(270, 14).click();
        Condition.wait(() -> ctx.inventory.select().id(food.id()).count() == 0 || ctx.chat.canContinue(), 2500, itemInventoryCount);
    }
}
