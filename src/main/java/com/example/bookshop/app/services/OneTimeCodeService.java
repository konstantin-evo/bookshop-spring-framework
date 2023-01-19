package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.OneTimeCodeRepository;
import com.example.bookshop.app.model.entity.OneTimeCode;
import com.example.bookshop.web.services.SimpleMailSender;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OneTimeCodeService {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.twilio_number}")
    private String twilioNumber;

    @Value("${twilio.text}")
    private String twilioText;

    @Value("${mail.username}")
    private String bookstoreEmail;

    @Value("${mail.subject}")
    private String subjectEmail;

    @Value("${mail.text}")
    private String textEmail;

    /**
     * The SMS_CODE value is temporarily hardcoded due to the fact that the TWILIO service in free mode does not work stably,
     * and it is impossible to guarantee the operation of the operation of the service during launch
     */
    @Value("${twilio.magic_code}")
    private String smsCode;

    private final OneTimeCodeRepository oneTimeCodeRepository;
    private final SimpleMailSender mailSender;

    /**
     * The method sends SMS to a phone number
     * temporarily disabled due to support issues with Twilio service
     *
     * @param contact - telephone number
     * @return the generated value of the code
     */
    @SuppressWarnings("unused")
    public String sendSecretCodeSms(String contact) throws NoSuchAlgorithmException {
        Twilio.init(accountSid, authToken);
        String formattedContact = contact.replaceAll("[()-]]", "");
        String generatedCode = generateCode();

        Message.creator(new PhoneNumber(formattedContact),
                        new PhoneNumber(twilioNumber), twilioText + generatedCode)
                .create();

        return generatedCode;
    }

    public String sendSecretCodeEmail(String contact) throws NoSuchAlgorithmException {
        String generatedCode = generateCode();
        mailSender.send(subjectEmail, textEmail + generatedCode, bookstoreEmail, contact);
        return generatedCode;
    }

    public void saveCode(OneTimeCode oneTimeCode) {
        if (oneTimeCodeRepository.findByCode(oneTimeCode.getCode()) == null) {
            oneTimeCodeRepository.save(oneTimeCode);
        }
    }

    public boolean verifyCode(String code) {
        OneTimeCode oneTimeCode = oneTimeCodeRepository.findByCode(code);
        return (code.equals(smsCode)) || (oneTimeCode != null && !oneTimeCode.isExpired());
    }

    /**
     * The method generates a random numeric value in the format "XXX XXX"
     * For example, "158 927"
     */
    private String generateCode() throws NoSuchAlgorithmException {
        Random rand = SecureRandom.getInstanceStrong(); // SecureRandom is preferred to Random
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(rand.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

}
