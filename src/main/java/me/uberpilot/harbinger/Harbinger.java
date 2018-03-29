package me.uberpilot.harbinger;

import me.uberpilot.harbinger.listeners.OnMessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Harbinger
{
    private final String TOKEN = "DISCORD TOKEN HERE";

    private Harbinger()
    {
        try
        {
            JDA jda = new JDABuilder(AccountType.BOT).setToken(TOKEN).addEventListener(new OnMessageListener()).buildBlocking();
            jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "HvZ | !join | !zombie           Questions/comments should be directed to @UberPilot"));
        }
        catch (LoginException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Harbinger harbinger = new Harbinger();
    }
}
