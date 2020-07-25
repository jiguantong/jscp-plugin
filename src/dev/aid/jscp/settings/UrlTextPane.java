package dev.aid.jscp.settings;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * todo
 *
 * @author: 04637@163.com
 * @date: 2020/7/25
 */
public class UrlTextPane extends JTextPane {


    public UrlTextPane() {
        this.setEditable(false);
        this.addHyperlinkListener(new UrlHyperlinkListener());
        this.setContentType("text/html");
    }

    public UrlTextPane(String url) {
        this.setEditable(false);
        this.addHyperlinkListener(new UrlHyperlinkListener(url));
        this.setContentType("text/html");
        this.setText(url);
    }

    private class UrlHyperlinkListener implements HyperlinkListener {
        private String url;

        public UrlHyperlinkListener() {
        }

        public UrlHyperlinkListener(String url) {
            this.url = url;
        }

        @Override
        public void hyperlinkUpdate(final HyperlinkEvent event) {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (final IOException e) {
                    throw new RuntimeException("Can't open URL", e);
                } catch (final URISyntaxException e) {
                    throw new RuntimeException("Can't open URL", e);
                }
            }
        }
    }


    /**
     * Set the text, first translate it into HTML:
     */
    @Override
    public void setText(final String input) {
        final StringBuilder answer = new StringBuilder();
        answer.append("<html><body style=\"font-size: 9px;font-family: 微软雅黑, sans-serif\">");
        answer.append("<a href=\"" + input + "\">" + input + "</a>");
        answer.append("</body></html>");
        super.setText(answer.toString().replace("\n", "<br />"));
    }
}
