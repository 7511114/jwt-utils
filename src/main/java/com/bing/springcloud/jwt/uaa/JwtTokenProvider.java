package com.bing.springcloud.jwt.uaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private JwtConfiguration configuration;
	
	public JwtTokenProvider(JwtConfiguration configuration) {
		this.configuration = configuration;
		//this.setConfiguration(configuration);
	}
	
	/**
	 * 生成token
	 * @param claims
	 * @return
	 */
	public String createToken(Claims claims) {
		String compactJws = Jwts.builder().setPayload(JSONObject.toJSONString(claims))
				.compressWith(CompressionCodecs.DEFLATE)
				.signWith(SignatureAlgorithm.HS512, configuration.getSecretKeySpec())
				.compact();
		return compactJws;
	}
	
	public Claims parseToken(String token) {
		try {
			return Jwts.parser().setSigningKey(configuration.getSecretKeySpec())
					.parseClaimsJws(token).getBody();
		} catch (Exception e) {
			logger.error("token:"+token+",转换异常" + e.getMessage(), e);
		}
		return null;
	}
	
	public JwtConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(JwtConfiguration configuration) {
		this.configuration = configuration;
	}
}
