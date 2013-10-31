package es.magicbox.hackathon.utils.network;

/**
 * Small class to return more information from network API request
 *
 */
public class NetworkResponse {
	
	private int status;
	
	private String status_msg;
	
	private String content;
	
	
	public NetworkResponse(int _status, String _status_msg, String _content) {
		this.content = _content;
		this.status = _status;
		this.status_msg = _status_msg;
	}


	public int getStatus() {
		return status;
	}


	public String getStatus_msg() {
		return status_msg;
	}


	public String getContent() {
		return content;
	}
	
	public boolean isOkResponse() {
		return (status >= 200 && status < 300);
	}


	@Override
	public String toString() {
		return "NetworkResponse [status=" + status + ", status_msg="
				+ status_msg + ", content=" + content + "]";
	}
	
	

}
