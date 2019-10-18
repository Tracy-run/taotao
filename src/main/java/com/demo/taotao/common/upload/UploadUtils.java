package com.demo.taotao.common.upload;

import com.demo.taotao.common.pageutil.PageUtil;
import com.mysql.jdbc.log.LogFactory;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.NotSupportedException;
import java.io.File;
import java.util.Map;

public class UploadUtils {

    private static Logger logger = LoggerFactory.getLogger(UploadUtils.class);


    private static final String[] extension = {"png", "jpg", "xls", "xlsx", "zip", "rar", "exe", "doc", "docx", "txt", "pdf", "ppt"};


    /**
     * 上传文件
     * @param file
     * @param session
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String fileUploadMethod(@RequestParam("file")CommonsMultipartFile file, HttpSession session, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception{
        logger.info("start upload file");
        try {
            String fileDir = "XXXXXXXXX";//配置文件中获取
            File filePath = new File(fileDir);//清空上次的传输记录

            String createFileName = String.valueOf(System.currentTimeMillis());
            String name = new String();
            String common = new String();
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String fileExtension = FilenameUtils.getExtension(fileName);
                name = createFileName;
                common = fileName;

                createFileName = "/" + createFileName + "." + fileExtension;
                if (!ArrayUtils.contains(extension, fileExtension)) {
                    throw new NotSupportedException("No Support extentsion.");
                }

                file.transferTo(new File(filePath, name + "." + fileExtension));
            }

            logger.info("upload end");

            Map<String, Object> map = State.OK.toMap();
            map.put("name", name);
            map.put("common", common);
            map.put("url", createFileName);

            return PageUtil.getResultJsonArray(map, 1, "成功");
        }catch (Exception e){
            e.printStackTrace();
            return PageUtil.getResultJsonArray(null, 0, "失败");
        }
    }

    /**
     *    文件上传
     * @param file
     * @param session
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public @ResponseBody Map<String, Object> fileUploadDemo(@RequestParam("file")CommonsMultipartFile file, HttpSession session, HttpServletRequest request,
                   HttpServletResponse response) throws Exception{
        logger.info("start upload file");
        try {
            String fileDir = "XXXXXXXXX";//配置文件中获取
            File filePath = new File(fileDir);//清空上次的传输记录

            String createFileName = String.valueOf(System.currentTimeMillis());
            String name = new String();
            String common = new String();
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String fileExtension = FilenameUtils.getExtension(fileName);
                name = createFileName;
                common = fileName;

                createFileName = "/" + createFileName + "." + fileExtension;
                if (!ArrayUtils.contains(extension, fileExtension)) {
                    throw new NotSupportedException("No Support extentsion.");
                }

                file.transferTo(new File(filePath, createFileName));
            }

            logger.info("upload end");

            String path = "xxxxxxxx";//配置的路径
            String downPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            Map<String, Object> map = State.OK.toMap();
            map.put("name", name);
            map.put("common", common);
            map.put("url", createFileName);
            map.put("downPath",downPath + path + createFileName);

            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
