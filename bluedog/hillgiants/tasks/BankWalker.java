package bluedog.hillgiants.tasks;

import bluedog.hillgiants.Walker;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class BankWalker extends Task {
    private final int FOOD_ID = 373;
    private final int AMOUNT_FOOD = 10;
    private final Walker walker = new Walker(ctx);
    public static final Tile[] pathToBank = {new Tile(3109, 9834, 0), new Tile(3113, 9836, 0), new Tile(3116, 9839, 0), new Tile(3116, 9843, 0), new Tile(3117, 9847, 0), new Tile(3116, 9851, 0), new Tile(3116, 3451, 0), new Tile(3120, 3451, 0), new Tile(3124, 3453, 0), new Tile(3127, 3456, 0), new Tile(3131, 3456, 0), new Tile(3135, 3458, 0), new Tile(3139, 3457, 0), new Tile(3143, 3457, 0), new Tile(3147, 3456, 0), new Tile(3151, 3456, 0), new Tile(3155, 3456, 0), new Tile(3157, 3460, 0), new Tile(3160, 3463, 0), new Tile(3163, 3466, 0), new Tile(3164, 3470, 0), new Tile(3164, 3474, 0), new Tile(3164, 3478, 0), new Tile(3164, 3482, 0), new Tile(3166, 3486, 0)};

    public BankWalker(ClientContext ctx) {
        super(ctx);
    }

    private boolean hasFood() {
        return ctx.inventory.select().id(FOOD_ID).count() == AMOUNT_FOOD;
    }

    private boolean hasNoFood() {
        return ctx.inventory.select().id(FOOD_ID).count() == 0;
    }

    @Override
    public boolean activate() {
        return hasNoFood() || (hasFood() && pathToBank[0].distanceTo(ctx.players.local()) > 20);
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if (hasNoFood()) {
                if(pathToBank[pathToBank.length -1].distanceTo(ctx.players.local()) < 5){
                    if(!ctx.bank.inViewport()){
                        ctx.camera.turnTo(ctx.bank.nearest());
                    }
                    if(ctx.bank.open()){
                        ctx.bank.depositInventory();
                        ctx.bank.withdraw(983, 1);
                        ctx.bank.withdraw(373, 10);
                    }
                }
                walker.walkPath(pathToBank);
            }
            if (hasFood()) {
                walker.walkPathReverse(pathToBank);
            }
        }
    }

}
