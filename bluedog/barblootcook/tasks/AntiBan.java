package bluedog.barblootcook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;

public class AntiBan extends Task {
    public AntiBan(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if(isCooking()){
            return getRand(-50, 1) > 0;
        }
        return false;
    }

    @Override
    public void execute() {
        int random_action = getRand(0, 3);
        switch (random_action) {
            case 1:
                System.out.println("Changing camera angle");
                changeCameraAngle();
                break;
            case 2:
                System.out.println("Hovering over skill");
                hoverOverSkill();
                break;
            case 3:
                System.out.println("Viewing nearby player");
                hoverOverRandomPlayer();
                break;
            default:
                break;
        }
    }


    private void changeCameraAngle() {
        ctx.camera.angle(getRand(1, 50));
    }

    public void hoverOverSkill() {
        ctx.widgets.component(161, 52).click();
        ctx.widgets.component(320, 17).hover();
        Condition.sleep(Random.nextInt(1000, 4000));
        ctx.widgets.component(161, 54).click();
    }

    private void hoverOverRandomPlayer(){
        int playerAmount = ctx.players.get().size();
        Player playerToHover = ctx.players.get().get(Random.nextInt(0, playerAmount));

        System.out.println("Trying to interact with: "+ playerToHover.name());
        if (!playerToHover.inViewport()){
            ctx.camera.turnTo(playerToHover);
        }
        if (playerToHover.valid()){
            playerToHover.hover();
//            Condition.sleep(Random.nextInt(2500, 4000));
        }
    }
}