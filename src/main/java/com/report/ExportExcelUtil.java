package com.report;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

/**
 * @ClassName ExportExcelUtil
 * @Description 导出加密文件
 * @Author QiBin
 * @Date 2023/2/17 11:17
 * @Version 1.0
 **/
@Slf4j
public class ExportExcelUtil {
    /**
     * 导出Excel
     *
     * @param workbook workbook流
     * @param fileName 文件名
     * @param response 响应
     */
    public static void exportExcel(Workbook workbook, String fileName, HttpServletResponse response) {
        // 输出文件
        try (OutputStream out = response.getOutputStream()) {
            // 获取文件名并转码
            String name = URLEncoder.encode(fileName, "UTF-8");
            // 编码
            response.setCharacterEncoding("UTF-8");
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");
            // 输出表格
            workbook.write(out);
        } catch (IOException e) {
            log.error("文件导出异常,详情如下:{}", e);
        } finally {
            try {
                if (workbook != null) {
                    // 关闭输出流
                    workbook.close();
                }
            } catch (IOException e) {
                log.error("文件导出关闭输出流失败,详情如下:{}", e);
            }
        }
    }


    /**
     * 导出一个需要密码打开的Excel
     *
     * @param workbook workbook流
     * @param fileName 文件名
     * @param response 响应
     */
    public static void exportEncryptExcel(Workbook workbook, String fileName, HttpServletResponse response, String password) throws Exception {
        //
        if (password != null && !"".equals(password)) {
            // 文件名
            fileName = fileName + ".xlsx";

            // 创建一个字节数组输出流
            ByteArrayOutputStream workbookOutput = new ByteArrayOutputStream();
            workbook.write(workbookOutput);
            workbookOutput.flush();

            // 创建一个字节数组输入流
            ByteArrayInputStream workbookInput = new ByteArrayInputStream(workbookOutput.toByteArray());

            // 加密
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
            Encryptor enc = info.getEncryptor();
            enc.confirmPassword(password);

            // 创建一个POIFS 文件系统
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem();
            OPCPackage opc = OPCPackage.open(workbookInput);
            OutputStream outputStream = enc.getDataStream(poifsFileSystem);
            opc.save(outputStream);
            outputStream.close();
            opc.close();
            workbookOutput = new ByteArrayOutputStream();
            poifsFileSystem.writeFilesystem(workbookOutput);
            workbookOutput.flush();

            // 获取文件名并转码
            String name = URLEncoder.encode(fileName, "UTF-8");
            // 编码
            response.setCharacterEncoding("UTF-8");
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            byte[] buff = new byte[1024];
            BufferedInputStream bufferedInputStream = null;

            try {
                OutputStream responseOutputStream = response.getOutputStream();
                bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(workbookOutput.toByteArray()));

                for (int i = bufferedInputStream.read(buff); i != -1; i = bufferedInputStream.read(buff)) {
                    responseOutputStream.write(buff, 0, buff.length);
                    responseOutputStream.flush();
                }
            } catch (IOException e) {
                log.error("文件导出失败,详情如下:{}", e);
            } finally {
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        log.error("文件导出关闭输出流失败,详情如下:{}", e);
                    }
                }

            }
        }

    }


    /**
     *解密xls
     * @param FILE 文件名
     * @param password 密码
     */
    public static void decryptExcel_xls(String FILE, String password){
        try{
            POIFSFileSystem pfs = new POIFSFileSystem(new FileInputStream(FILE));
            //解密，这个密码不是指保护工作表和工作博密码，而是打开文件密码
            Biff8EncryptionKey.setCurrentUserPassword(password);
            HSSFWorkbook wb = new HSSFWorkbook(pfs);
            //读取测试
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.getCell(0);
            System.out.println(cell.getStringCellValue());
            wb.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
        }
    }
    /**
     * 解密xlsx
     * @param FILE 文件名
     * @param password 密码
     * @throws Exception
     */
    public static void decryptExcel_xlsx(String FILE, String password) throws Exception{
        Workbook wb = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(FILE);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
            EncryptionInfo encInfo = new EncryptionInfo(poifsFileSystem);
            Decryptor decryptor = Decryptor.getInstance(encInfo);
            decryptor.verifyPassword(password);
            wb = new XSSFWorkbook(decryptor.getDataStream(poifsFileSystem));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            in.close();
        }
        System.out.println("=================================");
        System.out.println("Number of Sheets:" + wb.getNumberOfSheets());
        System.out.println("Sheet1's name:" + wb.getSheetName(0));
        System.out.println();
    }

    /**
     * 加密Excl
     * @param FILE
     * @param password
     * @throws IOException
     * @throws InvalidFormatException
     * @throws GeneralSecurityException
     */
    public static void encryptExcl(String FILE, String password) throws IOException, InvalidFormatException, GeneralSecurityException {
        if (FILE.toLowerCase().endsWith("xlsx")) {
            System.out.println("=====加密 xlsx===="+FILE);
            // 设置密 码 保 护 ·
            POIFSFileSystem fs = new POIFSFileSystem();
            EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
            Encryptor enc = info.getEncryptor();
            //设置密码
            enc.confirmPassword(password);
            //加密文件
            OPCPackage opc = OPCPackage.open(new File(FILE), PackageAccess.READ_WRITE);
            OutputStream os = enc.getDataStream(fs);
            opc.save(os);
            opc.close();
            // 这一步特别注意，导出之前一定要先关闭加密文件流，不然导出文件会损坏而无法打开
            os.close();
            //把加密后的文件写回到流
            FileOutputStream fos = new FileOutputStream(FILE);
            fs.writeFilesystem(fos);
            fos.close();
        } else {
            System.out.println("=====加密 xls===="+FILE);
            POIFSFileSystem poif = new POIFSFileSystem(new FileInputStream(FILE));
            HSSFWorkbook wb = new HSSFWorkbook(poif);
            // 设置密 码 保 护 ·
            Biff8EncryptionKey.setCurrentUserPassword(password);
            wb.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "管理员");
            wb.unwriteProtectWorkbook();
            // 写入excel文件
            FileOutputStream fileOut = new FileOutputStream(FILE);
            wb.write(fileOut);
            fileOut.close();
        }

        System.out.println("over");

    }

}
