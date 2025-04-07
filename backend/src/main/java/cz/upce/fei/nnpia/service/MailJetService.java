package cz.upce.fei.nnpia.service;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import com.mailjet.client.transactional.SendContact;
import com.mailjet.client.transactional.SendEmailsRequest;
import com.mailjet.client.transactional.TrackOpens;
import com.mailjet.client.transactional.TransactionalEmail;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import lombok.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailJetService {

    @Value("${mailjet.secret-key}")
    private String secretKey;

    @Value("${mailjet.api-key}")
    private String apiKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendReservationEmail(String to,Long restaurantId, UUID reservationUuid) throws MailjetException {

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(to, to))
                .from(new SendContact("rezervace@ondrejfialka.cz", "Rezervovač"))
                .htmlPart("<h1>Vyčkejte na potvrezení rezervace.</h1><a href='"+frontendUrl+"/restaurant/"+restaurantId+"reservation/cancel/" + reservationUuid +"'>zrušení rezervace</a>")
                .subject("Rezervace vytvořena - vyčkejte na potvrzení")
                .trackOpens(TrackOpens.ENABLED)
//                .attachment(Attachment.fromFile(attachmentPath))
//                .header("test-header-key", "test-value")
//                .customID("custom-id-value")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1) // you can add up to 50 messages per request
                .build();

        // act
        SendEmailsResponse response = request.sendWith(client);
        System.out.println(response);
    }

    public void sendReservationAcceptedEmail(@NonNull String to,Long resetaurantId, UUID reservationUuid) throws MailjetException {

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(to, to))
                .from(new SendContact("rezervace@ondrejfialka.cz", "Rezervovač"))
                .htmlPart("<h1>Vaše rezervace byla potvrzena</h1><a href='"+frontendUrl+"/restaurant/"+resetaurantId+"/reservation/cancel/" + reservationUuid +"'>zrušení rezervace</a>")
                .subject("Potvrzení rezervace")
                .trackOpens(TrackOpens.ENABLED)
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1) // you can add up to 50 messages per request
                .build();

        // act
        SendEmailsResponse response = request.sendWith(client);
        System.out.println(response);
    }

    public void sendReservationCancelEmail(@NonNull String to) throws MailjetException {

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(to, to))
                .from(new SendContact("rezervace@ondrejfialka.cz", "Rezervovač"))
                .htmlPart("<h1>Vaše rezervace byla úspěšně zrušena.</h1>")
                .subject("Zrušení rezervace")
                .trackOpens(TrackOpens.ENABLED)
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1) // you can add up to 50 messages per request
                .build();

        // act
        SendEmailsResponse response = request.sendWith(client);
        System.out.println(response);
    }
}