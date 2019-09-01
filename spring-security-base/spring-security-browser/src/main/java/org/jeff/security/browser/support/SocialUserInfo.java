/**
 * 
 */
package org.jeff.security.browser.support;

import lombok.Data;

/**
 * @author zhailiang
 *
 */
@Data
public class SocialUserInfo {

	private String providerId;
	
	private String providerUserId;
	
	private String nickname;
	
	private String headimg;

}
