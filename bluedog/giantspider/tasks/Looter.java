package bluedog.giantspider.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

public class Looter extends Task {
    private final int ARROWS = 888;

    private GroundItem nearbyArrows(){
        return ctx.groundItems.select().select().id(888).poll();
    }

    public Looter(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().interacting().interacting().equals (ctx.players.local()) && nearbyArrows().valid();
    }

    @Override
    public void execute() {
        ctx.camera.turnTo(nearbyArrows());
        nearbyArrows().interact("Take", nearbyArrows().name());

        Condition.wait(() -> ctx.players.local().inMotion(), 200, 10);
    }
}
