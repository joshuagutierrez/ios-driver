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
package org.uiautomation.ios.wkrdp;

import org.json.JSONObject;
import org.openqa.selenium.WebDriverException;

/**
 * when something goes wrong with the WKRDP. Typically a remote JS error or an access to a non
 * existing node.
 */
public class RemoteExceptionException extends WebDriverException {

  private int code;
  private String message;
  private JSONObject command;

  public RemoteExceptionException(int code, String message, JSONObject command) {
    this.code = code;
    this.message = message;
    this.command = command;
  }

  public RemoteExceptionException(JSONObject error, JSONObject command) {
    this.code = error.optInt("code", -1);
    this.message = error.optString("message", "No message for the error.");
    this.command = command;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public int getCode() {
    return code;
  }
}
