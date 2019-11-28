package com.demo.taotao.common.excel2pic.controller;

import com.demo.taotao.common.excel2pic.Excel2Pic;
import com.demo.taotao.common.excel2pic.KeeSeries;
import com.demo.taotao.common.excel2pic.KeeSeriesDetails;
import com.demo.taotao.common.excel2pic.KeeSpecimen;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoExport {

    //样品导出
//    @RequiresPermissions("specimen:keeSpecimen:view")
    @RequestMapping(value = "export", method= RequestMethod.POST)
    public void exportFile(KeeSpecimen keeSpecimen, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        List<KeeSpecimen> keeSpecimenList= new ArrayList<>();//keeSpecimenService.findList(keeSpecimen);
        // 生成Excel
        //excel标题
        String[] title={"编码","二维码","分类归属","中文名称","英文名称","主图","浏览数","下单数"};
        //excel名称
        String fileName = "样品数据.xls";
        //sheet名
        String sheetName = "样品信息";

//        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, keeSpecimenList,null);
        HSSFWorkbook wb = Excel2Pic.getHSSFworkBook(fileName,title,keeSpecimenList,null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();os.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    //主推导出
//    @RequiresPermissions("specimen:keeSeries:view")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public void exportFile(KeeSeries keeSeries, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        //获取所有的主推
        List<KeeSeries> keeSeriesList= new ArrayList<>();//keeSeriesService.findList(keeSeries);
        Map<String, List<KeeSpecimen>> map = new HashMap();
        //查询主推系列下面的样品
        for (KeeSeries keeSeries2 : keeSeriesList) {
            List<KeeSpecimen> keeSpecimenList = new ArrayList<KeeSpecimen>();
            KeeSeriesDetails keeSeriesDetails = new KeeSeriesDetails();
            keeSeriesDetails.setSeriesid(keeSeries2.getId());
            List<KeeSeriesDetails> keeSeriesDetailsList = new ArrayList<>();//keeSeriesDetailsDao.findList(keeSeriesDetails);
            if (!keeSeriesDetailsList.isEmpty()) {
                for (KeeSeriesDetails keeSeriesDetails2 : keeSeriesDetailsList) {
//                    keeSpecimenList.add(keeSpecimenService.get(keeSeriesDetails2.getSpecimenid()));
                    keeSpecimenList.add(new KeeSpecimen());
                }
            }
            map.put(keeSeries2.getId(), keeSpecimenList);
        }

        // 生成Excel
        //excel标题
        String[] title={"中文名称","英文名称","中文图片","英文图片","浏览量","下单量","发布状态"};
        String[] titleSpecimen={"编码","二维码","分类归属","中文名称","英文名称","主图","下单数/浏览数"};
        //excel名称
        String fileName = "主推系列数据.xls";

        HSSFWorkbook wb = Excel2Pic.getSeriesHSSFWorkbook( title, titleSpecimen, keeSeriesList,map,null);
        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();os.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
