package dev.aid.jscp;

import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.DefaultJavaProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.largeFilesEditor.editor.EditorManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.util.io.BaseOutputReader;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import sun.java2d.loops.ProcessPath;

/**
 * Jscp Action
 *
 * @author: 04637@163.com
 * @date: 2020/7/22
 */
public class DeployAction extends AnAction {

    private static Process process = null;
    private static ProcessHandler processHandler = null;
    private static ConsoleView consoleView = null;

    public DeployAction() {
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String basePath = project.getBasePath();
        String path = this.getClass().getClassLoader().getResource("jscp.exe").getPath().replaceFirst("/", "");
        String configPath = basePath + "/.idea/jscpSettings.xml";
        String pscpPath = this.getClass().getClassLoader().getResource("pscp.exe").getPath().replaceFirst("/", "");
        String command = path + " " + configPath + " " + pscpPath;
        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("Jscp");
        // 强制显示window
        toolWindow.show(() -> {});
        if (consoleView == null) {
            consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(e.getProject()).getConsole();
        }
        Content currentContent = toolWindow.getContentManager().findContent("Console");
        if (currentContent == null) {
            currentContent = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Console", false);
            toolWindow.getContentManager().addContent(currentContent);
        }

        Runtime runtime = Runtime.getRuntime();
        try {
            if (process != null) {
                process.destroy();
            }
            if (processHandler != null) {
                processHandler.destroyProcess();
            }
            process = runtime.exec(command);
            processHandler =
                    new OSProcessHandler(process, command, StandardCharsets.UTF_8) {
                        @NotNull
                        @Override
                        protected BaseOutputReader.Options readerOptions() {
                            return BaseOutputReader.Options.forMostlySilentProcess();
                        }
                    };
            processHandler.startNotify();
            consoleView.attachToProcess(processHandler);
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
}
