package com.example.bookshop.app.services;

import com.example.bookshop.app.model.dao.OneTimeCodeRepository;
import com.example.bookshop.app.model.entity.OneTimeCode;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OneTimeCodeService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.twilio_number}")
    private String TWILIO_NUMBER;

    @Value("${twilio.text}")
    private String TWILIO_TEXT;

    @Value("${mail.username}")
    private String BOOKSTORE_EMAIL;

    @Value("${mail.subject}")
    private String SUBJECT_EMAIL;

    @Value("${mail.text}")
    private String TEXT_EMAIL;

    private final OneTimeCodeRepository oneTimeCodeRepository;
    private final JavaMailSender mailSender;

    public OneTimeCodeService(OneTimeCodeRepository oneTimeCodeRepository, JavaMailSender mailSender) {
        this.oneTimeCodeRepository = oneTimeCodeRepository;
        this.mailSender = mailSender;
    }

    public String sendSecretCodeSms(String contact) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = contact.replaceAll("[()-]]", "");
        String generatedCode = generateCode();

        Message.creator(new PhoneNumber(formattedContact),
                        new PhoneNumber(TWILIO_NUMBER), TWILIO_TEXT + generatedCode)
                .create();

        return generatedCode;
    }

    public String sendSecretCodeEmail(String contact) {

        String generatedCode = generateCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(BOOKSTORE_EMAIL);
        message.setTo(contact);
        message.setSubject(SUBJECT_EMAIL);
        message.setText(TEXT_EMAIL + generatedCode);
        mailSender.send(message);

        return generatedCode;
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }

    public void saveCode(OneTimeCode oneTimeCode) {
        if (oneTimeCodeRepository.findByCode(oneTimeCode.getCode()) == null) {
            oneTimeCodeRepository.save(oneTimeCode);
        }
    }

    public Boolean verifyCode(String code) {
        OneTimeCode oneTimeCode = oneTimeCodeRepository.findByCode(code);
        return (oneTimeCode != null && !oneTimeCode.isExpired());
    }
}
