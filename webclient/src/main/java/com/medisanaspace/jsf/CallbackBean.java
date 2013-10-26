package com.medisanaspace.jsf;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.medisanaspace.web.main.CloudClient;

@Controller
@Scope("request")
public class CallbackBean {
	
	/***********************************
	 *  Injected Beans
	 *********************************/
	
	@Autowired private CloudClient cloudClient;
	
	@Autowired private SessionDataBean sessionDataBean;

	
	/**********************************
	 * temporary view attributes and parameters
	 **********************************/
	
	private String oauth_token;
	private String oauth_verifier;
	private boolean deny;
	private String messageLog;

	/**
	 * Authorize verifier token
	 * 
	 * @return
	 */
	public String authorizeVerifierToken() {
		// we need the verifier token!
		if (!FacesContext.getCurrentInstance().isPostback()) {
			if (!isDeny() && oauth_verifier != null) {
				try {
					sessionDataBean.setOauthdata(cloudClient.authorizeWithVerifierToken(oauth_verifier));
					setMessageLog(cloudClient.getMessageLog());
				} catch (Exception e) {
					// System.out.println("Error: Authorize verifier token error");
					return "error.jsf";
				}
			} else {
				// System.out.println("Error: denied or no Verifiertoken");
				return "error.jsf";
			}
		}
		return null;
	}
	
	
	/**
	 * Run tests after authorization process.
	 * 
	 * @return
	 */
	public String runTest() {
		try {
			if (!sessionDataBean.getSelectedTests().isEmpty()) {
				cloudClient.runTests(sessionDataBean.getSelectedTests(), sessionDataBean.getOauthdata());
			}
			messageLog = cloudClient.getMessageLog();
		} catch (Exception e) {
			System.out.println("Error: Tests do not run correctly");
			return "error.jsf";
		}
		return null;
	}
	
	/**
	 * Add random generated data to a set of modules
	 * 
	 * @return
	 */
	public String addRandomData() {
		try {
			if (sessionDataBean.isAddRandomData() && !sessionDataBean.getSelectedModulesToAddRandomData().isEmpty()) {
				cloudClient.addRandomData(sessionDataBean.getSelectedModulesToAddRandomData(), sessionDataBean.getOauthdata());
			}
			messageLog = cloudClient.getMessageLog();
		} catch (Exception e) {
			System.out.println("Error: Tests do not run correctly");
			return "error.jsf";
		}
		return null;
	}
	
	public String getOauth_token() {
		return oauth_token;
	}

	public void setOauth_token(String oauth_token) {
		this.oauth_token = oauth_token;
	}

	public String getOauth_verifier() {
		return oauth_verifier;
	}

	public void setOauth_verifier(String oauth_verifier) {
		this.oauth_verifier = oauth_verifier;
	}

	public boolean isDeny() {
		return deny;
	}

	public void setDeny(boolean deny) {
		this.deny = deny;
	}

	public String getMessageLog() {
		return messageLog;
	}

	public void setMessageLog(String messageLog) {
		this.messageLog = messageLog;
	}

}