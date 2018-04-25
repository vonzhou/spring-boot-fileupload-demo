package com.vonzhou.example;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication(exclude = {MultipartAutoConfiguration.class})
//@SpringBootApplication
public class SpringBootFileuploadDemoApplication {

    /**
     * 跟踪这种方式为何不行?
     * org.apache.commons.fileupload.FileUploadBase#setProgressListener(org.apache.commons.fileupload.ProgressListener)
     * org.apache.commons.fileupload.MultipartStream.ProgressNotifier#ProgressNotifier(org.apache.commons.fileupload.ProgressListener, long)
     *
     * @return
     */
//    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
//    public CommonsMultipartResolver getMultipartResolver() {
//        System.out.println("custom  multipart resolver");
//        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
//        cmr.setMaxUploadSize(1000000000);
//
//
//        cmr.getFileUpload().setProgressListener(new ProgressListener() {
//            private long megaBytes = -1;
//
//            public void update(long pBytesRead, long pContentLength, int pItems) {
//                long mBytes = pBytesRead / 1000000;
//                if (megaBytes == mBytes) {
//                    return;
//                }
//                megaBytes = mBytes;
//                System.out.println("We are currently reading item " + pItems);
//                if (pContentLength == -1) {
//                    System.out.println("So far, " + pBytesRead + " bytes have been read.");
//                } else {
//                    System.out.println("So far, " + pBytesRead + " of " + pContentLength
//                            + " bytes have been read.");
//                }
//            }
//        });
//
//        return cmr;
//    }

    @Bean
    public MultipartConfigElement configElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("2048MB");
        factory.setMaxRequestSize("2048MB");

        return factory.createMultipartConfig();
    }

    @Bean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
    public CommonsMultipartResolver getMultipartResolver() {
        System.out.println("custom  multipart resolver");
        CommonsMultipartResolver cmr = new MyCommonsMultipartResolver();
        cmr.setMaxUploadSize(1000000000);

        return cmr;
    }

    public static class MyCommonsMultipartResolver extends CommonsMultipartResolver {
        @Override
        protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
            FileUpload res = new ServletFileUpload(fileItemFactory);
            res.setProgressListener(new ProgressListener() {
                private long megaBytes = -1;

                public void update(long pBytesRead, long pContentLength, int pItems) {
                    // 控制频率
                    long mBytes = pBytesRead / 1000000;
                    if (megaBytes == mBytes) {
                        return;
                    }
                    megaBytes = mBytes;
                    System.out.println(String.format("正在读取第%s项...", pItems));
                    System.out.println(String.format("共%s字节，已读取%s字节", pContentLength, pBytesRead));
                }
            });
            return res;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFileuploadDemoApplication.class, args);
    }
}
