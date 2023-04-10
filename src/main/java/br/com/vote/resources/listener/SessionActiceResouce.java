package br.com.vote.resources.listener;

import br.com.vote.service.SessionService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class SessionActiceResouce {

    private SessionService sessionService;

    @SqsListener(
            value = "${cloud.aws.sqs.queue.active-session}",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS
    )
    public void getSessionActive(String idSession) {
        sessionService.desactivateSession(idSession);
        System.out.println(idSession);
    }
}
