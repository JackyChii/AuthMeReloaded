package fr.xephi.authme.command.executable.verification;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.command.PlayerCommand;
import fr.xephi.authme.data.VerificationCodeManager;
import fr.xephi.authme.mail.EmailService;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.service.CommonService;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;

/**
 * Used to complete the email verification process.
 */
public class VerificationCommand extends PlayerCommand {

    @Inject
    private CommonService commonService;

    @Inject
    private VerificationCodeManager codeManager;

    @Inject
    private EmailService emailService;

    @Override
    public void runCommand(Player player, List<String> arguments) {
        final String playerName = player.getName().toLowerCase();

        if (!emailService.hasAllInformation()) {
            ConsoleLogger.warning("Mail API is not set");
            commonService.send(player, MessageKey.INCOMPLETE_EMAIL_SETTINGS);
            return;
        }

        if(codeManager.isCodeRequired(playerName)){
            if(codeManager.checkCode(playerName, arguments.get(0))){
                commonService.send(player, MessageKey.VERIFICATION_CODE_VERIFIED);
            }else{
                commonService.send(player, MessageKey.INCORRECT_VERIFICATION_CODE);
            }
        }else{
           commonService.send(player, MessageKey.VERIFICATION_CODE_USELESS);
        }
    }
}
