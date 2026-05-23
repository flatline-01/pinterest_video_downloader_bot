package telegram.commands;

import services.LanguageService;
import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class LangCommand extends BotCommand{

    public LangCommand(){
        super("lang", "A command to change the language.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings){
        SendMessage sender = new SendMessage();
        sender.setChatId(chat.getId().toString());
        sender.setText(LanguageService.Get(chat.getId().toString(), "Select Language"));
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var btn1 = new InlineKeyboardButton();
        btn1.setText(LanguageService.Get(chat.getId().toString(), "English"));
        btn1.setCallbackData("English");
        var btn2 = new InlineKeyboardButton();
        btn2.setText(LanguageService.Get(chat.getId().toString(), "Russian"));
        btn2.setCallbackData("Russian");
        rowInline.add(btn1);
        rowInline.add(btn2);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        sender.setReplyMarkup(markupInline);
        try{
            absSender.execute(sender);
        } catch (TelegramApiException e){}
    }
}
