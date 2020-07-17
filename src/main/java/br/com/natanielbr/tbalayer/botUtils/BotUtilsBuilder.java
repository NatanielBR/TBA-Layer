package br.com.natanielbr.tbalayer.botUtils;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class BotUtilsBuilder<T> {
    private T value;
    private Session session;
    private AbsSender sender;

    public BotUtilsBuilder(AbsSender sender) {
        this.sender = sender;
    }

    public BotUtilsBuilder<T> setValue(T value){
        this.value = value;
        return this;
    }
    public BotUtilsBuilder<T> setSession(Session session){
        this.session = session;
        return this;
    }
    public BotUtils<T> build(){
        return new BotUtils<>(value,session, sender);
    }
}
