package com.softvision.ipm.pms.support.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value="support", produces=MediaType.APPLICATION_JSON_VALUE)
public class SupportRest {

    @Autowired private SessionRegistry sessionRegistry;

    @PreAuthorize(AuthorizeConstant.IS_SUPPORT)
    @RequestMapping(value="list/active", method=RequestMethod.GET)
    public @ResponseBody List<SessionInformation> listLoggedInUsers() {
        List<SessionInformation> sessions = new ArrayList<>();

        sessionRegistry.getAllPrincipals().forEach(principal -> {
            if (principal != null) {
                List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
                if (allSessions != null) {
                    Optional<SessionInformation> optional = allSessions.stream().findFirst();
                    if (optional.isPresent()) {
                        sessions.add(optional.get());
                    }
                }
            }
        });
        return sessions;
    }

}
