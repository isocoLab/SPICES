package org.soa4all.dashboard.gwt.module.consumptionplatform.client.view.ui;



public class Goal {
	String goalName;

	String id;
	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public Goal(String goalName, String id) {
		super();
		this.goalName = goalName;
		this.id = id;
	}


	
}
