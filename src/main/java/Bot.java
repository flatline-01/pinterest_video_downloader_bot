import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.handlers.CommandsHandler;

public class Bot {
    public static void main(String[] args){
        try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new CommandsHandler());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}