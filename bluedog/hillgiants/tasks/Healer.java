package bluedog.hillgiants.tasks;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Healer extends Task {
    private final int FOOD_ID = 373;
    private Item food = ctx.inventory.select().id(FOOD_ID).poll();

    public Healer(ClientContext ctx) {
        super(ctx);
    }

    private void focusInventory() {
        ctx.widgets.component(161, 54).click();
    }

    @Override
    public boolean activate() {
        return ctx.combat.healthPercent() < 50 && food.valid();
    }

    @Override
    public void execute() {
        focusInventory();
        food.click();
    }
}
