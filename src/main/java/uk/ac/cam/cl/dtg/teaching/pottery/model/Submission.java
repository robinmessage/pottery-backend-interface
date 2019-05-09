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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class Submission {

  public static final String STATUS_PENDING = "PENDING";
  public static final String STATUS_RUNNING = "RUNNING";
  public static final String STATUS_FAILED = "FAILED";
  public static final String STATUS_COMPLETE = "COMPLETE";

  public static final String WAIT_STEP_NAME = "wait";

  private final String repoId;
  private final String tag;
  private final String status;
  private final String errorMessage;
  private final Date dateScheduled;
  private final boolean needsRetry;
  private final List<StepResult> steps;

  @JsonCreator
  public Submission(
      @JsonProperty("repoId") String repoId,
      @JsonProperty("tag") String tag,
      @JsonProperty("status") String status,
      @JsonProperty("steps") List<StepResult> steps,
      @JsonProperty("errorMessage") String errorMessage,
      @JsonProperty("dateScheduled") Date dateScheduled,
      @JsonProperty("needsRetry") boolean needsRetry) {
    super();
    this.repoId = repoId;
    this.tag = tag;
    this.status = status;
    this.steps = steps;
    this.errorMessage = errorMessage;
    this.dateScheduled = dateScheduled;
    this.needsRetry = needsRetry;
  }

  public static Builder builder(String repoId, String tag) {
    return new Builder(repoId, tag);
  }

  public boolean isNeedsRetry() {
    return needsRetry;
  }

  public Date getDateScheduled() {
    return dateScheduled;
  }

  public String getRepoId() {
    return repoId;
  }

  public String getTag() {
    return tag;
  }

  public String getStatus() {
    return status;
  }

  public List<StepResult> getSteps() {
    return steps;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public static class Builder {

    private final Date dateScheduled;
    private final String repoId;
    private final String tag;
    private String status;
    private final ArrayDeque<StepResult> steps = new ArrayDeque<>();
    private List<String> errorMessage = new ArrayList<>();
    private boolean needsRetry;

    private Builder(String repoId, String tag) {
      this.repoId = repoId;
      this.tag = tag;
      this.status = STATUS_PENDING;
      this.dateScheduled = new Date();
    }

    public Builder setStatus(String status) {
      this.status = status;
      return this;
    }

    public Builder setRetry() {
      this.needsRetry = true;
      return this;
    }

    public String currentStepName() {
      if (steps.isEmpty()) {
        return null;
      }
      StepResult currentStep = steps.getLast();
      if (isComplete(currentStep.getStatus())) {
        return null;
      }
      return currentStep.getName();
    }

    public Builder setStarted() {
      startStep(WAIT_STEP_NAME);
      return completeStep(WAIT_STEP_NAME, STATUS_COMPLETE,
          System.currentTimeMillis() - dateScheduled.getTime(), null);
    }

    public Builder startStep(String name) {
      if (this.currentStepName() != null) {
        throw new IllegalArgumentException();
      }
      this.steps.add(new StepResult(name, STATUS_RUNNING, 0));
      return this;
    }

    public Builder completeStep(String name, String status, long msec, @Nullable String output) {
      if (!this.currentStepName().equals(name)) {
        throw new IllegalArgumentException();
      }
      if (!isComplete(status)) {
        throw new IllegalArgumentException();
      }
      this.steps.offerLast(new StepResult(name, status, msec, output));
      return this;
    }

    public Builder addErrorMessage(String errorMessage) {
      this.errorMessage.add(errorMessage);
      return this;
    }

    public Submission build() {
      return new Submission(
          repoId,
          tag,
          status,
          steps.stream().collect(Collectors.toList()),
          errorMessage.stream().collect(Collectors.joining("\r\n")),
          dateScheduled,
          needsRetry);
    }
  }

  public boolean isComplete() {
    return isComplete(status);
  }

  private static boolean isComplete(String status) {
    return ImmutableSet.of(
            STATUS_COMPLETE,
            STATUS_FAILED)
        .contains(status);
  }

  @Override
  public String toString() {
    return "Submission{"
        + "repoId='"
        + repoId
        + '\''
        + ", tag='"
        + tag
        + '\''
        + ", steps='"
        + steps
        + '\''
        + ", dateScheduled="
        + dateScheduled
        + ", errorMessage='"
        + errorMessage
        + '\''
        + ", status='"
        + status
        + '\''
        + ", needsRetry="
        + needsRetry
        + '\''
        + '}';
  }
}
