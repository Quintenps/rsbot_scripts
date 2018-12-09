package bluedog.hillgiants;


import bluedog.hillgiants.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Script.Manifest(name = "bluedog's hillgiants", description = "My first script", properties = "author=bluedog; topic=118146; client=4;")

public class Main extends PollingScript<ClientContext> implements PaintListener {
    List<Task> tasks = new ArrayList<Task>();

    int STRENGTH_LVL = ctx.skills.level(Constants.SKILLS_STRENGTH);
    int ATTACK_LVL = ctx.skills.level(Constants.SKILLS_ATTACK);
    int DEFENCE_LVL = ctx.skills.level(Constants.SKILLS_DEFENSE);
    int HITPPOINTS_LVL = ctx.skills.level(Constants.SKILLS_HITPOINTS);

    private String getRunningTime() {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void start() {
        BankWalker bankwalk = new BankWalker(ctx);
        Healer heal = new Healer(ctx);
        AntiBan antiban = new AntiBan(ctx);
        Combat combat = new Combat(ctx);

        tasks.add(bankwalk);
        tasks.add(heal);
        tasks.add(antiban);
        tasks.add(combat);
    }

    @Override
    public void poll() {
        for (Task currentTask : tasks) {
            System.out.println("tasks: " + currentTask.toString());
            if (ctx.controller.isStopping()) {
                break;
            }
            if (currentTask.activate()) {
                System.out.println("Executing: " + currentTask.toString());
                currentTask.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics; //get 2d graphics

        g.drawString("bluedog's hill giant", 20, 20);
        g.drawString("Time : " + getRunningTime(), 20, 40);
        g.drawString("Strength: " + (ctx.skills.level(Constants.SKILLS_STRENGTH) - STRENGTH_LVL), 20, 60);
        g.drawString("Attack: " + (ctx.skills.level(Constants.SKILLS_ATTACK) - ATTACK_LVL), 20, 80);
        g.drawString("Defence: " + (ctx.skills.level(Constants.SKILLS_DEFENSE) - DEFENCE_LVL), 20, 100);
        g.drawString("Hitpoints: " + (ctx.skills.level(Constants.SKILLS_HITPOINTS) - HITPPOINTS_LVL), 20, 120);
    }
}
