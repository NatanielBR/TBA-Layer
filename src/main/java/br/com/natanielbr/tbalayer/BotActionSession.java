package br.com.natanielbr.tbalayer;

import br.com.natanielbr.tbalayer.annotations.CallBack;
import br.com.natanielbr.tbalayer.botUtils.BotUtilsBuilder;
import br.com.natanielbr.tbalayer.utils.ReflectionUtils;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.logging.BotLogger;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class BotActionSession<T> extends TelegramLongPollingSessionBot {
    private String token;
    private String botUsername;

    private T actionClass;

    /**
     * Construtor para a classe.
     * @param token O token para o acesso a API (token do BotFather)
     * @param botUsername O username do bot.
     */
    public BotActionSession(String token, String botUsername) {
        this.token = token;
        this.botUsername = botUsername;
    }

    public void setActions(T actionClass) {
        this.actionClass = actionClass;
    }

    @Override
    public void onUpdateReceived(Update update, Optional<Session> botSession) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            BotUtilsBuilder<Message> botUtilsBuilder = new BotUtilsBuilder<Message>(this).setValue(message);
            Method method = ReflectionUtils.findMethod(actionClass.getClass(),message.getText().substring(1).split(" ")[0]);
            botUtilsBuilder.setSession(botSession.orElse(null));
            try {
                method.invoke(actionClass, botUtilsBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (update.hasCallbackQuery()){
            CallbackQuery call = update.getCallbackQuery();
            BotUtilsBuilder<CallbackQuery> botUtilsBuilder = new BotUtilsBuilder<CallbackQuery>(this)
                    .setValue(call);
            Method method = ReflectionUtils.findMethod(actionClass.getClass(),
                    a-> a.isAnnotationPresent(CallBack.class) && call.getData().startsWith(a.getName()));
            if (method != null){
                botUtilsBuilder.setSession(botSession.orElse(null));
                try {
                    method.invoke(actionClass,botUtilsBuilder.build());
                } catch (Exception e) {
                    BotLogger.error("br.com.natanielbr.tbalayer.BotActionSession", e);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
