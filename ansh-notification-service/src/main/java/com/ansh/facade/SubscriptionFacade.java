package com.ansh.facade;

import com.ansh.entity.subscription.Subscription;
import java.util.List;

public interface SubscriptionFacade {

    List<Subscription> getAllSubscriptionByApprover(String approver);
}
