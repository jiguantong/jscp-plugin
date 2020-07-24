package dev.aid.jscp;

import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.util.io.BaseOutputReader;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
    private static URI pscpExePath = null;
    private static URI jscpExePath = null;

    public DeployAction() {
        // if (consoleView == null) {
        //     consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(ProjectManager.getInstance().getOpenProjects()[0]).getConsole();
        // }
        URI uri = getJarURI();
        if (uri == null) {
            return;
        }
        pscpExePath = getFile(uri, "pscp.exe");
        jscpExePath = getFile(uri, "jscp.exe");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        String basePath = project.getBasePath();
        String path = this.getClass().getClassLoader().getResource("jscp.exe").getPath().replaceFirst("/", "");
        String configPath = basePath + "/.idea/jscpSettings.xml";
        String pscpPath = this.getClass().getClassLoader().getResource("pscp.exe").getPath().replaceFirst("/", "");
        String mainCommand;
        if (pscpExePath == null || jscpExePath == null) {
            mainCommand = path + " " + configPath + " " + pscpPath;
        } else {
            mainCommand = jscpExePath.getPath() + " " + configPath + " " + pscpExePath.getPath().replaceFirst("/", "");
        }
        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("Jscp");
        // 强制显示window
        toolWindow.show(() -> {
        });
        if (consoleView == null) {
            consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(e.getProject()).getConsole();
        } else {
            consoleView.clear();
        }
        Content consoleContent = toolWindow.getContentManager().findContent("Console");
        if (consoleContent == null) {
            consoleContent = toolWindow.getContentManager().getFactory().createContent(consoleView.getComponent(), "Console", false);
            toolWindow.getContentManager().addContent(consoleContent);
            // consoleView.print(command + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
            if (!new File(configPath).exists()) {
                consoleView.print("Please config Jscp: File => Settings => Tools => Jscp Plugin\n", ConsoleViewContentType.ERROR_OUTPUT);
                return;
            } else {
                consoleView.print("You can modify settings of Jscp: File => Settings => Tools => Jscp Plugin\n", ConsoleViewContentType.LOG_WARNING_OUTPUT);
            }
        }

        Runtime runtime = Runtime.getRuntime();
        new Thread(() -> {
            try {
                if (process != null) {
                    process.destroy();
                }
                if (processHandler != null) {
                    processHandler.destroyProcess();
                }
                // build 命令
                String buildCmd = readBuildCmdFromXml(configPath);
                if (!StringUtils.isEmpty(buildCmd)) {
                    buildCmd = "cmd /c " + buildCmd;
                    process = runtime.exec(buildCmd, null, new File(basePath));
                    processHandler =
                            new OSProcessHandler(process, buildCmd, Charset.defaultCharset()) {
                                @NotNull
                                @Override
                                protected BaseOutputReader.Options readerOptions() {
                                    return BaseOutputReader.Options.forMostlySilentProcess();
                                }
                            };
                    processHandler.startNotify();
                    consoleView.attachToProcess(processHandler);
                }

                // 如果需要build且build失败, 结束
                if (!StringUtils.isEmpty(buildCmd)) {
                    int buildCode = process.waitFor();
                    process.destroy();
                    processHandler.destroyProcess();
                    if (buildCode != 0) {
                        consoleView.print("### => Build failed!\n", ConsoleViewContentType.ERROR_OUTPUT);
                        return;
                    } else {
                        consoleView.print("### => Build complete.\n\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
                    }
                }

                // 主命令
                process = runtime.exec(mainCommand);
                processHandler =
                        new OSProcessHandler(process, mainCommand, Charset.defaultCharset()) {
                            @NotNull
                            @Override
                            protected BaseOutputReader.Options readerOptions() {
                                return BaseOutputReader.Options.forMostlySilentProcess();
                            }
                        };
                processHandler.startNotify();
                consoleView.attachToProcess(processHandler);
            } catch (IOException | InterruptedException exp) {
                exp.printStackTrace();
            }
        }).start();
    }

    private URI getJarURI() {
        URL classResource = DeployAction.class.getResource(DeployAction.class.getSimpleName() + ".class");
        final String url = classResource.toString();
        final String suffix = DeployAction.class.getCanonicalName().replace('.', '/') + ".class";
        if (!url.endsWith(suffix)) return null; // weird URL

        // strip the class's path from the URL string
        final String base = url.substring(0, url.length() - suffix.length());

        String path = base;

        // remove the "jar:" prefix and "!/" suffix, if present
        if (path.startsWith("jar:")) path = path.substring(4, path.length() - 2);

        try {
            return new URI(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private URI getFile(final URI where,
                        final String fileName) {
        final File location;
        URI fileURI = null;

        location = new File(where);

        // not in a JAR, just return the path on disk
        if (location.isDirectory()) {
            fileURI = URI.create(where.toString() + fileName);
        } else {
            final ZipFile zipFile;
            try {
                zipFile = new ZipFile(location);
                try {
                    fileURI = extract(zipFile, fileName);
                } finally {
                    zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (fileURI);
    }

    private URI extract(final ZipFile zipFile,
                        final String fileName)
            throws IOException {
        final File tempFile;
        final ZipEntry entry;
        final InputStream zipStream;
        OutputStream fileStream;

        tempFile = File.createTempFile(fileName, Long.toString(System.currentTimeMillis()));
        tempFile.deleteOnExit();
        entry = zipFile.getEntry(fileName);

        if (entry == null) {
            throw new FileNotFoundException("cannot find file: " + fileName + " in archive: " + zipFile.getName());
        }

        zipStream = zipFile.getInputStream(entry);
        fileStream = null;

        try {
            final byte[] buf;
            int i;

            fileStream = new FileOutputStream(tempFile);
            buf = new byte[1024];
            i = 0;

            while ((i = zipStream.read(buf)) != -1) {
                fileStream.write(buf, 0, i);
            }
        } finally {
            close(zipStream);
            close(fileStream);
        }

        return (tempFile.toURI());
    }

    private void close(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String readBuildCmdFromXml(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(file);
            Element root = doc.getRootElement();
            List<Element> nodes = root.getChildren();
            Element component = nodes.get(0);
            List<Element> options = component.getChildren();
            for (Element option : options) {
                if ("buildCmd".equals(option.getAttributeValue("name"))) {
                    return option.getAttributeValue("value");
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // private void log(String log) {
    //     consoleView.print("---log--- " + log + "\n", ConsoleViewContentType.LOG_INFO_OUTPUT);
    // }
}
