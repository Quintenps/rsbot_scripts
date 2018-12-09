package bluedog.hillgiants.tasks;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

public class Looter extends Task {
    private final int ITEM_TO_LOOT = 888;

    public Looter(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {
        GroundItem toloot = ctx.groundItems.select().id(ITEM_TO_LOOT).nearest().poll();
        while (toloot.valid()) {
            toloot.interact("Take");
            System.out.println(toloot.valid());
        }
    }
}
