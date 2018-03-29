package me.uberpilot.harbinger.listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class OnMessageListener extends ListenerAdapter
{
    private static final Color ZOMBIE_COLOR = new Color(230, 126, 34);
    private static final Color HUMAN_COLOR = new Color(46, 204, 113);

    private static final Consumer<Message> DELETE_LONG = (response) -> response.delete().queueAfter(60, TimeUnit.SECONDS);
    private static final Consumer<Message> DELETE_SHORT = (response) -> response.delete().queueAfter(5, TimeUnit.SECONDS);

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if(event.isFromType(ChannelType.TEXT))
        {
            Member member = event.getMember();
            Guild guild = event.getGuild();


            if (event.getMessage().getContentDisplay().startsWith("!"))
            {

                //Tests for a Human Role, exiting if it doesn't exist.
                List<Role> roleList = guild.getRolesByName("Human", true);
                if (roleList.isEmpty())
                {
                    event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Error").setDescription("The Human role could not be found, and therefore cannot be added.").build()).queue(DELETE_LONG);
                    return;
                }

                Role human = roleList.get(0);

                //Tests for a Zombie Role, exiting if it doesn't exist.
                roleList = guild.getRolesByName("Zombie", true);
                if (roleList.isEmpty())
                {
                    event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Error").setDescription("The Zombie role could not be found, and therefore cannot be added.").build()).queue(DELETE_LONG);
                    return;
                }

                Role zombie = roleList.get(0);





                if (event.getMessage().getContentDisplay().equalsIgnoreCase("!zombie") || event.getMessage().getContentDisplay().equalsIgnoreCase("!switch") || event.getMessage().getContentDisplay().equalsIgnoreCase("!swap"))
                {

                    if (!member.getRoles().contains(zombie) && member.getRoles().contains(human))
                    {
                        guild.getController().addSingleRoleToMember(member, zombie).queue();
                        guild.getController().removeSingleRoleFromMember(member, human).queue();

                        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(zombie.getColor()).setTitle("Player Tagged").setDescription((member.getNickname() == null ? member.getUser().getName() : member.getNickname()) + " has joined the Zombies.").build()).queue();
                    }
                    event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                }
                else if (event.getMessage().getContentDisplay().equalsIgnoreCase("!join"))
                {
                    //Makes sure that the user doesn't already have any of the playing roles.
                    if (!(event.getMember().getRoles().contains(human) || event.getMember().getRoles().contains(zombie)))
                    {
                        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(human.getColor()).setTitle("Player Joined").setDescription((member.getNickname() == null ? member.getUser().getName() : member.getNickname()) + " has joined Human team.").build()).queue(DELETE_LONG);
                        guild.getController().addSingleRoleToMember(member, human).queue();
                    }
                    else
                    {
                        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setTitle("Error!").setDescription("You are already participating in the game!").build()).queue(DELETE_SHORT);
                    }


                    event.getMessage().delete().queueAfter(5, TimeUnit.SECONDS);
                }
            }
//            else if(event.getMessage().getContentDisplay().equalsIgnoreCase("!status"))
//            {
//                Role zombie, human;
//
//                //Tests for a Zombie Role, exiting if it doesn't exist.
//                List<Role> roleList = guild.getRolesByName("Human", true);
//                if(roleList.isEmpty())
//                {
//                    event.getTextChannel().sendMessage(
//                            new EmbedBuilder()
//                                    .setColor(Color.RED)
//                                    .setTitle("Error")
//                                    .setDescription("The Human role could not be found, and therefore cannot be removed.")
//                                    .build()
//                    ).queue(DELETE_LONG);
//                    return;
//                }
//                human = roleList.get(0);
//
//                //Tests for a Zombie Role, exiting if it doesn't exist.
//                roleList = guild.getRolesByName("Zombie", true);
//                if(roleList.isEmpty())
//                {
//                    event.getTextChannel().sendMessage(
//                            new EmbedBuilder()
//                                    .setColor(Color.RED)
//                                    .setTitle("Error")
//                                    .setDescription("The Zombie role could not be found, and therefore cannot be added.")
//                                    .build()
//                    ).queue((message) -> message.delete().queueAfter(30, TimeUnit.SECONDS));
//                    return;
//                }
//                zombie = roleList.get(0);
//
//                event.getTextChannel().sendMessage(
//                        new EmbedBuilder()
//                                .setColor(human.getColor())
//                                .setTitle("Human Role")
//                                .setDescription("Color: " + human.getColor().toString() + "\nSelf: " + (member.getRoles().contains(human) ? "\u2705" : "\u274E"))
//                                .build()
//                ).queue();
//
//                event.getTextChannel().sendMessage(
//                        new EmbedBuilder()
//                                .setColor(zombie.getColor())
//                                .setTitle("Zombie Role")
//                                .setDescription("Color: " + zombie.getColor().toString() + "\nSelf: " + (member.getRoles().contains(zombie) ? "\u2705" : "\u274E"))
//                                .build()
//                ).queue();
//            }
        }
    }
}
