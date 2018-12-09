package bluedog.bankfire.tasks;

import bluedog.bankfire.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Banker extends Task {
    private final int LOGS_ID;
    private final int TINDERBOX_ID = 590;
    private final Walker walker = new Walker(ctx);
    public static final Tile[] pathToBank = {new Tile(3167, 3488, 0), new Tile(3167, 3491, 0), new Tile(3167, 3494, 0), new Tile(3166, 3497, 0), new Tile(3165, 3500, 0), new Tile(3165, 3503, 0), new Tile(3165, 3506, 0), new Tile(3165, 3509, 0)};

    public Banker(ClientContext ctx, int LOGS_ID) {
        super(ctx);
        this.LOGS_ID = LOGS_ID;
    }

    private boolean hasNoLogs() {
        return ctx.inventory.select().id(LOGS_ID).count() == 0;
    }

    private boolean hasWithdrawnLogs() {
        return ctx.inventory.select().id(LOGS_ID).count() == 27;
    }

    @Override
    public boolean activate() {
        return hasNoLogs();
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if (hasNoLogs()) {
                if (pathToBank[0].distanceTo(ctx.players.local()) < 3) {
                    if (!ctx.bank.inViewport()) {
                        ctx.camera.turnTo(ctx.bank.nearest());
                    }
                    if (ctx.bank.open()) {
                        Condition.sleep(getRand(200, 2000));
                        ctx.bank.withdraw(LOGS_ID, 27);
                    }
                }
                walker.walkPathReverse(pathToBank);
            }
            if (hasWithdrawnLogs()) {
                walker.walkPath(pathToBank);
            }
        }
    }

}
