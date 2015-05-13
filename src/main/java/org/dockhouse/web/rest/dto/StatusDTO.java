package org.dockhouse.web.rest.dto;

public class StatusDTO {

	public final static String STATUT_ONLINE = "online";
	public final static String STATUT_OFFLINE = "offline";

	private String status;

    public String getStatus() {
    	return status;
    }

    public void setStatus(String status) {
    	this.status = status;
    }
}
