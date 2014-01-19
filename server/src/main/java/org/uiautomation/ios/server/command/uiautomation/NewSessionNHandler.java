/*
 * Copyright 2012-2013 eBay Software Foundation and ios-driver committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.uiautomation.ios.server.command.uiautomation;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.Response;
import org.uiautomation.ios.IOSCapabilities;
import org.uiautomation.ios.communication.WebDriverLikeRequest;
import org.uiautomation.ios.server.IOSServerManager;
import org.uiautomation.ios.server.ServerSideSession;
import org.uiautomation.ios.server.command.BaseNativeCommandHandler;

import java.util.logging.Logger;

public class NewSessionNHandler extends BaseNativeCommandHandler {


  private static final int MAX_TRIES = 3;
  private static final Logger log = Logger.getLogger(NewSessionNHandler.class.getName());
  private Exception lastException;

  public NewSessionNHandler(IOSServerManager driver, WebDriverLikeRequest request) {
    super(driver, request);
  }

  public Response handle() throws Exception {

    JSONObject payload = getRequest().getPayload();
    IOSCapabilities cap = new IOSCapabilities(payload.getJSONObject("desiredCapabilities"));

    int nbTries = 0;
    ServerSideSession session = createSession(cap);
    while (session == null && nbTries < MAX_TRIES) {
      log.warning("Couldn't start instruments properly");
      session = createSession(cap);
      nbTries++;
    }

    if (session == null) {
      throw new SessionNotCreatedException(lastException.getMessage(), lastException);
    }
    Response resp = new Response();
    resp.setSessionId(session.getSessionId());
    resp.setStatus(0);
    resp.setValue("");
    return resp;
  }

  private ServerSideSession createSession(IOSCapabilities cap) {
    ServerSideSession session = null;
    try {
      session = getServer().createSession(cap);
      session.start();
      return session;
    } catch (Exception e) {
      lastException = e;
      if (session != null) {
        session.stop();
      }
    }
    return null;
  }

  @Override
  public JSONObject configurationDescription() throws JSONException {
    return noConfigDefined();
  }
}
