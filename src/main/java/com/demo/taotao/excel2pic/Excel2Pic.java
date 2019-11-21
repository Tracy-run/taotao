package com.demo.taotao.excel2pic;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

public class Excel2Pic {


    private static Logger logger= LoggerFactory.getLogger(Excel2Pic.class);


    public static HSSFWorkbook getHSSFworkBook(String sheetName, String []title, List<KeeSpecimen> list, HSSFWorkbook wb){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 650);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            sheet.setColumnWidth(i, 6000);
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            HSSFFont font = wb.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 15);//设置字体大小
            style.setFont(font);
            cell.setCellStyle(style);
        }
        BufferedImage bufferImg = null;//图片一
        BufferedImage bufferImg1 = null;//图片二
        try {
            //创建内容
            HSSFCellStyle styleCon = wb.createCellStyle();
            styleCon.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
            styleCon.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            for(int i=0;i<list.size();i++){
                row = sheet.createRow(i + 1);
                row.setHeight((short) 550);
                KeeSpecimen keeSpecimen = list.get(i);
                //将内容按顺序赋给对应的列对象
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOut1 = new ByteArrayOutputStream();
                HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                //将两张图片读到BufferedImage
                String qrcode = "/static/images/qrcode/"+keeSpecimen.getRes1();
                qrcode = request.getSession().getServletContext().getRealPath(qrcode);
                if (new File(qrcode).exists()) {
                    bufferImg = ImageIO.read(new File(qrcode));
                    ImageIO.write(bufferImg, "jpg", byteArrayOut);
                    //图片一导出到单元格B2中
                    HSSFClientAnchor anchor = new HSSFClientAnchor(480, 30, 700, 250,
                            (short) 1, i+1, (short) 1, i+1);
                    // 插入图片
                    patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                            .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
                //获取图片路径并且处理
                String baseImage = keeSpecimen.getBaseImage();
                baseImage = baseImage.replaceFirst("/kee", "");
                baseImage = request.getSession().getServletContext().getRealPath(baseImage);
                //判断图片是否存在
                if (new File(baseImage).exists()) {
                    bufferImg1 = ImageIO.read(new File(baseImage));
                    ImageIO.write(bufferImg1, FileUtils.getFileExtension(keeSpecimen.getBaseImage()), byteArrayOut1);
                    //图片一导出到单元格B6中
                    HSSFClientAnchor anchor1 = new HSSFClientAnchor(400, 30, 700, 220,
                            (short) 5, i+1, (short) 5, i+1);
                    patriarch.createPicture(anchor1, wb.addPicture(byteArrayOut1
                            .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
                cell = row.createCell(0);cell.setCellValue(keeSpecimen.getCode());
                cell.setCellStyle(styleCon);
                //DictUtils是我的字典工具类，根据业务场景需要，有的朋友纠结，特此声明
//                cell = row.createCell(2);cell.setCellValue(DictUtils.getDictLabel(keeSpecimen.getType(), "specimen_type", ""));
                cell = row.createCell(2);cell.setCellValue(keeSpecimen.getType());
                cell.setCellStyle(styleCon);
                cell = row.createCell(3);cell.setCellValue(keeSpecimen.getChName());
                cell.setCellStyle(styleCon);
                cell = row.createCell(4);cell.setCellValue(keeSpecimen.getEnName());
                cell.setCellStyle(styleCon);
                cell = row.createCell(6);cell.setCellValue(keeSpecimen.getLookNum());
                cell.setCellStyle(styleCon);
                cell = row.createCell(7);cell.setCellValue(keeSpecimen.getOrdersNum());
                cell.setCellStyle(styleCon);
            }
            return wb;
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
        }
        return wb;
    }


    /**
     * 导出Excel（主推）
     * @param title
     * @param keys
     * @param list
     * @param map
     * @param wb
     * @return
     */
    public static HSSFWorkbook getSeriesHSSFWorkbook(String [] title, String [] keys, List<KeeSeries> list, Map<String, List<KeeSpecimen>> map, HSSFWorkbook wb){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }
        //样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        HSSFCellStyle styleCon = wb.createCellStyle();
        styleCon.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        styleCon.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //字体
        HSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 17);//设置字体大小
        style.setFont(font);
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet（根据主推来创建）
        for (KeeSeries keeSeries : list) {
            HSSFSheet sheet = wb.createSheet(keeSeries.getChName());
            HSSFCell cell = null;
            //设置表头的列宽
            for (int i = 0; i < title.length; i++) {
                sheet.setColumnWidth(i, 6000);
            }
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
            //设置主推表头
            HSSFRow row = sheet.createRow(0);
            row.setHeight((short) 1000);
            cell=row.createCell(0);
            cell.setCellValue("主推系列");
            cell.setCellStyle(style);
            //合并第一行，6列
            CellRangeAddress region=new CellRangeAddress(0, 0, 0,6);
            sheet.addMergedRegion(region);
            //设置样品表头
            HSSFRow rowThree = sheet.createRow(3);
            rowThree.setHeight((short) 1000);
            cell=rowThree.createCell(0);
            cell.setCellValue("系列样品");
            cell.setCellStyle(style);
            CellRangeAddress regionT=new CellRangeAddress(3, 3, 0,6);
            sheet.addMergedRegion(regionT);

            //创建主推标题
            HSSFRow rowOne = sheet.createRow(1);
            HSSFRow rowFour = sheet.createRow(4);
            HSSFCell cellFour = null;
            for(int i=0;i<title.length;i++){
                rowOne.setHeight((short) 650);
                rowFour.setHeight((short) 550);
                cell = rowOne.createCell(i);
                cell.setCellValue(title[i]);
                cellFour = rowFour.createCell(i);
                cellFour.setCellValue(keys[i]);
                font.setFontHeightInPoints((short) 14);//设置字体大小
                style.setFont(font);
                cell.setCellStyle(style);
                cellFour.setCellStyle(style);
            }

            BufferedImage bufferImg = null;//图片一
            BufferedImage bufferImg1 = null;//图片二
            try {
                //主推系列数据
                HSSFRow rowTwo = sheet.createRow(2);
                rowTwo.setHeight((short) 550);
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                ByteArrayOutputStream byteArrayOut1 = new ByteArrayOutputStream();
                HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
                //将两张图片读到BufferedImage
                String chImage = keeSeries.getChImage();
                chImage = chImage.replaceFirst("/kee", "");
                chImage = request.getSession().getServletContext().getRealPath(chImage);
                if (new File(chImage).exists()) {
                    bufferImg = ImageIO.read(new File(chImage));
                    ImageIO.write(bufferImg, FileUtils.getFileExtension(keeSeries.getChImage()), byteArrayOut);
                    //图片一导出到单元格B2中
                    HSSFClientAnchor anchor = new HSSFClientAnchor(480, 30, 700, 250,
                            (short) 2, 2, (short) 2, 2);
                    // 插入图片
                    patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                            .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }

                String enImage = keeSeries.getEnImage();
                enImage = enImage.replaceFirst("/kee", "");
                enImage = request.getSession().getServletContext().getRealPath(enImage);
                if (new File(enImage).exists()) {
                    bufferImg1 = ImageIO.read(new File(enImage));
                    ImageIO.write(bufferImg1, FileUtils.getFileExtension(keeSeries.getEnImage()), byteArrayOut1);
                    //图片一导出到单元格B6中
                    HSSFClientAnchor anchor1 = new HSSFClientAnchor(400, 30, 700, 220,
                            (short) 3, 2, (short) 3, 2);
                    patriarch.createPicture(anchor1, wb.addPicture(byteArrayOut1
                            .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
                cell=rowTwo.createCell(0);cell.setCellValue(keeSeries.getChName());cell.setCellStyle(styleCon);
                cell=rowTwo.createCell(1);cell.setCellValue(keeSeries.getEnName());cell.setCellStyle(styleCon);
                cell=rowTwo.createCell(4);cell.setCellValue(keeSeries.getLookNum());cell.setCellStyle(styleCon);
                cell=rowTwo.createCell(5);cell.setCellValue(keeSeries.getOrdersNum());cell.setCellStyle(styleCon);
                cell=rowTwo.createCell(6);cell.setCellStyle(styleCon);
                cell.setCellValue(keeSeries.getRes1());cell.setCellStyle(styleCon);

                //查询主推下边的样品
                List<KeeSpecimen> keeSpecimenList = map.get(keeSeries.getId());
                if (!keeSpecimenList.isEmpty()) {
                    for(int i=0;i<keeSpecimenList.size();i++){
                        row = sheet.createRow(i + 5);
                        row.setHeight((short) 550);
                        KeeSpecimen keeSpecimen = keeSpecimenList.get(i);
                        //将两张图片读到BufferedImage
                        String qrcode = "/static/images/qrcode/"+keeSpecimen.getRes1();
                        qrcode = request.getSession().getServletContext().getRealPath(qrcode);
                        if (new File(qrcode).exists()) {
                            bufferImg = ImageIO.read(new File(qrcode));
                            ImageIO.write(bufferImg, "jpg", byteArrayOut);
                            //图片一导出到单元格B2中
                            HSSFClientAnchor anchor = new HSSFClientAnchor(480, 30, 700, 250,
                                    (short) 1, i+5, (short) 1, i+5);
                            // 插入图片
                            patriarch.createPicture(anchor, wb.addPicture(byteArrayOut
                                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }

                        String baseImage = keeSpecimen.getBaseImage();
                        baseImage = baseImage.replaceFirst("/kee", "");
                        baseImage = request.getSession().getServletContext().getRealPath(baseImage);
                        if (new File(baseImage).exists()) {
                            bufferImg1 = ImageIO.read(new File(baseImage));
                            //FileUtils.getFileExtension获取图片的后缀名，是jpg还是png
                            ImageIO.write(bufferImg1, FileUtils.getFileExtension(keeSpecimen.getBaseImage()), byteArrayOut1);
                            //图片一导出到单元格B6中
                            HSSFClientAnchor anchor1 = new HSSFClientAnchor(400, 30, 700, 220,
                                    (short) 5, i+5, (short) 5, i+5);
                            patriarch.createPicture(anchor1, wb.addPicture(byteArrayOut1
                                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
                        }
                        cell=row.createCell(0);cell.setCellValue(keeSpecimen.getCode());cell.setCellStyle(styleCon);
//                        cell=row.createCell(2);cell.setCellValue(DictUtils.getDictLabel(keeSpecimen.getType(), "specimen_type", ""));
                        cell=row.createCell(2);cell.setCellValue(keeSpecimen.getType());
                        cell.setCellStyle(styleCon);
                        cell=row.createCell(3);cell.setCellValue(keeSpecimen.getChName());cell.setCellStyle(styleCon);
                        cell=row.createCell(4);cell.setCellValue(keeSpecimen.getEnName());cell.setCellStyle(styleCon);
                        cell=row.createCell(6);cell.setCellValue(keeSpecimen.getOrdersNum()+" / "+keeSpecimen.getLookNum());
                        cell.setCellStyle(styleCon);
                    }
                }

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }

        return wb;
    }




}


class FileUtils{


    public static String getFileExtension(String chImage) {
        return null;
    }
}