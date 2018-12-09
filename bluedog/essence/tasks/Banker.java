package bluedog.essence.tasks;

import bluedog.bankfire.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Objects;

public class Banker extends Task {

    private final Walker walker = new Walker(ctx);

    //public static final Tile[] pathToRuneEssence = {new Tile(3254, 3420, 0), new Tile(3254, 3425, 0), new Tile(3258, 3428, 0), new Tile(3260, 3423, 0), new Tile(3260, 3418, 0), new Tile(3260, 3413, 0), new Tile(3260, 3408, 0), new Tile(3259, 3403, 0), new Tile(3256, 3399, 0), new Tile(3253, 3403, 0), new Tile(9970, 6069, 0), new Tile(9967, 6064, 0), new Tile(9964, 6060, 0), new Tile(9963, 6055, 0)};
    private static final Tile[] pathToBank = {new Tile(3262, 3416, 0)};
    private static final Tile[] bankToAubury = {new Tile(3257, 3405, 0)};
    private static final Tile teleportedLocation = new Tile(12087, 6648, 0);
    private static final Tile essenceLocation[] = {new Tile(12072, 6667, 0)};

    public final int PORTAL_ID = 7473;
    private final int TELEPORT_NPC = 637;

    private boolean hasFullInventory() {
        return ctx.inventory.select().count() == 28;
    }

    private boolean hasEmptyInventory() {
        return ctx.inventory.select().count() == 0;
    }

    private boolean mineNearby() {
        return ctx.objects.select().name("Rune Essence").nearest().poll().tile().distanceTo(ctx.players.local().tile()) > 20;
    }

    public Banker(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (hasFullInventory() || (hasEmptyInventory() && mineNearby())) && !ctx.players.local().inMotion() && !ctx.players.local().interacting().valid();
    }

    @Override
    public void execute() {
        toggleRun();
        if (hasFullInventory()) {
            if (!ctx.bank.inViewport()) {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
            if (ctx.bank.open()) {
                ctx.bank.depositInventory();
                ctx.camera.pitch(99);
            }

            Npc portal = ctx.npcs.select().name("Portal").select().nearest().poll();
            System.out.println("Banker: full inventory");

            if (portal.tile().distanceTo(ctx.players.local().tile()) < 15) {
                System.out.println("Banker: trying to interact with portal");
                ctx.camera.turnTo(portal);
                if(!portal.interact("Use")){
                 portal.interact("Exit");
                }
                Condition.wait(() -> !ctx.players.local().inMotion() && !ctx.players.local().interacting().valid(), getRand(500,1000), 10);
            }
            else {
                ctx.movement.step(portal.tile());
            }

            walker.walkPath(pathToBank);
            Condition.wait(() -> !ctx.players.local().inMotion(), getRand(500,1000), 10);
        }

        if (hasEmptyInventory()) {
            System.out.println("Banker: empty inventory");
            if (ctx.npcs.select().id(TELEPORT_NPC).poll().tile().distanceTo(ctx.players.local().tile()) < 15) {
                System.out.println("Banker: Trying to teleport with Auby");
                Npc Aubery = ctx.npcs.select().id(TELEPORT_NPC).nearest().poll();
                Aubery.interact("Teleport");

                Condition.wait(() -> !ctx.players.local().inMotion() && !ctx.players.local().interacting().valid(), getRand(500,1000), 10);
            }
            walker.walkPath(bankToAubury);
            Condition.wait(() -> !ctx.players.local().inMotion(), getRand(500,1000), 10);

            if (ctx.players.local().tile().distanceTo(ctx.objects.select().name("Rune Essence").nearest().poll()) < 50){
                System.out.println("Banker: Out of location, trying to walk to mine");
                ctx.movement.step(ctx.objects.select().name("Rune Essence").nearest().poll());
            }
        }

    }
}
