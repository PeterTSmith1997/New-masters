package dataModel;

/**
 * @author peter
 * @version 21 Jul 2019
 */
public class Hits {
	
	private final String iPaddress;
	private final String request;
	private final String dateTime;
	private final int response;
	private final int size;
	private final String referer;
	private final String userAgent;
	private final String protocal;

	/**
	 * @param iPaddress
	 * @param request
	 * @param dateTime
	 * @param response
	 * @param size
	 * @param referer
	 * @param userAgent
	 */
	public Hits(String iPaddress, String request, String protocal, String dateTime,
			int response, int size, String referer, String userAgent) {
		this.iPaddress = iPaddress;
		this.request = request;
		this.protocal = protocal;
		this.dateTime = dateTime;
		this.response = response;
		this.size = size;
		this.referer = referer;
		this.userAgent = userAgent;
	}

	/**
	 * @return the iPaddress
	 */
	public String getiPaddr() {
		return iPaddress;
	}

	/**
	 * @return the request
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @return the response
	 */
	public int getResponse() {
		return response;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @return the protocal
	 */
	public String getProtocal() {
		return protocal;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Hits other = (Hits) obj;
		if (dateTime == null) {
			if (other.dateTime != null) {
				return false;
			}
		} else if (!dateTime.equals(other.dateTime)) {
			return false;
		}
		if (iPaddress == null) {
			if (other.iPaddress != null) {
				return false;
			}
		} else if (!iPaddress.equals(other.iPaddress)) {
			return false;
		}
		if (protocal == null) {
			if (other.protocal != null) {
				return false;
			}
		} else if (!protocal.equals(other.protocal)) {
			return false;
		}
		if (referer == null) {
			if (other.referer != null) {
				return false;
			}
		} else if (!referer.equals(other.referer)) {
			return false;
		}
		if (request == null) {
			if (other.request != null) {
				return false;
			}
		} else if (!request.equals(other.request)) {
			return false;
		}
		if (response != other.response) {
			return false;
		}
		if (size != other.size) {
			return false;
		}
		if (userAgent == null) {
			if (other.userAgent != null) {
				return false;
			}
		} else if (!userAgent.equals(other.userAgent)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IP address=" + iPaddress + ", request=" + request + ", dateTime="
				+ dateTime + ", response=" + response + ", size=" + size
				+ ", referer=" + referer + ", userAgent=" + userAgent
				+ ", protocal=" + protocal;
	}
	
}