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

import java.util.Calendar;
import java.util.Date;

public class RepoInfoWithStatus {

  private RepoInfo repoInfo;
  private RepoStatus status;

  public RepoInfoWithStatus(
      RepoInfo repoInfo,
      boolean ready) {
    this.repoInfo = repoInfo;
    boolean expired = repoInfo.getExpiryDate() != null
        && new Date().after(repoInfo.getExpiryDate());
    this.status = ready
        ? (expired ? RepoStatus.EXPIRED : RepoStatus.READY)
        : RepoStatus.CREATING;
  }

  public Date getExpiryDate() {
    return repoInfo.getExpiryDate();
  }

  public String getRepoId() {
    return repoInfo.getRepoId();
  }

  public String getTaskId() {
    return repoInfo.getTaskId();
  }

  public boolean isUsingTestingVersion() {
    return repoInfo.isUsingTestingVersion();
  }

  public RepoStatus getStatus() {
    return status;
  }

  public String getVariant() {
    return repoInfo.getVariant();
  }

  public String getRemote() {
    return repoInfo.getRemote();
  }
}