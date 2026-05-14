package telegram.handlers;

import services.LanguageService;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pinterest.Downloader;
import io.github.cdimascio.dotenv.Dotenv;
import telegram.commands.HelpCommand;
import telegram.commands.LangCommand;
import telegram.commands.StartCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    private static final String PINTEREST_LONG_LINK = "https://www.pinterest.com/pin/";
    private static final String PINTEREST_SHORT_LINK = "https://pin.it/";
    public static final String BOT_NAME = "AnotheR_VideO_DownloadeR_Bot";
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private List<BotCommand> commands;

    public CommandsHandler(){
        commands = new ArrayList<>();
        commands.add(new StartCommand());
        commands.add(new HelpCommand());
        commands.add(new LangCommand());

        for(BotCommand command : commands){
            register(command);
        }

        registerDefaultAction((absSender, message) -> {
            SendMessage sender = new SendMessage();
            sender.setChatId(message.getChatId().toString());
            sender.setText(LanguageService.Get(message.getChatId().toString(),"Wrong Command"));
            try {
                absSender.execute(sender);
            } catch (TelegramApiException e) {}
        });
    }

    @Override
    public void processNonCommandUpdate(Update update){
        SendMessage messageSender = new SendMessage();

        if (update.getMessage() != null) {
            String userMessage = update.getMessage().getText();
            String chatId =  update.getMessage().getChatId().toString();

            SendVideo videoSender = new SendVideo();

            if(userMessage.startsWith(PINTEREST_LONG_LINK) || userMessage.startsWith(PINTEREST_SHORT_LINK)){
                sendMessageToUser(messageSender, LanguageService.Get(chatId, "Wait"), chatId);

                //InputFile video = Downloader.getInstance().download(userMessage);
                Downloader d = new Downloader(userMessage);
                Future<InputFile> future = executorService.submit(d);
                try {
                    InputFile video = future.get();
                    if(video != null){
                        sendMessageToUser(videoSender, video, chatId);
                    } else {
                        sendMessageToUser(messageSender, LanguageService.Get(chatId, "Error"), chatId);
                    }
                } 
                catch(Exception ex) {
                    sendMessageToUser(messageSender, LanguageService.Get(chatId, "Error"), chatId);
                }
            }
            else {
                sendMessageToUser(messageSender, LanguageService.Get(chatId, "Wrong Input"), chatId);
            }
        }
        else if (update.hasCallbackQuery()) {
            CallbackQuery query = update.getCallbackQuery();
            String chatId =  query.getMessage().getChatId().toString();
            String selectedLang = query.getData();
            String lang = "en";
            switch (selectedLang) {
                case "English":
                    lang = "en";
                    break;
                case "Russian":
                    lang = "ru";
                    break;
            }   
            LanguageService.SetCurrentLang(chatId, lang);
            sendMessageToUser(messageSender, LanguageService.Get(chatId, "Language changed"), chatId);
        }
    }

    public synchronized void sendMessageToUser(SendMessage messageSender, String message, String id){
        messageSender.setChatId(id);
        messageSender.setText(message);
        try{
            execute(messageSender);
        } catch(TelegramApiException ex){
            System.out.println(ex.getMessage());
        }
    }

    public synchronized void sendMessageToUser(SendVideo videoSender, InputFile video, String id){
        videoSender.setChatId(id);
        videoSender.setVideo(video);
        try{
            execute(videoSender);
        } catch(TelegramApiException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        Dotenv dotenv = Dotenv.load();
        return dotenv.get("BOT_TOKEN");
    }
}
