package com.softvision.ipm.pms.support.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softvision.ipm.pms.common.constants.AuthorizeConstant;

@RestController
@RequestMapping(value="api/support", produces=MediaType.APPLICATION_JSON_VALUE)
public class SupportRest {

    @Value("${server.session.timeout}")
    private int sessionTimeout;

    @Autowired private SessionRegistry sessionRegistry;

    @PreAuthorize(AuthorizeConstant.IS_SUPPORT)
    @RequestMapping(value="sessions/active", method=RequestMethod.GET)
    public @ResponseBody List<SessionInformation> listLoggedInUsers() {
        List<SessionInformation> sessions = new ArrayList<>();

        Date now = Calendar.getInstance().getTime();
        sessionRegistry.getAllPrincipals().forEach(principal -> {
            if (principal != null) {
                List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
                if (allSessions != null) {
                    allSessions.stream().forEach(sessionInfo -> {
                        if (!sessionInfo.isExpired()) {
                            Date lastRequest = sessionInfo.getLastRequest();
                            long diffInMillis=now.getTime()-lastRequest.getTime();
                            if (diffInMillis >= 0 && diffInMillis < (sessionTimeout * 1000)) {
                                sessions.add(sessionInfo);
                            }
                        }
                    });
                }
            }
        });
        return sessions;
    }

}
