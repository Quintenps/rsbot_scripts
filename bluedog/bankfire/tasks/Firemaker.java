package bluedog.bankfire.tasks;

import org.powerbot.bot.rt4.client.CollisionMap;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Objects;

import java.util.List;

public class Firemaker extends Task {

    private int LOGS_ID;

//    [9:04 PM] benpoulson: Check the collision map for tiles that are empty.
//    [9:04 PM] benpoulson: And missing the game object.

    private boolean availableTile() {
        return ctx.objects.select().at(ctx.players.local().tile()).poll().id() != 26185;
    }

    private void moveReset() {

    }

    public Firemaker(ClientContext ctx, int LOGS_ID) {
        super(ctx);
        this.LOGS_ID = LOGS_ID;
    }

    @Override
    public boolean activate() {

        System.out.println();
        return !ctx.players.local().inMotion() && ctx.players.local().animation() != 733 && availableTile();
    }

    @Override
    public void execute() {
//        if(!availableTile()){
//            moveReset();
//        }
//
//        Item log = ctx.inventory.select().id(LOGS_ID).poll();
//        Item tinderbox = ctx.inventory.select().id(590).poll();
//
//        tinderbox.click();
//        Condition.sleep(tasks.getRand(100, 500));
//        log.click();
//        Condition.sleep(tasks.getRand(750,2850));
    }
}
