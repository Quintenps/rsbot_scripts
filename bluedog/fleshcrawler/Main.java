package bluedog.fleshcrawler;

import bluedog.fleshcrawler.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "bluedog's fleshcrawler", description = "", properties = "author=bluedog; topic=118146; client=4;")

public class Main extends PollingScript<ClientContext> implements PaintListener {
    List<Task> tasks = new ArrayList<Task>();

    private final int LVL_ATTACK_START = ctx.skills.level(Constants.SKILLS_ATTACK);
    private final int LVL_STRENGTH_START = ctx.skills.level(Constants.SKILLS_STRENGTH);
    private final int LVL_DEFENCE_START = ctx.skills.level(Constants.SKILLS_DEFENSE);
    private final int LVL_HITPOINTS_START = ctx.skills.level(Constants.SKILLS_HITPOINTS);

    private String getRunningTime() {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void start() {
        Healer heal = new Healer(ctx);
//        Looter loot = new Looter(ctx);
        Combat combat = new Combat(ctx);

        tasks.add(heal);
//        tasks.add(loot);
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

        g.drawString("bluedog's flesh crawler", 20, 80);
        g.drawString("Time : " + getRunningTime(), 20, 100);
        g.drawString("Attack: "+ ctx.skills.level(Constants.SKILLS_ATTACK) + " (+"+(ctx.skills.level(Constants.SKILLS_ATTACK) - LVL_ATTACK_START)+")", 20, 120);
        g.drawString("Strength: "+ ctx.skills.level(Constants.SKILLS_STRENGTH) + " (+"+(ctx.skills.level(Constants.SKILLS_STRENGTH) - LVL_STRENGTH_START)+")", 20, 140);
        g.drawString("Defence: "+ ctx.skills.level(Constants.SKILLS_DEFENSE) + " (+"+(ctx.skills.level(Constants.SKILLS_DEFENSE) - LVL_DEFENCE_START)+")", 20, 160);
        g.drawString("Hitpoints: "+ ctx.skills.level(Constants.SKILLS_HITPOINTS) + " (+"+(ctx.skills.level(Constants.SKILLS_HITPOINTS) - LVL_HITPOINTS_START)+")", 20, 180);
    }
}
