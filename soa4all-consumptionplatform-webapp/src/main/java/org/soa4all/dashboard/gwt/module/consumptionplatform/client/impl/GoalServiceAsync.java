package org.soa4all.dashboard.gwt.module.consumptionplatform.client.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GoalServiceAsync {

    public void getGoal(String id, AsyncCallback<String[]> callback);
}
