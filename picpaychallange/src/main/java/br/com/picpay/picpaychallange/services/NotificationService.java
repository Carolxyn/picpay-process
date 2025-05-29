package br.com.picpay.picpaychallange.services;

import br.com.picpay.picpaychallange.Dto.NotificationDTO;
import br.com.picpay.picpaychallange.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;


    public void  sendNotification(User user, String message) throws Exception {
        String email  = user.getEmail();
        NotificationDTO notificationRequest = new NotificationDTO(email, message);

        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://run.mocky.io/v3/ab88dad1-6c90-4d17-b603-05ba3b19a210", notificationRequest, String.class);

        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)){
            System.out.println("Erro ao enviar a notificação");
            throw new Exception("Serviço de notificação está fora do ar");

    }
}
    }
