package dev.aid.jscp.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

/**
 * Jscp Controller
 *
 * @author: 04637@163.com
 * @date: 2020/7/23
 */
public class JscpConfigurable implements Configurable {

    private JscpComponent component;
    private final Project project;

    public JscpConfigurable(Project project) {
        this.project = project;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Jscp Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return component.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        component = new JscpComponent();
        component.getLocalDirChoose().addBrowseFolderListener(
                "Select local dir", "", project,
                new FileChooserDescriptor(true, true, true, true, false, false) {
                    // @Override
                    // public boolean isFileSelectable(VirtualFile file) {
                    //     return true;
                    // }
                }
        );
        return component.getPanel();
    }

    @Override
    public boolean isModified() {
        JscpState settings = JscpState.getInstance(project);
        boolean modified = !component.getIpText().equalsIgnoreCase(settings.ip);
        modified |= !component.getSshPort().equals(settings.port);
        modified |= !component.getUserText().equals(settings.user);
        modified |= !component.getPwdText().equals(settings.pwd);
        modified |= !component.getRemoteDirText().equals(settings.remoteDir);
        modified |= !component.getLocalDirChooseText().equals(settings.localDir);
        modified |= !component.getCmdText().equals(settings.cmd);
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        JscpState settings = JscpState.getInstance(project);
        settings.ip = component.getIpText();
        settings.port = component.getSshPort();
        settings.user = component.getUserText();
        settings.pwd = component.getPwdText();
        settings.localDir = component.getLocalDirChooseText();
        settings.remoteDir = component.getRemoteDirText();
        settings.cmd = component.getCmdText();
    }

    @Override
    public void reset() {
        JscpState settings = JscpState.getInstance(project);
        component.setIpText(settings.ip);
        if (StringUtils.isEmpty(settings.user)) {
            settings.user = "root";
        }
        if (StringUtils.isEmpty(settings.port)) {
            settings.port = "22";
        }
        component.setSshPortText(settings.port);
        component.setUserText(settings.user);
        component.setPwdText(settings.pwd);
        component.setLocalDirChoose(settings.localDir);
        component.setRemoteDirText(settings.remoteDir);
        component.setCmdText(settings.cmd);
    }

    @Override
    public void disposeUIResources() {
        component = null;
    }
}
