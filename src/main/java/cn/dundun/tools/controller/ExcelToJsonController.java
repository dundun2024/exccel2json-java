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
        return """
                <!DOCTYPE html>
                <html lang="zh">
                <head>
                    <meta charset="UTF-8">
                    <title>Excel to JSON Converter</title>
                    <style>
                        #jsonBlock {
                            white-space: pre-wrap; /* 保持空白和换行 */
                            word-wrap: break-word; /* 长单词或URL换行 */
                            background-color: #f4f4f4; /* 背景色 */
                            padding: 10px; /* 内边距 */
                            border: 1px solid #ccc; /* 边框 */
                            max-height: 300px; /* 最大高度，可滚动 */
                            overflow-y: auto; /* 垂直滚动条 */
                        }
                        #copyButton {
                            margin-top: 10px; /* 上边距 */
                            padding: 5px 10px; /* 内边距 */
                            background-color: #007BFF; /* 背景色 */
                            color: white; /* 文字颜色 */
                            border: none; /* 无边框 */
                            cursor: pointer; /* 鼠标指针样式 */
                        }
                        #copyButton:hover {
                            background-color: #0056b3; /* 悬停背景色 */
                        }
                    </style>
                </head>
                <body>
                    <h1>Excel to JSON Converter</h1>
                    <input type="file" id="excelFile">
                    <button onclick="uploadFile()">Convert</button>
                    <div id="jsonBlock"></div> <!-- 用于显示JSON的块 -->
                    <button id="copyButton" onclick="copyText()">Copy</button> <!-- 复制按钮 -->
                
                    <script>
                        function uploadFile() {
                            const file = document.getElementById('excelFile').files[0];
                            const formData = new FormData();
                            formData.append('file', file);
                
                            fetch('/upload', {
                                method: 'POST',
                                body: formData
                            })
                            .then(response => response.json())
                            .then(data => {
                                document.getElementById('jsonBlock').textContent = JSON.stringify(data.data, null, 2);
                            })
                            .catch(error => console.error('Error:', error));
                        }
                
                        function copyText() {
                            const jsonBlock = document.getElementById('jsonBlock');
                            const text = jsonBlock.textContent;
                            navigator.clipboard.writeText(text).then(() => {
                                alert('JSON copied to clipboard!');
                            }).catch(err => {
                                console.error('Failed to copy text: ', err);
                            });
                        }
                    </script>
                </body>
                </html>""";
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
