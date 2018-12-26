package bluedog.fleshcrawler.tasks;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Healer extends Task {
    private final int FOOD_ID = 373;
    private int foodCount = ctx.inventory.select().id(FOOD_ID).count();

    public Healer(ClientContext ctx) {
        super(ctx);
    }

    private void focusInventory() {
        if(!ctx.widgets.component(161, 54).visible()) {
            ctx.widgets.component(161, 54).click();
        }
    }

    @Override
    public boolean activate() {
        return ctx.combat.healthPercent() < getRand(30,55) && foodCount > 0;
    }

    @Override
    public void execute() {
        Item food = ctx.inventory.select().id(FOOD_ID).poll();
        focusInventory();
        food.click();
    }
}
