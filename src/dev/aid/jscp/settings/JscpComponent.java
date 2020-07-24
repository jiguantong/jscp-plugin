package dev.aid.jscp.settings;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import java.awt.Dimension;

import javax.swing.JComponent;
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
    // cmd
    private final JBTextField buildCmdText = new JBTextField();
    private final JBTextField deployCmdText = new JBTextField();

    public JscpComponent() {
        JBTextArea tip = new JBTextArea("Example (restart tomcat and track logs): \n" +
                "   source /etc/profile&&${TOMCAT_HOME}/bin/shutdown.sh&&${TOMCAT_HOME}/bin/startup.sh&&tail -f ${TOMCAT_HOME}/logs/catalina.out");
        tip.setEditable(false);
        tip.setLineWrap(true);
        remoteDirText.getEmptyText().setText("The local directory/file will be uploaded to the directory.");
        deployCmdText.getEmptyText().setText("After uploading, the command will be executed on the server. Nullable.");
        buildCmdText.getEmptyText().setText("Before starting the upload, the command will be executed locally. Nullable.");
        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(new TitledSeparator("Server settings"))
                .addLabeledComponent(new JBLabel("Server ip: "),
                        ipText)
                .addLabeledComponent(new JBLabel("Ssh port: "), sshPortText)
                .addLabeledComponent(new JBLabel("User name: "),
                        userText)
                .addLabeledComponent(new JBLabel("Password: "),
                        pwdText)
                .addComponent(new TitledSeparator("Project settings"))
                .addLabeledComponent(new JBLabel("Remote dir: "), remoteDirText)
                .addLabeledComponent(new JBLabel("Local dir/file: "), localDirChoose)
                .addComponent(new TitledSeparator("Command"))
                .addLabeledComponent(new JBLabel("Build cmd: "), buildCmdText)
                .addLabeledComponent(new JBLabel("Deploy cmd: "), deployCmdText)
                .addComponent(tip)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        mainPanel.setPreferredSize(new Dimension(400, -1));
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
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
        return new String(pwdText.getPassword());
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

    public void setDeployCmdText(String cmd) {
        deployCmdText.setText(cmd);
    }

    public String getDeployCmdText() {
        return deployCmdText.getText();
    }

    public void setBuildCmdText(String cmd) {
        buildCmdText.setText(cmd);
    }

    public String getBuildCmdText() {
        return buildCmdText.getText();
    }
}
