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

package uk.ac.cam.cl.dtg.teaching.pottery.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import java.util.Collection;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import uk.ac.cam.cl.dtg.teaching.pottery.exceptions.RetiredTaskException;
import uk.ac.cam.cl.dtg.teaching.pottery.exceptions.TaskNotFoundException;
import uk.ac.cam.cl.dtg.teaching.pottery.exceptions.TaskStorageException;
import uk.ac.cam.cl.dtg.teaching.pottery.model.BuilderInfo;
import uk.ac.cam.cl.dtg.teaching.pottery.model.TaskInfo;
import uk.ac.cam.cl.dtg.teaching.pottery.model.TaskLocation;
import uk.ac.cam.cl.dtg.teaching.pottery.model.TaskStatus;

@Produces("application/json")
@Path("/tasks")
@Api(
    value = "/tasks",
    description = "Manages the descriptions of the programming questions.",
    position = 0)
public interface TasksController {
  @GET
  @Path("/registered")
  @ApiOperation(
      value = "Lists all registered tasks",
      response = TaskInfo.class,
      responseContainer = "List",
      position = 0)
  Collection<TaskInfo> listRegistered();

  @GET
  @Path("/")
  @ApiOperation(
      value = "List the ids of all tasks (not retired) that exist",
      response = String.class,
      responseContainer = "List")
  Collection<String> listAll();

  @GET
  @Path("/retired")
  @ApiOperation(
      value = "List the ids of all retired tasks",
      response = String.class,
      responseContainer = "List")
  Collection<String> listRetired();

  @GET
  @Path("/testing")
  @ApiOperation(
      value = "Lists all tasks with a testing version",
      response = TaskInfo.class,
      responseContainer = "List",
      position = 0)
  Collection<TaskInfo> listTesting();

  @POST
  @Path("/create")
  @ApiOperation(value = "Create a new task", response = TaskLocation.class)
  TaskLocation create(@Context UriInfo uriInfo) throws TaskStorageException;

  @POST
  @Path("/create_remote")
  @ApiOperation(
      value = "Create a remote task (whose definition will be stored on another server)",
      response = TaskLocation.class)
  TaskLocation createRemote(@FormParam("remote") String remote) throws TaskStorageException;

  @GET
  @Path("/{taskId}")
  @ApiOperation(value = "Returns information about a specific task", response = TaskInfo.class)
  TaskInfo getTask(@PathParam("taskId") String taskId) throws TaskNotFoundException;

  @GET
  @Path("/{taskId}/status")
  @ApiOperation(
      value = "Returns the SHA1 of refs/heads/master for this task",
      response = String.class)
  TaskStatus getStatus(@PathParam("taskId") String taskId)
      throws TaskNotFoundException, TaskStorageException;

  @POST
  @Path("/{taskId}/retire")
  @ApiOperation(value = "Mark a task as retired", response = TaskInfo.class)
  Response retireTask(@PathParam("taskId") String taskId)
      throws TaskNotFoundException, TaskStorageException, RetiredTaskException;

  @POST
  @Path("/{taskId}/unretire")
  @ApiOperation(value = "Restore a retired task", response = TaskInfo.class)
  Response unretireTask(@PathParam("taskId") String taskId)
      throws TaskNotFoundException, TaskStorageException, RetiredTaskException;

  @POST
  @Path("/{taskId}/register")
  @ApiOperation(
      value =
          "Registers (or updates the registered version) of a task. If sha1 is not specified then "
              + "HEAD is used.")
  BuilderInfo scheduleTaskRegistration(
      @PathParam("taskId") String taskId, @FormParam("sha1") String sha1)
      throws TaskNotFoundException, RetiredTaskException;

  @GET
  @Path("/{taskId}/registering_status")
  @ApiOperation(value = "Polls the progress of the current registration process.")
  BuilderInfo pollTaskRegistrationStatus(@PathParam("taskId") String taskId)
      throws TaskNotFoundException;

  @POST
  @Path("/{taskId}/update")
  @ApiOperation(value = "Registers (or updates the testing version) of a task.")
  BuilderInfo scheduleTaskTesting(@PathParam("taskId") String taskId)
      throws TaskNotFoundException, RetiredTaskException;

  @GET
  @Path("/{taskId}/update_status")
  @ApiOperation(value = "Polls the progress of the current testing registration process.")
  BuilderInfo pollTaskTestingStatus(@PathParam("taskId") String taskId)
      throws TaskNotFoundException;

  @GET
  @Path("/types")
  @ApiOperation(value = "Lists defined task types and their description", position = 1)
  Map<String, String> listTypes();
}
