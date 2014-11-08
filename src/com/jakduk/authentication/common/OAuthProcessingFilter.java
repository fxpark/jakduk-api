package com.jakduk.authentication.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.weaver.ast.Instanceof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jakduk.common.CommonConst;
import com.jakduk.model.simple.OAuthUserOnLogin;
import com.jakduk.repository.UserRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */
public class OAuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

	@Autowired
	UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	protected OAuthProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,	FilterChain chain) throws IOException, ServletException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		
		if (!authentication.isAuthenticated() && (!httpRequest.getServletPath().equals("/oauth/daum/callback") || 
				!httpRequest.getServletPath().equals("/oauth/write"))) {
			SecurityContextHolder.getContext().setAuthentication(null);
			
			if (logger.isInfoEnabled()) {
				logger.info("oauth was cancled. Authentication object was deleted.");
			}
		}
		
		super.doFilter(req, res, chain);
	}

	public OAuthProcessingFilter() {
		super("/oauth/daum/callback");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

		String type = request.getParameter("type");
		String error = request.getParameter("error");
		
		if (error != null) {
			throw new BadCredentialsException(error);
		}
		
		if (type!= null && (type.equals(CommonConst.OAUTH_TYPE_DAUM) || type.equals(CommonConst.OAUTH_TYPE_FACEBOOK))) {
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(type, null);
			SecurityContextHolder.getContext().setAuthentication(authRequest);

			return this.getAuthenticationManager().authenticate(authRequest);
		} else {
			throw new AuthenticationCredentialsNotFoundException("not found OAuth account");
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		if (authResult.getPrincipal() instanceof OAuthPrincipal) {
			OAuthPrincipal principal = (OAuthPrincipal) authResult.getPrincipal();
			OAuthUserOnLogin oauthUser = userRepository.findByOauthUser(principal.getType(), principal.getOauthId());
			
			if (oauthUser != null) {
				String addInfoStatus = oauthUser.getOauthUser().getAddInfoStatus();
				
				if (addInfoStatus.equals(CommonConst.OAUTH_ADDITIONAL_INFO_STATUS_BLANK)) {
					response.sendRedirect(request.getContextPath() + "/oauth/write");
				}
			}
			super.successfulAuthentication(request, response, chain, authResult);
		} else {
			// faild
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
					throws IOException, ServletException {
		if (failed instanceof AccessTokenRequiredException
				|| failed instanceof AccessTokenRequiredException) {
			// Need to force a redirect via the OAuth client filter, so rethrow
			// here
			throw failed;
		} else {
			// If the exception is not a Spring Security exception this will
			// result in a default error page
			super.unsuccessfulAuthentication(request, response, failed);
		}
	} 
}
