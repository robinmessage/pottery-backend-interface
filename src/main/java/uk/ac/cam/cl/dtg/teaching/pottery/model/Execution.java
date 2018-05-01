/*
 * pottery-backend-interface - Backend API for testing programming exercises
 * Copyright © 2015 Andrew Rice (acr31@cam.ac.uk)
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class Execution {
  @ApiModelProperty("Image that this execution should be run in")
  private String image;
  @ApiModelProperty("Command-line for this execution")
  private String program;
  @ApiModelProperty("Container restrictions on this execution")
  private ContainerRestrictions restrictions;

  @JsonCreator
  public Execution(
      @JsonProperty("image") String image,
      @JsonProperty("program") String program,
      @JsonProperty("restrictions") ContainerRestrictions restrictions) {
    this.image = image;
    this.program = program;
    this.restrictions = restrictions;
  }

  public String getImage() {
    return image;
  }

  public String getProgram() {
    return program;
  }

  public ContainerRestrictions getRestrictions() {
    return restrictions;
  }

  void setDefaultContainerRestriction(ContainerRestrictions defaultRestrictions) {
    if (restrictions == null) {
      restrictions = defaultRestrictions;
    }
  }

  public Execution withProgram(String program) {
    return new Execution(image, program, restrictions);
  }
}