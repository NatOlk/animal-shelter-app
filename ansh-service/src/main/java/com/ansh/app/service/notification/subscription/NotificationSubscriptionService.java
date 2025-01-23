package com.ansh.app.service.notification.subscription;

import com.ansh.entity.animal.UserProfile;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.external.ExternalNotificationServiceClient;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationSubscriptionService {

  private static final Logger LOG = LoggerFactory.getLogger(NotificationSubscriptionService.class);

  @Autowired
  private ExternalNotificationServiceClient externalNotificationServiceClient;

  @Value("${notification.subscription.endpoint.allByApprover}")
  private String allByApproverEndpoint;

  @Value("${notification.subscription.endpoint.statusByApprover}")
  private String statusByApproverEndpoint;

  public List<Subscription> getAllSubscriptionByApprover(String approver) {
    return externalNotificationServiceClient.post(
        allByApproverEndpoint,
        Map.of("approver", approver),
        List.class
    );
  }

  public UserProfile.AnimalNotificationSubscriptionStatus getStatusByApprover(String approver) {
    return externalNotificationServiceClient.post(
        statusByApproverEndpoint,
        Map.of("approver", approver),
        UserProfile.AnimalNotificationSubscriptionStatus.class
    );
  }

  protected void setAllByApproverEndpoint(String allByApproverEndpoint) {
    this.allByApproverEndpoint = allByApproverEndpoint;
  }

  protected void setStatusByApproverEndpoint(String statusByApproverEndpoint) {
    this.statusByApproverEndpoint = statusByApproverEndpoint;
  }
}
