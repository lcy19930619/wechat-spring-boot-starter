package net.jlxxw.wechat.log.facade.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author 冷春阳
 * @date 2024-02-05 10:58
 */
public class WeChatLogbackAdapter extends JoranConfigurator {
    @Override
    public void setContext(Context context) {
        super.setContext(context);
    }

    public void configure(URL url) throws Exception {
        InputStream in = null;
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            in = urlConnection.getInputStream();
            doConfigure(in, url.toExternalForm());
        } catch (IOException ioe) {
            String errMsg = "Could not open URL [" + url + "].";
            addError(errMsg, ioe);
            throw new JoranException(errMsg, ioe);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    String errMsg = "Could not close input stream";
                    addError(errMsg, ioe);
                    throw new JoranException(errMsg, ioe);
                }
            }
        }
    }

}
