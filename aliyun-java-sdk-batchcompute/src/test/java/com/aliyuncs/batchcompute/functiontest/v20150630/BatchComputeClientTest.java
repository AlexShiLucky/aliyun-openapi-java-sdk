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

package com.aliyuncs.batchcompute.functiontest.v20150630;

import com.aliyuncs.batchcompute.main.v20150630.BatchCompute;
import com.aliyuncs.batchcompute.main.v20150630.BatchComputeClient;
import com.aliyuncs.batchcompute.model.v20150630.*;
import com.aliyuncs.batchcompute.pojo.v20150630.*;
import com.aliyuncs.batchcompute.util.Config;
import com.aliyuncs.exceptions.ClientException;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guangchun.luo on 15/4/14.
 */
public class BatchComputeClientTest extends TestCase {

    private static BatchCompute client;

    private String imageId;

    private String PROGRAM_NAME = "count_all_num.py";

    private String OSS_BUCKET = "loak";
    private String OSS_PROGRAM_PACKAGE_PATH = "diku_simple_test/program_package.tar.gz";


    private String OSS_LOG_PATH = "diku_simple_test/log";
    private String OSS_DATA_PACKAGE = "diku_simple_test/diku_test_file.tar.gz";


    @Override
    public void setUp() throws Exception {
        Config cfg = Config.getInstance();
        BatchComputeClient.verbose = true;
        client = new BatchComputeClient(cfg.getRegionId(), cfg.getAccessId(), cfg.getAccessKey());
    }

    @Test
    public void testImages() throws ClientException {

        ListImagesResponse response = client.listImages();

        List<Image> list = response.getImageList();

        assertTrue(list.size() >= 0);

        if(list.size()>0){
            Image img = list.get(0);
            imageId = img.getImageId();
            assertTrue(img.getImageId().startsWith("img-"));
        }
    }




    @Test
    public void testJob() throws ClientException {


        //创建
        CreateJobResponse res = createJob();

        String jobId = res.getJobId();


        assertTrue(jobId != null);
        assertTrue(res.getRequestId() != null);

        //查询描述
        GetJobDescriptionResponse getJobDescriptionResponse = client.getJobDescription(jobId);

        JobDescription jobDescription = getJobDescriptionResponse.getJobDescription();
        assertEquals("jobName1", jobDescription.getJobName());



        TaskDescription countTask = jobDescription.getTaskDag().getTaskDescMap().get("CountTask");
        assertEquals("CountTask", countTask.getTaskName());

        assertEquals(PROGRAM_NAME, countTask.getProgramName());
        assertEquals(imageId, countTask.getImageId());

        assertEquals("JobTag", jobDescription.getJobTag());


        //修改
        try {
            client.updateJobPriority(res.getJobId(), 2);
        } catch (ClientException e) {
            //waiting 状态 不能修改 优先级,只有stopped才能
            assertEquals("StateConflict", e.getErrCode());
        }


        //列举 job status
        ListJobsResponse listJobsResponse = client.listJobs();

        List<Job> list = listJobsResponse.getJobList();

        assertTrue(list.size() > 0);
        assertTrue(jobListContains(list, jobId));


        //列举 task status
        ListTasksResponse listTasksResponse = client.listTasks(jobId);

        List<Task> taskList = listTasksResponse.getTaskList();

        assertTrue(list.size() >= 0);

        if (list.size() > 0) {
            Task task = taskList.get(0);
            assertTrue(task.getTaskName().length() > 0);

            List<Instance> insList = task.getInstanceList();

            if (insList.size() > 0) {
                Instance instance = insList.get(0);
                assertTrue(instance.getInstanceId() >= 0);
            }
        }


        //stop
        client.stopJob(jobId);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //再修改
        client.updateJobPriority(res.getJobId(), 2);


        //查询 status
        GetJobResponse getJobResponse = client.getJob(jobId);

        assertEquals(2, getJobResponse.getJob().getPriority());


        //start
        client.startJob(jobId);

        //查询 status
        getJobResponse = client.getJob(jobId);

        assertEquals("Waiting", getJobResponse.getJob().getState());


        //stop
        client.stopJob(jobId);

        //delete
        client.deleteJob(jobId);


        try {
            getJobResponse = client.getJob(jobId);
        } catch (ClientException e) {
           // e.printStackTrace();
            assertEquals("ResourceNotFound", e.getErrCode());
        }


    }





    private boolean jobListContains(List<Job> jobList, String jobId) {
        for (Job job : jobList) {
            if (job.getJobId().equals(jobId)) return true;
        }
        return false;
    }

    private CreateJobResponse createJob() throws ClientException {
        JobDescription job = genJobObject();
        return client.createJob(job);

    }
    private String getImageId() throws ClientException {

        ListImagesResponse response = client.listImages();

        List<Image> list = response.getImageList();

        if(list.size()>0){
            Image img = list.get(0);
            imageId = img.getImageId();
            return imageId;
        }else{
            return null;
        }
    }

    private JobDescription genJobObject() {

        String imageId = null;
        try {
            imageId = getImageId();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        TaskDag taskDag = new TaskDag();

        TaskDescription task = new TaskDescription();

        ResourceDescription resourceDesc = new ResourceDescription();
        resourceDesc.setCpu(1200);      //12 threads
        resourceDesc.setMemory(16000);  //16 G

        task.setResourceDescription(resourceDesc);
        task.setInstanceCount(1);
        task.setImageId(imageId);
        task.setProgramName(PROGRAM_NAME);
        task.setProgramType("python");
        task.setTimeout(21600); //seconds
        task.setPackageUri("oss://" + OSS_BUCKET + "/" + OSS_PROGRAM_PACKAGE_PATH);

        task.setStderrRedirectPath("oss://" + OSS_BUCKET + "/" + OSS_LOG_PATH);
        task.setStdoutRedirectPath("oss://" + OSS_BUCKET + "/" + OSS_LOG_PATH);

        Map<String, TaskDescription> taskDescMap = new HashMap();

        taskDescMap.put("CountTask", task);

        taskDag.setTaskDescMap(taskDescMap);
        List<String> list = new ArrayList();

        //taskDag.addDependencies("CountTask", list);

        JobDescription job = new JobDescription();
        job.setJobName("jobName1");
        job.setJobTag("JobTag");
        job.setTaskDag(taskDag);
        job.setPriority(0);

        return job;
    }
}
