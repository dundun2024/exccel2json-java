package cn.dundun.tools.controller;

import cn.dundun.tools.common.Response;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaotian
 * @since 2025/2/22 14:47
 */
@Slf4j
@RestController
public class ExcelToJsonController {

    @GetMapping("/")
    public String index() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"zh\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Excel to JSON Converter</title>\n" +
                "    <style>\n" +
                "        #jsonBlock {\n" +
                "            white-space: pre-wrap; /* 保持空白和换行 */\n" +
                "            word-wrap: break-word; /* 长单词或URL换行 */\n" +
                "            background-color: #f4f4f4; /* 背景色 */\n" +
                "            padding: 10px; /* 内边距 */\n" +
                "            border: 1px solid #ccc; /* 边框 */\n" +
                "            max-height: 300px; /* 最大高度，可滚动 */\n" +
                "            overflow-y: auto; /* 垂直滚动条 */\n" +
                "        }\n" +
                "        #copyButton {\n" +
                "            margin-top: 10px; /* 上边距 */\n" +
                "            padding: 5px 10px; /* 内边距 */\n" +
                "            background-color: #007BFF; /* 背景色 */\n" +
                "            color: white; /* 文字颜色 */\n" +
                "            border: none; /* 无边框 */\n" +
                "            cursor: pointer; /* 鼠标指针样式 */\n" +
                "        }\n" +
                "        #copyButton:hover {\n" +
                "            background-color: #0056b3; /* 悬停背景色 */\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Excel to JSON Converter</h1>\n" +
                "    <input type=\"file\" id=\"excelFile\">\n" +
                "    <button onclick=\"uploadFile()\">Convert</button>\n" +
                "    <div id=\"jsonBlock\"></div> <!-- 用于显示JSON的块 -->\n" +
                "    <button id=\"copyButton\" onclick=\"copyText()\">Copy</button> <!-- 复制按钮 -->\n" +
                "\n" +
                "    <script>\n" +
                "        function uploadFile() {\n" +
                "            const file = document.getElementById('excelFile').files[0];\n" +
                "            const formData = new FormData();\n" +
                "            formData.append('file', file);\n" +
                "\n" +
                "            fetch('/upload', {\n" +
                "                method: 'POST',\n" +
                "                body: formData\n" +
                "            })\n" +
                "            .then(response => response.json())\n" +
                "            .then(data => {\n" +
                "                document.getElementById('jsonBlock').textContent = JSON.stringify(data.data, null, 2);\n" +
                "            })\n" +
                "            .catch(error => console.error('Error:', error));\n" +
                "        }\n" +
                "\n" +
                "        function copyText() {\n" +
                "            const jsonBlock = document.getElementById('jsonBlock');\n" +
                "            const text = jsonBlock.textContent;\n" +
                "            navigator.clipboard.writeText(text).then(() => {\n" +
                "                alert('JSON copied to clipboard!');\n" +
                "            }).catch(err => {\n" +
                "                console.error('Failed to copy text: ', err);\n" +
                "            });\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }

    @PostMapping("/upload")
    public Response<?> excel2Json(@RequestParam("file")MultipartFile multipartFile) {

        log.info(">>>>>>>>> 开始解析文件：{}", multipartFile.getOriginalFilename());

        try {
            final List<Map<String, String>> list = new ArrayList<>();

            EasyExcel.read(multipartFile.getInputStream(), new ReadListener<Map<String, String>>() {

                @Override
                public void invoke(Map<String, String> data, AnalysisContext analysisContext) {
                    list.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }
            }).sheet().doRead();

            return Response.success(list);

        } catch (Exception e) {
            log.error(">>>>>>>>> 文件解析失败", e);
            throw new RuntimeException("文件解析失败");
        }
    }
}
