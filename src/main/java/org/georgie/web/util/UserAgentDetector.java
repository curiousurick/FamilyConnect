package org.georgie.web.util;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class UserAgentDetector
{
    public boolean isMobile(HttpServletRequest request)
    {
        String userAgent = request.getHeader(USER_AGENT);
        for (String mobileAgent : MOBILE_DEVICES)
        {
            if (userAgent.contains(mobileAgent))
            {
                return true;
            }
        }
        return false;
    }
    //USER AGENT
    private static final String USER_AGENT = "User-Agent";
    // MOBILE USER AGENTS
    private static final String WEB_KIT = "webkit";
    private static final String DEVICE_ANDROID = "android";
    private static final String DEVICE_IPHONE = "iphone";
    private static final String DEVICE_IPOD = "ipod";
    private static final String DEVICE_IOS = "iOS";
    private static final List<String> MOBILE_DEVICES =
            Lists.newArrayList(
                    WEB_KIT,
                    DEVICE_ANDROID,
                    DEVICE_IPHONE,
                    DEVICE_IPOD,
                    DEVICE_IOS);
}
