// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.selenium.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.testing.drivers.Browser.FIREFOX;
import static org.openqa.selenium.testing.drivers.Browser.HTMLUNIT;
import static org.openqa.selenium.testing.drivers.Browser.IE;
import static org.openqa.selenium.testing.drivers.Browser.SAFARI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.testing.Ignore;
import org.openqa.selenium.testing.JupiterTestBase;
import org.openqa.selenium.testing.drivers.WebDriverBuilder;

import java.util.Set;
import java.util.logging.Level;

@Ignore(HTMLUNIT)
@Ignore(IE)
@Ignore(FIREFOX)
@Ignore(SAFARI)
class PerformanceLogTypeTest extends JupiterTestBase {

  private WebDriver localDriver;

  @AfterEach
  public void quitDriver() {
    if (localDriver != null) {
      localDriver.quit();
      localDriver = null;
    }
  }

  @Test
  void performanceLogShouldBeDisabledByDefault() {
    Set<String> logTypes = driver.manage().logs().getAvailableLogTypes();
    assertThat(logTypes.contains(LogType.PERFORMANCE))
        .describedAs("Performance log should not be enabled by default").isFalse();
  }

  void createLocalDriverWithPerformanceLogType() {
    LoggingPreferences logPrefs = new LoggingPreferences();
    logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
    Capabilities caps = new ImmutableCapabilities(CapabilityType.LOGGING_PREFS, logPrefs);
    localDriver = new WebDriverBuilder().get(caps);
  }

  @Test
  void shouldBeAbleToEnablePerformanceLog() {
    createLocalDriverWithPerformanceLogType();
    Set<String> logTypes = localDriver.manage().logs().getAvailableLogTypes();
    assertThat(logTypes.contains(LogType.PERFORMANCE))
        .describedAs("Profiler log should be enabled").isTrue();
  }

  @Test
  void pageLoadShouldProducePerformanceLogEntries() {
    createLocalDriverWithPerformanceLogType();
    localDriver.get(pages.simpleTestPage);
    LogEntries entries = localDriver.manage().logs().get(LogType.PERFORMANCE);
    assertThat(entries).isNotEmpty();
  }
}
