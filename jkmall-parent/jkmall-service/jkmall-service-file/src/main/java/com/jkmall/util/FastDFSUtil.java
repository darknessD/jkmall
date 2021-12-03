package com.jkmall.util;

import com.jkmall.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.csource.fastdfs.ClientGlobal;

import java.io.IOException;

public class FastDFSUtil {
    /**
     * load tracker info
     */
    static {
        String fileName = new ClassPathResource("fdfs_client.conf").getPath();
        try {
            ClientGlobal.init(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    public static String[] uploadFile(FastDFSFile fastDFSFile) throws IOException, MyException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        String[] uploads = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), null);
        return uploads;
    }
}
