package br.com.natanielbr.tbalayer.annotations;

import java.lang.annotation.*;


/**
 * Annotation to transform a java method in Telegram Command.
 * In Telegram API, a command is /commandName.
 * To Usage is necessary:
 *
 * @Command
 * public void commandName(BotUtils<Message>)
 *
 * Is necessary the same name the java method and Telegram Command,
 * and usage the a BotUtils<Message> to receive a message and others
 * methods utils.
 */
@SuppressWarnings("ALL")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
}
