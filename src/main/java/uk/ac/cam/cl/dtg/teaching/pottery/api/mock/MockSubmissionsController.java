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

package uk.ac.cam.cl.dtg.teaching.pottery.api.mock;

import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.core.Response;
import uk.ac.cam.cl.dtg.teaching.pottery.api.SubmissionsController;
import uk.ac.cam.cl.dtg.teaching.pottery.model.Submission;

public class MockSubmissionsController implements SubmissionsController {

  @Override
  public String scheduleTest(String repoId, String tag) {
    return Submission.builder(repoId, tag).setStatus(Submission.STATUS_PENDING).build().getOutput();
  }

  private AtomicInteger counter = new AtomicInteger(0);

  @Override
  public String getSubmission(String repoId, String tag) {
    return Submission.builder(repoId, tag)
        .setStatus(Submission.STATUS_COMPLETE)
        .setOutput(String.format("Polling %d", counter.incrementAndGet()))
        .build().getOutput();
  }

  @Override
  public Response deleteSubmission(String repoId, String tag) {
    throw new Error("Unimplemented");
  }
}
