package com.tumcca.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-05-29
 */
public class OSSConfig {
    String accessKeyId;
    String secretAccessKey;
    String endpoint;
    String apiVersion;
    String bucketName;
    String trashBucket;

    public OSSConfig() {
    }

    public OSSConfig(String accessKeyId, String secretAccessKey, String endpoint, String apiVersion, String bucketName, String trashBucket) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.endpoint = endpoint;
        this.apiVersion = apiVersion;
        this.bucketName = bucketName;
        this.trashBucket = trashBucket;
    }

    @JsonProperty
    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    @JsonProperty
    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    @JsonProperty
    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @JsonProperty
    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @JsonProperty
    public String getTrashBucket() {
        return trashBucket;
    }

    public void setTrashBucket(String trashBucket) {
        this.trashBucket = trashBucket;
    }
}
