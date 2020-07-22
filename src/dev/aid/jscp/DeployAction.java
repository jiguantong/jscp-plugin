package dev.aid.jscp;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * todo
 *
 * @author: 04637@163.com
 * @date: 2020/7/22
 */
public class DeployAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        String path = this.getClass().getClassLoader().getResource("jscp.exe").getPath().replaceFirst("/", "");
        String configPath = this.getClass().getClassLoader().getResource("pscp.yml").getPath().replaceFirst("/", "");
        String pscpPath = this.getClass().getClassLoader().getResource("pscp.exe").getPath().replaceFirst("/", "");
        String command = path + " " + configPath + " " + pscpPath;
        // System.out.println(command);
        String line;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
}
