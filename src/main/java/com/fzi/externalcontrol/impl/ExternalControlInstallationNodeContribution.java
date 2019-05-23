// -- BEGIN LICENSE BLOCK ----------------------------------------------
// Copyright 2019 FZI Forschungszentrum Informatik
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// -- END LICENSE BLOCK ------------------------------------------------

//----------------------------------------------------------------------
/*!\file
 *
 * \author  Lea Steffen steffen@fzi.de
 * \date    2019-04-18
 *
 */
//----------------------------------------------------------------------

package com.fzi.externalcontrol.impl;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class ExternalControlInstallationNodeContribution implements InstallationNodeContribution {
  private static final String HOST_IP = "192.168.0.5"; // "127.0.0.1";
  private static final String PORT_NR = "50002";
  private String urScriptProgram = "";
  private final RequestProgram sender;
  private static final String DEFAULT_IP = "192.168.0.5";
  private static final String DEFAULT_PORT = "50002";
  private DataModel model;
  private final ExternalControlInstallationNodeView view;
  private final KeyboardInputFactory keyboardFactory;

  public ExternalControlInstallationNodeContribution(InstallationAPIProvider apiProvider,
      ExternalControlInstallationNodeView view, DataModel model) {
    this.keyboardFactory =
        apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
    this.model = model;
    this.view = view;
    this.sender = new RequestProgram(getHostIP(), getHostPort());
  }

  @Override
  public void openView() {}

  @Override
  public void closeView() {}

  public boolean isDefined() {
    return !getHostIP().isEmpty();
  }

  @Override
  public void generateScript(ScriptWriter writer) {
    urScriptProgram = sender.sendCommand("request_program");
  }

  // IP helper functions
  public void setHostIP(String ip) {
    if ("".equals(ip)) {
      resetToDefaultIP();
    } else {
      model.set(HOST_IP, ip);
    }
  }

  public String getHostIP() {
    return model.get(HOST_IP, DEFAULT_IP);
  }

  private void resetToDefaultIP() {
    model.set(HOST_IP, DEFAULT_IP);
  }

  public KeyboardTextInput getInputForIPTextField() {
    KeyboardTextInput keyboInput = keyboardFactory.createStringKeyboardInput();
    keyboInput.setInitialValue(getHostIP());
    return keyboInput;
  }

  public KeyboardInputCallback<String> getCallbackForIPTextField() {
    return new KeyboardInputCallback<String>() {
      @Override
      public void onOk(String value) {
        setHostIP(value);
        view.UpdateIPTextField(value);
      }
    };
  }

  // port helper functions
  public void setHostPort(String port) {
    if ("".equals(port)) {
      resetToDefaultPort();
    } else {
      model.set(PORT_NR, port);
    }
  }

  public String getHostPort() {
    return model.get(PORT_NR, DEFAULT_PORT);
  }

  private void resetToDefaultPort() {
    model.set(PORT_NR, DEFAULT_PORT);
  }

  public KeyboardTextInput getInputForPortTextField() {
    KeyboardTextInput keyboInput = keyboardFactory.createStringKeyboardInput();
    keyboInput.setInitialValue(getHostPort());
    return keyboInput;
  }

  public KeyboardInputCallback<String> getCallbackForPortTextField() {
    return new KeyboardInputCallback<String>() {
      @Override
      public void onOk(String value) {
        setHostPort(value);
        view.UpdatePortTextField(value);
      }
    };
  }

  public String getUrScriptProgram() {
    return urScriptProgram;
  }
}