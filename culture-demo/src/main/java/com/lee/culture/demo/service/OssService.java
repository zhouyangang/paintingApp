package com.lee.culture.demo.service;

import com.aliyun.openservices.ClientException;
import com.aliyun.openservices.ServiceException;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSErrorCode;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * @Author: joe
 * @Date: 17-8-7 上午11:11.
 * @Description:
 */
@Service
public class OssService {

    /**
     * 阿里云ACCESS_ID
     */
    @Value("${oss.access-id}")
    private String ACCESS_ID;
    /**
     * 阿里云ACCESS_KEY
     */
    @Value("${oss.access-key}")
    private String ACCESS_KEY;
    /**
     * 阿里云OSS_ENDPOINT  青岛Url
     */
    @Value("${oss.endpoint}")
    private String OSS_ENDPOINT;

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    @Value("${oss.bucket-name.profile}")
    private String PROFILE_BUCKET_NAME = "profiles";

    @Value("${oss.bucket-name.work}")
    private String WORK_BUCKET_NAME = "works";

    private OSSClient client;

    @PostConstruct
    public void init() {
        client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);

        //如果你想配置OSSClient的一些细节的参数，可以在构造OSSClient的时候传入ClientConfiguration对象。
        //ClientConfiguration是OSS服务的配置类，可以为客户端配置代理，最大连接数等参数。
        //具体配置看http://aliyun_portal_storage.oss.aliyuncs.com/oss_api/oss_javahtml/OSSClient.html#id2
        //ClientConfiguration conf = new ClientConfiguration();
        //OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY, conf);

        setBucketPublicReadable(PROFILE_BUCKET_NAME);
        setBucketPublicReadable(WORK_BUCKET_NAME);
    }

    /**
     * 创建Bucket
     *
     * @param bucketName  BUCKET名
     * @throws OSSException
     * @throws ClientException
     */
    private void ensureBucket(String bucketName)throws OSSException, ClientException {
        try{
            if (!client.isBucketExist(bucketName)) {
                client.createBucket(bucketName);
            }
        }catch(ServiceException e){
            if(!OSSErrorCode.BUCKES_ALREADY_EXISTS.equals(e.getErrorCode())){
                throw e;
            }
        }
    }

    /**
     * 删除一个Bucket和其中的Objects
     *
     * @param client  OSSClient对象
     * @param bucketName  Bucket名
     * @throws OSSException
     * @throws ClientException
     */
    public void deleteBucket(OSSClient client, String bucketName)throws OSSException, ClientException{
        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        for(int i = 0; i < listDeletes.size(); i++){
            String objectName = listDeletes.get(i).getKey();
            System.out.println("objectName = " + objectName);
            //如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    /**bucket-name.profile
     * 把Bucket设置成所有人可读
     *
     * @param bucketName  Bucket名
     * @throws OSSException
     * @throws ClientException
     */
    private void setBucketPublicReadable(String bucketName)throws OSSException, ClientException{
        //创建bucket
        ensureBucket(bucketName);

        //设置bucket的访问权限， public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    /**
     * 上传文件
     *
     * @param bucketName  Bucket名
     * @param objectKey  上传到OSS起的名
     * @param filename  本地文件名
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    private URL uploadFile(String bucketName, String objectKey, String filename)
            throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        //判断上传类型，多的可根据自己需求来判定
        if (filename.endsWith("xml")) {
            objectMeta.setContentType("text/xml");
        }
        else if (filename.endsWith("jpg")) {
            objectMeta.setContentType("image/jpeg");
        }
        else if (filename.endsWith("png")) {
            objectMeta.setContentType("image/png");
        }

        InputStream input = new FileInputStream(file);
        client.putObject(bucketName, objectKey, input, objectMeta);
        // 有效期 10年
        Date expiration = new Date(new Date().getTime() + 10 * 365 * 30 * 24 * 3600 * 1000);// 生成URL
        return client.generatePresignedUrl(bucketName, objectKey, expiration);
    }

    /**
     * 上传 用户头像
     * @param objectKey
     * @param filename
     * @throws FileNotFoundException
     */
    public URL uploadProfile(String objectKey, String filename) throws FileNotFoundException {
        return uploadFile(PROFILE_BUCKET_NAME, objectKey, filename);
    }

    public URL uploadWork(String objectKey, String filename) throws FileNotFoundException {
        return uploadFile(WORK_BUCKET_NAME, objectKey, filename);
    }

    /**
     *  下载文件
     *
     * @param bucketName  Bucket名
     * @param Objectkey  上传到OSS起的名
     * @param filename 文件下载到本地保存的路径
     * @throws OSSException
     * @throws ClientException
     */
    private void downloadFile(String bucketName, String Objectkey, String filename)
            throws OSSException, ClientException {
        client.getObject(new GetObjectRequest(bucketName, Objectkey),
                new File(filename));
    }
}
