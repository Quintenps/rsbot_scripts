package bluedog.fleshcrawler.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class Combat extends Task {

    private Npc getNearestAvailableNpc() {
        return ctx.npcs.select().name("Flesh Crawler").select(npc -> !npc.inCombat()).nearest().poll();
    }

    private Npc npcAttackingYou() {
        return ctx.npcs.select().name("Flesh Crawler").select(npc -> npc.interacting().equals(ctx.players.local())).poll();
    }

    private void attackNpc(Npc npcToKill) {
        ctx.camera.turnTo(npcToKill);
        npcToKill.interact("Attack");
        System.out.println("Found NPC " + npcToKill.name() + " #" + npcToKill.id());
        Condition.wait(() -> ctx.players.local().inCombat(), 800, 10);
    }

    public Combat(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        if (ctx.players.local().interacting().valid() || npcAttackingYou().valid() || ctx.players.local().interacting().interacting().equals (ctx.players.local())) {
            if(npcAttackingYou().valid() && !ctx.players.local().interacting().interacting().equals (ctx.players.local())){
                attackNpc(npcAttackingYou());
            }
            return false;
        }
        return true;
    }

    @Override
    public void execute() {
        toggleRun();
        attackNpc(getNearestAvailableNpc());
    }
}
