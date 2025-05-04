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
import cz.upce.fei.nnpia.domain.Reservation;
import cz.upce.fei.nnpia.dto.ReservationRequestDto;
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

    public void sendReservationEmail(ReservationRequestDto reservation, Long restaurantId, UUID reservationUuid) throws MailjetException {

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(reservation.getEmail(), reservation.getEmail()))
                .from(new SendContact("rezervace@ondrejfialka.cz", "Rezervovač"))
//                .htmlPart("<h1>Vyčkejte na potvrezení rezervace.</h1><a href='"+frontendUrl+"/restaurant/"+restaurantId+"reservation/cancel/" + reservationUuid +"'>zrušení rezervace</a>")
                .htmlPart(
                        "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                                + "  <h2 style='color: #2c3e50;'>Děkujeme za vytvoření rezervace!</h2>"
                                + "  <p style='font-size: 16px;'>Rezervace byla přijata, nyní čeká na schválení.</p>"

                                + "  <div style='background-color: #f8f9fa; padding: 15px; border-radius: 6px; margin-top: 20px;'>"
                                + "    <p><strong>Jméno:</strong> " + reservation.getReservationName() + "</p>"
                                + "    <p><strong>Počet osob:</strong> " + reservation.getNumberOfPeople() + "</p>"
                                + "    <p><strong>Začátek:</strong> " + reservation.getStartTime().toString().replace("T", " ") + "</p>"
                                + "    <p><strong>Konec:</strong> " + reservation.getEndTime().toString().replace("T", " ") + "</p>"
                                + "  </div>"

                                + "  <p style='margin-top: 20px;'>Pokud si to rozmyslíte, můžete rezervaci zrušit kliknutím na následující tlačítko:</p>"

                                + "  <a href='" + frontendUrl + "/restaurant/" + restaurantId + "/reservation/cancel/" + reservationUuid + "'"
                                + "     style='display: inline-block; margin-top: 10px; padding: 10px 20px; background-color: #dc3545; color: #fff;"
                                + "            text-decoration: none; border-radius: 5px;'>Zrušit rezervaci</a>"

                                + "  <p style='margin-top: 30px; font-size: 14px; color: #6c757d;'>Tento e-mail byl vygenerován automaticky. Neodpovídejte na něj.</p>"
                                + "</div>"
                )
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

    public void sendReservationAcceptedEmail(@NonNull Reservation reservation, Long resetaurantId, UUID reservationUuid) throws MailjetException {

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(reservation.getEmail(), reservation.getEmail()))
                .from(new SendContact("rezervace@ondrejfialka.cz", "Rezervovač"))
//                .htmlPart("<h1>Vaše rezervace byla potvrzena</h1><a href='"+frontendUrl+"/restaurant/"+resetaurantId+"/reservation/cancel/" + reservationUuid +"'>zrušení rezervace</a>")
                .htmlPart(
                        "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                                + "  <h2 style='color: #28a745;'>Vaše rezervace byla potvrzena ✅</h2>"
                                + "  <p style='font-size: 16px;'>Těšíme se na Vaši návštěvu. Zde jsou podrobnosti Vaší rezervace:</p>"

                                + "  <div style='background-color: #f8f9fa; padding: 15px; border-radius: 6px; margin-top: 20px;'>"
                                + "    <p><strong>Jméno:</strong> " + reservation.getReservationName() + "</p>"
                                + "    <p><strong>Počet osob:</strong> " + reservation.getNumberOfPeople() + "</p>"
                                + "    <p><strong>Začátek:</strong> " + reservation.getStartTime().toString().replace("T", " ") + "</p>"
                                + "    <p><strong>Konec:</strong> " + reservation.getEndTime().toString().replace("T", " ") + "</p>"
                                + "  </div>"

                                + "  <p style='margin-top: 20px;'>V případě potřeby můžete rezervaci zrušit kliknutím na následující tlačítko:</p>"

                                + "  <a href='" + frontendUrl + "/restaurant/" + resetaurantId + "/reservation/cancel/" + reservationUuid + "'"
                                + "     style='display: inline-block; margin-top: 10px; padding: 10px 20px; background-color: #dc3545; color: #fff;"
                                + "            text-decoration: none; border-radius: 5px;'>Zrušit rezervaci</a>"

                                + "  <p style='margin-top: 30px; font-size: 14px; color: #6c757d;'>Děkujeme, že jste si vybrali naši restauraci.</p>"
                                + "</div>"
                ).subject("Potvrzení rezervace")
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

    public void sendReservationCancelEmail(@NonNull Reservation reservation) throws MailjetException {

        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(secretKey)
                .build();

        MailjetClient client = new MailjetClient(options);
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(reservation.getEmail(), reservation.getEmail()))
                .from(new SendContact("rezervace@ondrejfialka.cz", "Rezervovač"))
//                .htmlPart("<h1>Vaše rezervace byla úspěšně zrušena.</h1>")
                .htmlPart(
                        "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                                + "  <h2 style='color: #dc3545;'>Rezervace byla úspěšně zrušena ❌</h2>"
                                + "  <p style='font-size: 16px;'>Vaše rezervace byla zrušena. Je nám líto, že k návštěvě nedojde, ale doufáme, že nás navštívíte jindy.</p>"

                                + "  <div style='background-color: #f8f9fa; padding: 15px; border-radius: 6px; margin-top: 20px;'>"
                                + "    <p><strong>Jméno:</strong> " + reservation.getReservationName() + "</p>"
                                + "    <p><strong>Počet osob:</strong> " + reservation.getNumberOfPeople() + "</p>"
                                + "    <p><strong>Začátek:</strong> " + reservation.getStartTime().toString().replace("T", " ") + "</p>"
                                + "    <p><strong>Konec:</strong> " + reservation.getEndTime().toString().replace("T", " ") + "</p>"
                                + "  </div>"

                                        + "  <p style='margin-top: 30px; font-size: 14px; color: #6c757d;'>Pokud šlo o omyl, prosím vytvořte rezervaci znovu prostřednictvím našich stránek.</p>"
                                        + "</div>"
                                )
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