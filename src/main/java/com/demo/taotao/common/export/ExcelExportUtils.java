package com.demo.taotao.common.export;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.sun.rowset.internal.Row;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;


/**
 * 导出非图片的excel
 */
public class ExcelExportUtils {
        /**
         * @param headers 表头
         * @param keys 用来在 rows里获取每一个单元格数据
         * @param rows
         * @param title
         */
        public static String exportExcel(String[] headers,String[] keys,List<Map<String,Object>> rows,String title,boolean isOrderNumber,int... colOver){
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(title);//创建table工作薄
            title = title + new Date().getTime();;
            //设置样式 -- 单元格
            HSSFCellStyle style = wb.createCellStyle();
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
            //设置样式 -- 表头
            HSSFCellStyle styleHeader = wb.createCellStyle();
            styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            styleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            styleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            styleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
            //颜色
            styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);

            HSSFRow row;
            HSSFCell cell;
            //第一列是序号
            if(isOrderNumber){
                //创建表头行
                row = sheet.createRow(0);
                cell = row.createCell(0);
                HSSFFont f  = wb.createFont();
                f.setFontHeightInPoints((short) 11);//字号
                f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                styleHeader.setFont(f);
                cell.setCellStyle(styleHeader);

                cell.setCellValue("序号");
                for (int i = 0;i < headers.length;i++) {
                    cell = row.createCell(i + 1);//根据表格行创建单元格
                    //设置样式
                    f.setFontHeightInPoints((short) 11);//字号
                    f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                    style.setFont(f);
                    cell.setCellStyle(styleHeader);

                    cell.setCellValue(headers[i]);
                }
                //填充表格内容
                int j = 1;
                for (Map<String,Object> map : rows) {
                    row = sheet.createRow(j);//创建表格行
                    cell = row.createCell(0);
                    HSSFFont f1  = wb.createFont();
                    f1.setFontHeightInPoints((short) 11);//字号
                    f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                    style.setFont(f1);
                    cell.setCellStyle(style);

                    cell.setCellValue(String.valueOf(j));

                    for (int k = 0;k < keys.length;k++) {
                        cell = row.createCell(k + 1);//根据表格行创建单元格
                        //设置样式
                        cell.setCellStyle(style);

                        if (keys[k].equals("confirmUserName")) {
                            cell.setCellValue(" ");
                        }else {
                            cell.setCellValue(String.valueOf(MapUtils.getObject(map, keys[k],"")));
                        }
                    }
                    j++;
                }
                //设置列宽
                for(int i = 0;i < headers.length + 1;i++){
                    if(0 == i ){
                        sheet.setColumnWidth(i, 1000); //设置列宽
                    }else{
                        sheet.setColumnWidth(i, 6000); //设置列宽
                        if (i == 2 && title.indexOf("roleList") >= 0 && "状态".equals(headers[1])) { //如果是权限的第三列
                            sheet.setColumnWidth(i, 60000); //设置列宽
                        }
                    }

                }

                //第一列没有序号
            }else{
                //创建表头行
                row = sheet.createRow(0);
                for (int i = 0;i < headers.length;i++) {
                    cell = row.createCell(i);//根据表格行创建单元格
                    HSSFFont f  = wb.createFont();
                    f.setFontHeightInPoints((short) 11);//字号
                    f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                    styleHeader.setFont(f);
                    cell.setCellStyle(styleHeader);

                    cell.setCellValue(headers[i]);
                }
                //填充表格内容
                int j = 1;
                for (Map<String,Object> map : rows) {
                    row = sheet.createRow(j);//创建表格行
                    for (int k = 0;k < keys.length;k++) {
                        cell = row.createCell(k);//根据表格行创建单元格
                        HSSFFont f  = wb.createFont();
                        f.setFontHeightInPoints((short) 11);//字号
                        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                        cell.setCellStyle(style);

                        if (keys[k].equals("confirmUserName")) {
                            cell.setCellValue(" ");
                        }else {
                            cell.setCellValue(String.valueOf(MapUtils.getObject(map, keys[k],"")));
                        }

                    }
                    j++;
                }
                //设置列宽
                for(int i = 0;i < headers.length + 1;i++){
                    if(colOver!=null && colOver.length>0 ){
                        if(colOver[0] == (i+1)){
                            sheet.setColumnWidth(i, 12000); //设置列宽
                            break;
                        }else{
                            sheet.setColumnWidth(i, 6000); //设置列宽
                        }
                    }else{
                        sheet.setColumnWidth(i, 6000); //设置列宽
                    }

                }
            }
            try {
                String upload= "d:/java/apache-tomcat-7.0.54/webapps/uploadFiles/jssh";
               // String url = Configuration.getReourcesV("upload") + "/export/" + title + ".xls";
                String url = upload + "/export/" + title + ".xls";
                FileOutputStream fileOutputStreamnew = new FileOutputStream(url);
                wb.write(fileOutputStreamnew);
                //wb.write(new FileOutputStream("D:/" + title + ".xls"));
                fileOutputStreamnew.close();
                String[] path = url.split("/");
//			return path[4] + "/" + path[5] + "/" + path[6] + "/" +  title  + ".xls";
                return title  + ".xls";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    /**
     *
     * @param headerss 表头
     * @param keyss 用来在 rows里获取每一个单元格数据
     * @param rowss
     * @param titles
     * @param isOrderNumber
     * @param name
     * @param colOver
     * @return
     */
        public static String exportExcelSheets(List<String[]> headerss,List<String[]> keyss,List<List<Map<String,Object>>> rowss,List<String> titles,boolean isOrderNumber,String name,int... colOver){
            HSSFWorkbook wb = new HSSFWorkbook();
            for(int w = 0;w < rowss.size();w++) {
                List<Map<String,Object>> rows = rowss.get(w);
                String title = titles.get(w);
                HSSFSheet sheet = wb.createSheet();//创建table工作薄

                title = title + new Date().getTime();
                //设置样式 -- 单元格
                HSSFCellStyle style = wb.createCellStyle();
                style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
                //设置样式 -- 表头
                HSSFCellStyle styleHeader = wb.createCellStyle();
                styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                styleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
                styleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
                styleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
                styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
                //颜色
                styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
                HSSFRow row;
                HSSFCell cell;
                //第一列是序号
                if(isOrderNumber){
                    //创建表头行
                    row = sheet.createRow(0);
                    cell = row.createCell(0);
                    HSSFFont f  = wb.createFont();
                    f.setFontHeightInPoints((short) 11);//字号
                    f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                    styleHeader.setFont(f);
                    cell.setCellStyle(styleHeader);

                    cell.setCellValue("序号");

                    String[] headers = headerss.get(w);
                    for (int i = 0;i < headers.length;i++) {
                        cell = row.createCell(i + 1);//根据表格行创建单元格
                        //设置样式
                        f.setFontHeightInPoints((short) 11);//字号
                        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                        style.setFont(f);
                        cell.setCellStyle(styleHeader);

                        cell.setCellValue(headers[i]);
                    }
                    //填充表格内容
                    int j = 1;
                    for (Map<String,Object> map : rows) {
                        row = sheet.createRow(j);//创建表格行
                        cell = row.createCell(0);
                        HSSFFont f1  = wb.createFont();
                        f1.setFontHeightInPoints((short) 11);//字号
                        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                        style.setFont(f1);
                        cell.setCellStyle(style);

                        cell.setCellValue(String.valueOf(j));

                        String[] keys = keyss.get(w);
                        for (int k = 0;k < keys.length;k++) {
                            cell = row.createCell(k + 1);//根据表格行创建单元格
                            //设置样式
                            cell.setCellStyle(style);

                            if (keys[k].equals("confirmUserName")) {
                                cell.setCellValue(" ");
                            }else {
                                cell.setCellValue(String.valueOf(MapUtils.getObject(map, keys[k],"")));
                            }
                        }
                        j++;
                    }
                    //设置列宽
                    for(int i = 0;i < headers.length + 1;i++){
                        if(0 == i ){
                            sheet.setColumnWidth(i, 1000); //设置列宽
                        }else{
                            sheet.setColumnWidth(i, 6000); //设置列宽
                            if (i == 2 && title.indexOf("roleList") >= 0 && "状态".equals(headers[1])) { //如果是权限的第三列
                                sheet.setColumnWidth(i, 60000); //设置列宽
                            }
                        }

                    }

                    //第一列没有序号
                }else{
                    //创建表头行
                    row = sheet.createRow(0);
                    String[] headers = headerss.get(w);
                    for (int i = 0;i < headers.length;i++) {
                        cell = row.createCell(i);//根据表格行创建单元格
                        HSSFFont f  = wb.createFont();
                        f.setFontHeightInPoints((short) 11);//字号
                        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                        styleHeader.setFont(f);
                        cell.setCellStyle(styleHeader);

                        cell.setCellValue(headers[i]);
                    }
                    //填充表格内容
                    int j = 1;
                    String[] keys = keyss.get(w);
                    for (Map<String,Object> map : rows) {
                        row = sheet.createRow(j);//创建表格行
                        for (int k = 0;k < keys.length;k++) {
                            cell = row.createCell(k);//根据表格行创建单元格
                            HSSFFont f  = wb.createFont();
                            f.setFontHeightInPoints((short) 11);//字号
                            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                            cell.setCellStyle(style);

                            if (keys[k].equals("confirmUserName")) {
                                cell.setCellValue(" ");
                            }else {
                                cell.setCellValue(String.valueOf(MapUtils.getObject(map, keys[k],"")));
                            }

                        }
                        j++;
                    }
                    //设置列宽
                    for(int i = 0;i < headers.length + 1;i++){
                        if(colOver!=null && colOver.length>0 ){
                            if(colOver[0] == (i+1)){
                                sheet.setColumnWidth(i, 12000); //设置列宽
                                break;
                            }else{
                                sheet.setColumnWidth(i, 6000); //设置列宽
                            }
                        }else{
                            sheet.setColumnWidth(i, 6000); //设置列宽
                        }

                    }
                }
            }
            try {
                String upload= "d:/java/apache-tomcat-7.0.54/webapps/uploadFiles/jssh";
                // String url = Configuration.getReourcesV("upload") + "/export/" + title + ".xls";
                String url = upload + "/export/" + name + ".xls";
                FileOutputStream fileOutputStreamnew = new FileOutputStream(url);
                wb.write(fileOutputStreamnew);
                //wb.write(new FileOutputStream("D:/" + title + ".xls"));
                fileOutputStreamnew.close();
                String[] path = url.split("/");
//			return path[4] + "/" + path[5] + "/" + path[6] + "/" +  title  + ".xls";
                return name  + ".xls";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        public static String exportExcelSheet(List<Map<String,Object>> maps,String title,boolean isOrderNumber,int... colOver){
            HSSFWorkbook wb = new HSSFWorkbook();
            for(Map<String,Object> m:maps){
                String[] headers = (String[])m.get("headers");
                String[] keys = (String[]) m.get("keys");
                String sheetTitle  = (String) m.get("sheetTitle");
                List<Map<String,Object>> rows = (List<Map<String, Object>>) m.get("rows");

                HSSFSheet sheet =wb.createSheet(sheetTitle);//创建table工作薄
                title = title+ new Date().getTime();
                //设置样式 -- 单元格
                HSSFCellStyle style = wb.createCellStyle();
                style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
                style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
                style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
                //设置样式 -- 表头
                HSSFCellStyle styleHeader = wb.createCellStyle();
                styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                styleHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                styleHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
                styleHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
                styleHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
                styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
                //颜色
                styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);

                HSSFRow row;
                HSSFCell cell;
                //第一列是序号
                if(isOrderNumber){
                    //创建表头行
                    row = sheet.createRow(0);
                    cell = row.createCell(0);
                    HSSFFont f  = wb.createFont();
                    f.setFontHeightInPoints((short) 11);//字号
                    f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                    styleHeader.setFont(f);
                    cell.setCellStyle(styleHeader);

                    cell.setCellValue("序号");
                    for (int i = 0;i < headers.length;i++) {
                        cell = row.createCell(i + 1);//根据表格行创建单元格
                        //设置样式
                        f.setFontHeightInPoints((short) 11);//字号
                        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                        style.setFont(f);
                        cell.setCellStyle(styleHeader);

                        cell.setCellValue(headers[i]);
                    }
                    //填充表格内容
                    int j = 1;
                    for (Map<String,Object> map : rows) {
                        row = sheet.createRow(j);//创建表格行
                        cell = row.createCell(0);
                        HSSFFont f1  = wb.createFont();
                        f1.setFontHeightInPoints((short) 11);//字号
                        f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                        style.setFont(f1);
                        cell.setCellStyle(style);

                        cell.setCellValue(String.valueOf(j));

                        for (int k = 0;k < keys.length;k++) {
                            cell = row.createCell(k + 1);//根据表格行创建单元格
                            //设置样式
                            cell.setCellStyle(style);

                            if (keys[k].equals("confirmUserName")) {
                                cell.setCellValue(" ");
                            }else {
                                cell.setCellValue(String.valueOf(MapUtils.getObject(map, keys[k],"")));
                            }
                        }
                        j++;
                    }
                    //设置列宽
                    for(int i = 0;i < headers.length + 1;i++){
                        if(0 == i ){
                            sheet.setColumnWidth(i, 1000); //设置列宽
                        }else{
                            sheet.setColumnWidth(i, 6000); //设置列宽
                            if (i == 2 && title.indexOf("roleList") >= 0 && "状态".equals(headers[1])) { //如果是权限的第三列
                                sheet.setColumnWidth(i, 60000); //设置列宽
                            }
                        }

                    }

                    //第一列没有序号
                }else{
                    //创建表头行
                    row = sheet.createRow(0);
                    if("异常设备数趋势".equals(sheetTitle)){

                        byte[] picInfoByte= (byte[]) rows.get(0).get("picinfo");
                        if(null!=picInfoByte){
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream(); // 将图片写入流中
                            ByteArrayInputStream in = new ByteArrayInputStream(picInfoByte);    //将picInfoByte作为输入流；
                            BufferedImage bufferImg;
                            try {
                                bufferImg = ImageIO.read(in);
                                ImageIO.write(bufferImg, "PNG", outStream); // 利用HSSFPatriarch将图片写入EXCEL

                                HSSFPatriarch patri = sheet.createDrawingPatriarch();
                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255,255,
                                        (short) 1, 1, (short) 5, 8);
                                anchor.setAnchorType(3);
                                patri.createPicture(anchor, wb.addPicture(
                                        outStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG));
                                createTitle(sheet,"异常设备数趋势");
                                try {
                                    wb.write(outStream);
                                    outStream.flush();
                                    outStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }     //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
                        }
                    }else{
                        for (int i = 0;i < headers.length;i++) {
                            cell = row.createCell(i);//根据表格行创建单元格
                            HSSFFont f  = wb.createFont();
                            f.setFontHeightInPoints((short) 11);//字号
                            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                            styleHeader.setFont(f);
                            cell.setCellStyle(styleHeader);

                            cell.setCellValue(headers[i]);
                        }
                        //填充表格内容
                        int j = 1;
                        for (Map<String,Object> map : rows) {
                            row = sheet.createRow(j);//创建表格行
                            for (int k = 0;k < keys.length;k++) {
                                cell = row.createCell(k);//根据表格行创建单元格
                                HSSFFont f  = wb.createFont();
                                f.setFontHeightInPoints((short) 11);//字号
                                f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加粗
                                cell.setCellStyle(style);

                                if (keys[k].equals("confirmUserName")) {
                                    cell.setCellValue(" ");
                                }else {
                                    cell.setCellValue(String.valueOf(MapUtils.getObject(map, keys[k],"")));
                                }

                            }
                            j++;
                        }
                        //设置列宽
                        for(int i = 0;i < headers.length + 1;i++){
                            if(colOver!=null && colOver.length>0 ){
                                if(colOver[0] == (i+1)){
                                    sheet.setColumnWidth(i, 12000); //设置列宽
                                    break;
                                }else{
                                    sheet.setColumnWidth(i, 6000); //设置列宽
                                }
                            }else{
                                sheet.setColumnWidth(i, 6000); //设置列宽
                            }

                        }
                    }

                }
                try {
                    String upload= "d:/java/apache-tomcat-7.0.54/webapps/uploadFiles/jssh";
                    // String url = Configuration.getReourcesV("upload") + "/export/" + title + ".xls";
                    String url = upload + "/export/" + title + ".xls";
                    FileOutputStream fileOutputStreamnew = new FileOutputStream(url);
                    wb.write(fileOutputStreamnew);
                    //wb.write(new FileOutputStream("D:/" + title + ".xls"));
                    fileOutputStreamnew.close();
                    String[] path = url.split("/");
//				return path[4] + "/" + path[5] + "/" + path[6] + "/" +  title  + ".xls";
                    return  title  + ".xls";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return  null;
        }

    /**
     *
     * @param imgsURl
     * @return
     */
    public static byte[] base64TObyte(String imgsURl) {
            byte[] buffer;
            if (imgsURl == null)
                return null;
            try
            {
                String[] url = imgsURl.split(",");
                String u = url[1];
                //Base64
                buffer = new BASE64Decoder().decodeBuffer(u);
                return buffer;
            }
            catch (Exception e)
            {
                System.out.println(e);
                return null;
            }
        }

        private static void createTitle(HSSFSheet sheet, String brandName) {
            HSSFRow row = sheet.createRow(1);
            HSSFCell titalCell = row.createCell(5);
            titalCell.setCellValue(brandName);

        }
        public static void main(String[] args) {
            String[] a = {"表头1","表头2","表头3"};
            String[] b = {"1","2","3"};
            String[] c = {"测试1","测试2","测试3"};
            List<Map<String,Object>> rows = new ArrayList<Map<String,Object>>();
            Map<String,Object> map1 = new HashMap<String, Object>();
            map1.put("1", "内容11");
            map1.put("2", "内容12");
            map1.put("3", "内容13");
            Map<String,Object> map2 = new HashMap<String, Object>();
            map2.put("1", "内容21");
            map2.put("2", "内容22");
            map2.put("3", "内容23");
            rows.add(map1);
            rows.add(map2);
        }

    /**
     * 读取excel中的获取数据  需要使用poi 4.0.1 以上版本
     * @param file
     * @return
     * @throws Exception
     */
  /*  private static List<Map<String,String>> readExcel(File file,String []keyList){
        List<Map<String,String>> wordList = new ArrayList<>();
        try {
            // 创建输入流，读取Excel
            FileInputStream is = new FileInputStream(file.getAbsolutePath());
            Workbook workbook = new HSSFWorkbook(is);

            for(int n = 0;n < workbook.getNumberOfSheets();n++){
                Sheet sheet = workbook.getSheetAt(n);
                //获取每个表
                if(sheet == null){
                    continue;
                }
                System.out.println("sheet num "+sheet.getLastRowNum());
                for(int r =0;r<sheet.getLastRowNum();r++){
                    //行信息
                    Row row = sheet.getRow(r);
                    //从第一行开始获取到最后一行
                    if(row != null){
                        short cellNum = row.getLastCellNum();
                        Map<String,String> map = new HashMap<>();
                        for(int a = 0;a<cellNum;a++){
                            Cell numCell = row.getCell(a);
                            if(numCell != null){
                                numCell.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            map.put(keyList[a], numCell.getStringCellValue());
                        }
                        wordList.add(map);

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        finally{
            return wordList;
        }

    }*/


    /*
     * 导入Excel文件
     * @param obj	导入文件数据对应的实体类
     * @param request	HttpServletRequest请求request
     * @return	解析后数据集合
     */
    public List<Object> importExcel(Object obj, HttpServletRequest request) {
        try {
            // 将请求转化为多部件的请求
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            // 解析多部件请求文件
            MultipartFile mFile = multipartRequest.getFile("importFile");
            // 获得上传文件的文件名
            String fileName = mFile.getOriginalFilename();
            // 获取文件扩展名
            String eName = fileName.substring(fileName.lastIndexOf(".")+1);
            InputStream inputStream = mFile.getInputStream();
            Workbook workbook = getWorkbook(inputStream, eName);
            // 获取工作薄第一张表
            Sheet sheet = workbook.getSheetAt(0);
            // 获取名称
            String sheetName = sheet.getSheetName().trim();
            // 获取第一行;




        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    /*
     * 根据excel文件格式获知excel版本信息
     */
    public static Workbook getWorkbook(InputStream fs,String str){
        Workbook book = null;
        try{
            if ("xls".equals(str)) {
                // 2003
                book = new HSSFWorkbook(fs);
            } else {
                // 2007
                book = new XSSFWorkbook(fs);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

}
