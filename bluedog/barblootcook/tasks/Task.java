package bluedog.barblootcook.tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.util.Random;

public abstract class Task extends ClientAccessor {

    final int RAW_TROUT = 335;
    final int RAW_SALMON = 331;
    private final Tile fire_location = new Tile(3105, 3432, 0);

    public Task(ClientContext ctx) {
        super(ctx);
    }

    public abstract boolean activate();

    public abstract void execute();

    boolean hasFullInventory() {
        return ctx.inventory.select().count() == 28;
    }

    boolean isCooking() {
        return ctx.players.local().animation() == 879;
    }

    void tooFarFromLocation() {
        if(ctx.players.local().tile().distanceTo(fire_location) > 50){
            ctx.movement.step(fire_location);
        }
    }


    public void toggleRun() {
        if (ctx.movement.energyLevel() >= getRand(25, 100)) {
            ctx.movement.running(true);
        }
    }

    public static int getRand(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
