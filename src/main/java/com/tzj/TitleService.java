package com.tzj;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.Style;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TitleService
 * @Description TODO
 * @Author QiBin
 * @Date 2022/2/2219:38
 * @Version 1.0
 **/
public class TitleService {

    static String resource = "/Users/qibin/Downloads/报告主体.docx";

    public static void main(String[] args) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("year", Texts.of("2022").style(Style.builder().buildUnderlinePatterns(UnderlinePatterns.SINGLE).build()).create());
        XWPFTemplate template = XWPFTemplate.compile(resource).render(data);
        FileOutputStream out;
        out = new FileOutputStream("/Users/qibin/Downloads/报告主体out.docx");
        template.write(out);
        out.flush();
        out.close();
        template.close();


    }


    private static class TextRenderData {
        public TextRenderData(String s) {

        }
    }
}
