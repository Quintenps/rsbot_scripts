package bluedog.giantspider.tasks;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Healer extends Task {
    private final int FOOD_ID = 379;

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
        int foodCount = ctx.inventory.select().id(FOOD_ID).count();
        int number = getRand(30,80);
        System.out.println(foodCount);
        System.out.println(ctx.combat.healthPercent() < number);
        return ctx.combat.healthPercent() < number  && foodCount > 0;
    }

    @Override
    public void execute() {
        Item food = ctx.inventory.select().id(FOOD_ID).poll();
        focusInventory();
        food.click();
    }
}
