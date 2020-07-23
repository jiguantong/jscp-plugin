package dev.aid.jscp.settings;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

/**
 * Jscp View
 *
 * @author: 04637@163.com
 * @date: 2020/7/23
 */
public class JscpComponent {
    private final JPanel mainPanel;
    // server
    private final JBTextField ipText = new JBTextField();
    private final JBTextField sshPortText = new JBTextField();
    private final JBTextField userText = new JBTextField();
    private final JBPasswordField pwdText = new JBPasswordField();
    // project
    private final JBTextField remoteDirText = new JBTextField();
    private final TextFieldWithBrowseButton localDirChoose = new TextFieldWithBrowseButton();
    private final JBTextField cmdText = new JBTextField();
    public JscpComponent() {
        pwdText.setPasswordIsStored(true);
        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(new TitledSeparator("Server settings"))
                .addLabeledComponent(new JBLabel("Server ip: "),
                        ipText)
                .addLabeledComponent(new JBLabel("Ssh port: "), sshPortText)
                .addLabeledComponent(new JBLabel("User: "),
                        userText)
                .addLabeledComponent(new JBLabel("Password: "),
                        pwdText)
                .addComponent(new TitledSeparator("Project settings"))
                .addLabeledComponent(new JBLabel("Remote dir: "), remoteDirText)
                .addLabeledComponent(new JBLabel("Local dir: "), localDirChoose)
                .addSeparator()
                .addLabeledComponent(new JBLabel("Deploy cmd: "), cmdText)
                .addComponent(new JBTextArea("该命令将在文件上传完成后执行\n示例(部署tomcat项目后, 重启tomcat并追踪日志): \n"+
                        "source /etc/profile&&${TOMCAT_HOME}/bin/shutdown.sh&&${TOMCAT_HOME}/bin/startup.sh&&tail -f ${TOMCAT_HOME}/logs/catalina.out"))
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent(){
        return ipText;
    }

    public String getIpText() {
        return ipText.getText();
    }

    public void setIpText(String ip) {
        ipText.setText(ip);
    }

    public void setSshPortText(String port) {
        sshPortText.setText(port);
    }

    public String getSshPort() {
        return sshPortText.getText();
    }

    public void setUserText(String user) {
        userText.setText(user);
    }

    public String getUserText() {
        return userText.getText();
    }

    public void setPwdText(String pwd) {
        pwdText.setText(pwd);
    }

    public String getPwdText() {
        return pwdText.getText();
    }

    public void setRemoteDirText(String remoteDir) {
        remoteDirText.setText(remoteDir);
    }

    public String getRemoteDirText() {
        return remoteDirText.getText();
    }

    public void setLocalDirChoose(String localDir) {
        localDirChoose.setText(localDir);
    }

    public String getLocalDirChooseText() {
        return localDirChoose.getText();
    }

    public TextFieldWithBrowseButton getLocalDirChoose() {
        return localDirChoose;
    }

    public void setCmdText(String cmd) {
        cmdText.setText(cmd);
    }

    public String getCmdText() {
        return cmdText.getText();
    }
}
