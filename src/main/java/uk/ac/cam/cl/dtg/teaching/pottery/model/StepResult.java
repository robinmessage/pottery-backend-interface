/*
 * pottery-backend-interface - Backend API for testing programming exercises
 * Copyright © 2015-2018 BlueOptima Limited, Andrew Rice (acr31@cam.ac.uk)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.cam.cl.dtg.teaching.pottery.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StepResult {
  private final String name;
  private final String status;
  private final long msec;
  private final String output;

  @JsonCreator
  public StepResult(
      @JsonProperty("name") String name,
      @JsonProperty("status") String status,
      @JsonProperty("msec") long msec) {
    this.name = name;
    this.status = status;
    this.msec = msec;
    this.output = null;
  }

  StepResult(String name, String status, long msec, String output) {
    this.name = name;
    this.status = status;
    this.msec = msec;
    this.output = output;
  }

  public String getName() {
    return name;
  }

  public String getStatus() {
    return status;
  }

  public long getMsec() {
    return msec;
  }

  @JsonIgnore
  public String getOutput() {
    return output;
  }
}
