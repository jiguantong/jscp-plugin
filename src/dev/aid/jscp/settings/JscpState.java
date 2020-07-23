package dev.aid.jscp.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Jscp Model
 *
 * @author: 04637@163.com
 * @date: 2020/7/23
 */
@State(
        name = "dev.aid.jscp.settings.JscpState",
        storages = {@Storage("jscpSettings.xml")}
)
public class JscpState implements PersistentStateComponent<JscpState> {

    public String ip;
    public String user;
    public String port;
    public String pwd;
    public String remoteDir;
    public String localDir;
    public String cmd;

    @Nullable
    @Override
    public JscpState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull JscpState jscpState) {
        XmlSerializerUtil.copyBean(jscpState, this);
    }

    public static JscpState getInstance(Project project) {
        return ServiceManager.getService(project, JscpState.class);
    }
}
