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
package com.aliyuncs.green.model.v20161018;

import com.aliyuncs.RpcAcsRequest;
import java.util.List;

/**
 * @author auto create
 * @version 
 */
public class ImageResultsRequest extends RpcAcsRequest<ImageResultsResponse> {
	
	public ImageResultsRequest() {
		super("Green", "2016-10-18", "ImageResults", "green");
	}

	private List<String> taskIds;

	public List<String> getTaskIds() {
		return this.taskIds;
	}

	public void setTaskIds(List<String> taskIds) {
		this.taskIds = taskIds;	
		for (int i = 0; i < taskIds.size(); i++) {
			putQueryParameter("TaskId." + (i + 1) , taskIds.get(i));
		}	
	}

	@Override
	public Class<ImageResultsResponse> getResponseClass() {
		return ImageResultsResponse.class;
	}

}
