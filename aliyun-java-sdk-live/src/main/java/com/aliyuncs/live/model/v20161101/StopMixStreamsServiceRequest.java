/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.aliyuncs.live.model.v20161101;

import com.aliyuncs.RpcAcsRequest;

/**
 * @author auto create
 * @version 
 */
public class StopMixStreamsServiceRequest extends RpcAcsRequest<StopMixStreamsServiceResponse> {
	
	public StopMixStreamsServiceRequest() {
		super("live", "2016-11-01", "StopMixStreamsService", "live");
	}

	private String securityToken;

	private Long ownerId;

	private String mainDomainName;

	private String mainAppName;

	private String mainStreamName;

	private String mixDomainName;

	private String mixAppName;

	private String mixStreamName;

	public String getSecurityToken() {
		return this.securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
		putQueryParameter("SecurityToken", securityToken);
	}

	public Long getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		putQueryParameter("OwnerId", ownerId);
	}

	public String getMainDomainName() {
		return this.mainDomainName;
	}

	public void setMainDomainName(String mainDomainName) {
		this.mainDomainName = mainDomainName;
		putQueryParameter("MainDomainName", mainDomainName);
	}

	public String getMainAppName() {
		return this.mainAppName;
	}

	public void setMainAppName(String mainAppName) {
		this.mainAppName = mainAppName;
		putQueryParameter("MainAppName", mainAppName);
	}

	public String getMainStreamName() {
		return this.mainStreamName;
	}

	public void setMainStreamName(String mainStreamName) {
		this.mainStreamName = mainStreamName;
		putQueryParameter("MainStreamName", mainStreamName);
	}

	public String getMixDomainName() {
		return this.mixDomainName;
	}

	public void setMixDomainName(String mixDomainName) {
		this.mixDomainName = mixDomainName;
		putQueryParameter("MixDomainName", mixDomainName);
	}

	public String getMixAppName() {
		return this.mixAppName;
	}

	public void setMixAppName(String mixAppName) {
		this.mixAppName = mixAppName;
		putQueryParameter("MixAppName", mixAppName);
	}

	public String getMixStreamName() {
		return this.mixStreamName;
	}

	public void setMixStreamName(String mixStreamName) {
		this.mixStreamName = mixStreamName;
		putQueryParameter("MixStreamName", mixStreamName);
	}

	@Override
	public Class<StopMixStreamsServiceResponse> getResponseClass() {
		return StopMixStreamsServiceResponse.class;
	}

}
