package br.com.natanielbr.tbalayer.botUtils;

import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.Serializable;

public class BotUtils<T> {
    private T value;
    private Session session;
    private AbsSender sender;

    BotUtils(T value, Session session, AbsSender sender) {
        this.value = value;
        this.session = session;
        this.sender = sender;
    }

    public T getValue() {
        return value;
    }

    public boolean isEditable() {
        return getLastMessageID() != null;
    }

    public SendMessage getSend() {
        if (value instanceof Message) {
            Message message = (Message) value;
            return new SendMessage().setChatId(message.getChatId());
        } else {
            return new SendMessage();
        }
    }

    public void sendMessage(SendMessage send, boolean edit){
        if ( edit && isEditable()){
            if (!editText(send)) sendMessage(send,false);
        }else{
            execute(send);
        }
    }

    public void sendMessage(SendMessage send){
        sendMessage(send,false);
    }

    public boolean editText(SendMessage send) {
        if (!isEditable()) return false;
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(send.getChatId());
        editMessageText.setText(send.getText());
        editMessageText.setMessageId(getLastMessageID());
        try {
            editMessageText.setReplyMarkup((InlineKeyboardMarkup) send.getReplyMarkup());
        } catch (ClassCastException err) {
            //Não da pra fazer cast, ou seja não é possivel
            return false;
        }
        executeAbs(editMessageText);
        return true;

    }

    private void execute(SendMessage sendMessage) {
        Message message = executeAbs(sendMessage);
        if (message != null)  setLastMessageID(message.getMessageId());
    }

    private Integer getLastMessageID() {
        return (Integer) session.getAttribute("lastMSG");
    }

    private void setLastMessageID(Integer value) {
        session.setAttribute("lastMSG", value);
    }

    private <K extends Serializable> K executeAbs(BotApiMethod<K> method) {
        try {
            return sender.execute(method);
        } catch (TelegramApiException e) {
            BotLogger.error("BotUtils", e);
            return null;
        }
    }
}
