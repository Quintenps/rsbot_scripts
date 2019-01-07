package bluedog.giantspider.tasks;

import bluedog.giantspider.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Objects;

public class Banker extends Task {
    private final Walker walker = new Walker(ctx);
    private String currentState = "TeleportDown";

    public static final Tile[] pathBankToSpiders = {new Tile(3093, 3491, 0), new Tile(3090, 3491, 0), new Tile(3087, 3489, 0), new Tile(3084, 3486, 0), new Tile(3081, 3483, 0), new Tile(3081, 3480, 0), new Tile(3080, 3477, 0), new Tile(3080, 3474, 0), new Tile(3080, 3471, 0), new Tile(3080, 3468, 0), new Tile(3083, 3467, 0), new Tile(3086, 3464, 0), new Tile(3086, 3461, 0), new Tile(3086, 3458, 0), new Tile(3086, 3455, 0), new Tile(3086, 3452, 0), new Tile(3086, 3449, 0), new Tile(3086, 3446, 0), new Tile(3086, 3443, 0), new Tile(3086, 3440, 0), new Tile(3087, 3437, 0), new Tile(3088, 3434, 0), new Tile(3088, 3431, 0), new Tile(3088, 3428, 0), new Tile(3085, 3425, 0), new Tile(3082, 3423, 0), new Tile(1859, 5243, 0), new Tile(1863, 5239, 0), new Tile(1914, 5222, 0), new Tile(1911, 5222, 0), new Tile(1908, 5221, 0), new Tile(1905, 5221, 0), new Tile(2042, 5245, 0), new Tile(2040, 5240, 0), new Tile(2021, 5223, 0), new Tile(2023, 5220, 0), new Tile(2123, 5252, 0), new Tile(2126, 5252, 0), new Tile(2129, 5253, 0), new Tile(2132, 5255, 0), new Tile(2132, 5258, 0), new Tile(2132, 5261, 0), new Tile(2127, 5270, 0)};
    public static final Tile[] pathToCaveEntrance = {new Tile(3093, 3491, 0), new Tile(3090, 3491, 0), new Tile(3087, 3489, 0), new Tile(3084, 3486, 0), new Tile(3081, 3483, 0), new Tile(3081, 3480, 0), new Tile(3080, 3477, 0), new Tile(3080, 3474, 0), new Tile(3080, 3471, 0), new Tile(3080, 3468, 0), new Tile(3083, 3467, 0), new Tile(3086, 3464, 0), new Tile(3086, 3461, 0), new Tile(3086, 3458, 0), new Tile(3086, 3455, 0), new Tile(3086, 3452, 0), new Tile(3086, 3449, 0), new Tile(3086, 3446, 0), new Tile(3086, 3443, 0), new Tile(3087, 3440, 0), new Tile(3089, 3437, 0), new Tile(3089, 3434, 0), new Tile(3089, 3431, 0), new Tile(3088, 3428, 0), new Tile(3085, 3426, 0), new Tile(3082, 3425, 0), new Tile(3080, 3422, 0)};
    public static final Tile[] pathFromSpidersToPortal = {new Tile(2132, 5262, 0), new Tile(2132, 5259, 0), new Tile(2132, 5256, 0), new Tile(2129, 5255, 0), new Tile(2126, 5255, 0), new Tile(2123, 5255, 0)};

    public static final Tile strongHold_floor1_start = new Tile(1859, 5243, 0);
    public static final Tile strongHold_floor1_end = new Tile(1914, 5222, 0);
    public static final Tile strongHold_floor_2_start = new Tile(2024, 5245, 0);
    public static final Tile strongHold_floor_2_end = new Tile(2021, 5223, 0);


    public Banker(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        return (hasFullInventory() && ctx.players.local().tile().distanceTo(pathBankToSpiders[pathBankToSpiders.length - 1]) > 10  || hasEmptyInventory()) && !ctx.players.local().inMotion();
    }

    private boolean hasEmptyInventory() {
        return ctx.inventory.select().name("Lobster").count() == 0;
    }

    private boolean hasFullInventory() {
        return ctx.inventory.select().name("Lobster").count() == 28;
    }

    private void moveCameraAndWalk(GameObject object) {
        if (!object.inViewport()) {
            ctx.camera.turnTo(object);
        }
        if (ctx.players.local().tile().distanceTo(object.tile()) > 10) {
            ctx.movement.step(object.tile());
        }
    }

    /**
     * Interact with object (Teleport/ladder/etc) to climb up/down
     */
    private void interactObject(String name, String interaction, Integer objectId) {
        System.out.println("[Banker] Climbing down: " + name);
        GameObject interactionObject = ctx.objects.select().id(objectId).poll();
        moveCameraAndWalk(interactionObject);
        interactionObject.interact(interaction, interactionObject.name());
        Condition.wait(() -> !ctx.players.local().inMotion(), 1500, 25);
    }


    @Override
    public void execute() {
        if (hasEmptyInventory()) {
            System.out.println("[Banker] Empty inventory");
            if (!ctx.bank.inViewport()) {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
            if (ctx.bank.open()) {
                ctx.bank.withdraw(379, 28);
            }
            walker.walkPathReverse(pathBankToSpiders);
            Condition.wait(() -> !ctx.players.local().inMotion(), 500, 10);
        } else {
            walker.walkPath(pathBankToSpiders);
            Condition.wait(() -> !ctx.players.local().inMotion(), 500, 10);
        }
    }
}
