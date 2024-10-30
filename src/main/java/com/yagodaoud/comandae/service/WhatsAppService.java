package com.yagodaoud.comandae.service;

//import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Value("${twilio.whatsappFrom}")
    private String whatsappFrom;

    public void sendMessage(String to, String body) {
        //Message message = Message.creator(
               // new com.twilio.type.PhoneNumber("whatsapp:" + to),
               // new com.twilio.type.PhoneNumber("whatsapp:" + whatsappFrom),
               // body).create();
        // System.out.println("Message sent with SID: " + message.getSid());
    }

    public void sendMealOptions(String to, String mealSize) {
        String options = switch (mealSize.toLowerCase()) {
            case "p" -> "Selecione 1 carne, 1 guarnição e 1 salada."; //env marmita pequena
            case "m" -> "Selecione 1 carne, 2 guarnições e 2 saladas."; //env marmita media
            case "g" -> "Selecione 2 carnes, 2 guarnições e 2 saladas."; //env marmita grande
            default -> "Selecione um tipo válido de marmita.";
        };
        sendMessage(to, options);
    }
}
