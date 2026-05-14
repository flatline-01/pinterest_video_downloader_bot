package telegram.commands;

import services.LanguageService;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class StartCommand extends BotCommand{
    public StartCommand() {
        super("start", "With this command you can start the Bot.");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings){
        SendMessage sender = new SendMessage();
        sender.setChatId(chat.getId().toString());
        String greetingMessage = LanguageService.Get(chat.getId().toString(), "Greeting");
        if (user.getUserName() != null) {
            greetingMessage += ", "  + user.getUserName();
        }
        sender.setText(greetingMessage + ". " + LanguageService.Get(chat.getId().toString(), "Instruction"));
        try {
            absSender.execute(sender);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }
}