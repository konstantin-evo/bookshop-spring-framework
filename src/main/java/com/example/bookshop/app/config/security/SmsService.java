package com.example.bookshop.app.config.security;

import com.example.bookshop.app.model.dao.SmsCodeRepository;
import com.example.bookshop.app.model.entity.SmsCode;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsService {

    @Value("${twilio.account_sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth_token}")
    private String AUTH_TOKEN;

    @Value("${twilio.twilio_number}")
    private String TWILIO_NUMBER;

    private final SmsCodeRepository smsCodeRepository;

    @Autowired
    public SmsService(SmsCodeRepository smsCodeRepository) {
        this.smsCodeRepository = smsCodeRepository;
    }

    public String sendSecretCodeSms(String contact) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = contact.replaceAll("[()-]]", "");
        String generatedCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(TWILIO_NUMBER),
                "Your secret code is: " + generatedCode
        ).create();
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

    public void saveNewCode(SmsCode smsCode) {
        if (smsCodeRepository.findByCode(smsCode.getCode()) == null) {
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        return (smsCode != null && !smsCode.isExpired());
    }
}
