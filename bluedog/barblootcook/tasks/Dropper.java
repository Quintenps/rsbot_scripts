package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Dropper extends Task {

    private boolean hasInventoryFull() {
        return ctx.inventory.select().count() > 0 && ctx.inventory.select().id(RAW_SALMON, RAW_TROUT).count() == 0;
    }

    public Dropper(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return hasInventoryFull();
    }

    @Override
    public void execute() {
        Item[] inventoryItem = ctx.inventory.items();
        for (Item curItem : inventoryItem) {
            curItem.interact("Drop");
        }

        Condition.sleep(getRand(100, 400));
    }
}
